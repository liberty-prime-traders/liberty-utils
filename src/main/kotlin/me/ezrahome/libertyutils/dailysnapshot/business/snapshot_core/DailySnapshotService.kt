package me.ezrahome.libertyutils.dailysnapshot.business.snapshot_core

import lombok.RequiredArgsConstructor
import me.ezrahome.libertyutils.dailysnapshot.ExtraFieldsCalculator
import me.ezrahome.libertyutils.dailysnapshot.business.user_location.UserLocationRepository
import me.ezrahome.libertyutils.dailysnapshot.model.DailySnapshotEntity
import me.ezrahome.libertyutils.dailysnapshot.model.UserLocationEntity
import me.ezrahome.libertyutils.platform.business.audit.AuditFetcher
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditDto
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditor
import me.ezrahome.libertyutils.platform.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.platform.configuration.session.SessionContextProvider
import me.ezrahome.libertyutils.platform.constants.TableNames
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
@RequiredArgsConstructor
class DailySnapshotService(
    private val dailySnapshotRepository: DailySnapshotRepository,
    private val dailySnapshotMapper: DailySnapshotMapper,
    private val masterAuditor: MasterAuditor,
    private val auditFetcher: AuditFetcher,
    private val userLocationRepository: UserLocationRepository
) {

    @Transactional(readOnly = true)
    fun getSnapshotsForDates(startDate: String, endDate: String): Collection<DailySnapshotResponseDto> {
        val snapshotDateAfter = LocalDate.parse(startDate)
        val snapshotDateBefore = LocalDate.parse(endDate)
        val currentLocation = getUserLocation()
        val predicate: (DailySnapshotEntity) -> Boolean = { snapshot ->
            LibertyPermissions.isLibertyAdmin() || snapshot.location == currentLocation.location
        }

        return dailySnapshotRepository.findBySnapshotDateBetween(snapshotDateAfter, snapshotDateBefore)
            .filter(predicate)
            .map{dailySnapshotMapper.toResponseDto(it).apply {
                ExtraFieldsCalculator.calculateAndAppendExtraFields(this)
            }}
    }

    private fun getUserLocation(): UserLocationEntity =
        userLocationRepository.findByUserIdAndEndOnNull(SessionContextProvider.getUserId())
            ?: throw RuntimeException("User location not found")

    @Transactional
    fun createDailySnapshot(dailySnapshotInsertDto: DailySnapshotInsertDto): DailySnapshotResponseDto {
        if (dailySnapshotInsertDto.snapshotDate == null)
            throw RuntimeException("Snapshot date must be provided")
        val entity = dailySnapshotMapper.toEntity(dailySnapshotInsertDto)
        populateLocation(entity)
        dailySnapshotRepository.save(entity)
        masterAuditor.logInsert(entity)
        return dailySnapshotMapper.toResponseDto(entity)
            .apply { ExtraFieldsCalculator.calculateAndAppendExtraFields(this) }
    }

    private fun populateLocation(entity: DailySnapshotEntity) {
        if (LibertyPermissions.isLibertyAdmin()){
            checkNotNull(entity.location) { "Location must be provided" }

        } else {
            val userLocation = userLocationRepository.findByUserIdAndEndOnNull(SessionContextProvider.getUserId())
                ?: throw RuntimeException("User location not found")
            entity.location = userLocation.location
        }
    }

    @Transactional
    fun updateDailySnapshot(dailySnapshotUpdateDto: DailySnapshotUpdateDto): DailySnapshotResponseDto {
        val entity = dailySnapshotRepository.findById(dailySnapshotUpdateDto.id!!)
            .orElseThrow { RuntimeException("Daily snapshot not found") }
        val oldEntityState = dailySnapshotMapper.cloneEntity(entity)
        dailySnapshotMapper.partialUpdate(dailySnapshotUpdateDto, entity)
        dailySnapshotRepository.save(entity)
        masterAuditor.logUpdate(oldEntityState, entity)
        return dailySnapshotMapper.toResponseDto(entity)
            .apply { ExtraFieldsCalculator.calculateAndAppendExtraFields(this) }
    }

    @Transactional
    fun deleteDailySnapshot(id: UUID) {
        dailySnapshotRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getAuditRecords(snapshotId: String): Collection<MasterAuditDto> {
        val recordId = UUID.fromString(snapshotId)
        return auditFetcher.getAuditRecords(TableNames.DAILY_SNAPSHOT, recordId, DailySnapshotEntity::class.java)
    }
}
