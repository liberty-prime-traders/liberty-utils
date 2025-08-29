package me.ezrahome.libertyutils.dailysnapshot.business

import lombok.RequiredArgsConstructor
import me.ezrahome.libertyutils.configuration.security.LibertyPermissions
import me.ezrahome.libertyutils.dailysnapshot.ExtraFieldsCalculator
import me.ezrahome.libertyutils.dailysnapshot.model.DailySnapshotEntity
import me.ezrahome.libertyutils.platform.business.audit.AuditFetcher
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditDto
import me.ezrahome.libertyutils.platform.business.audit.MasterAuditor
import me.ezrahome.libertyutils.platform.business.user_location.UserLocationUtils
import me.ezrahome.libertyutils.reusable.constants.TableNames
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
    private val userLocationUtils: UserLocationUtils
) {

    @Transactional(readOnly = true)
    fun getSnapshotsForDates(startDate: String, endDate: String): Collection<DailySnapshotResponseDto> {
        val snapshotDateAfter = LocalDate.parse(startDate)
        val snapshotDateBefore = LocalDate.parse(endDate)

        return dailySnapshotRepository.findBySnapshotDateBetween(snapshotDateAfter, snapshotDateBefore)
            .filter { userLocationUtils.locationPredicate(it) }
            .map{dailySnapshotMapper.toResponseDto(it).apply {
                ExtraFieldsCalculator.calculateAndAppendExtraFields(this)
            }}
    }

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
            userLocationUtils.populateLocation(entity)
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
