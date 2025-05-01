package me.ezra_home.daily_snapshot

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
@RequiredArgsConstructor
class DailySnapshotService(
    private val dailySnapshotRepository: DailySnapshotRepository,
    private val dailySnapshotMapper: DailySnapshotMapper
) {

    fun getSnapshotsForDates(snapshotDateAfter: LocalDate, snapshotDateBefore: LocalDate): List<DailySnapshotResponseDto> {
        return dailySnapshotRepository.findBySnapshotDateBetween(snapshotDateAfter, snapshotDateBefore)
            .map{dailySnapshotMapper.toResponseDto(it).apply {
                ExtraFieldsCalculator.calculateAndAppendExtraFields(this)
            }}
    }

    fun createDailySnapshot(dailySnapshotInsertDto: DailySnapshotInsertDto): DailySnapshotResponseDto {
        if (dailySnapshotInsertDto.snapshotDate == null)
            throw RuntimeException("Snapshot date must be provided")
        val entity = dailySnapshotMapper.toEntity(dailySnapshotInsertDto)
        dailySnapshotRepository.save(entity)
        return dailySnapshotMapper.toResponseDto(entity)
            .apply { ExtraFieldsCalculator.calculateAndAppendExtraFields(this) }
    }

    fun updateDailySnapshot(dailySnapshotUpdateDto: DailySnapshotUpdateDto): DailySnapshotResponseDto {
        val entity = dailySnapshotRepository.findById(dailySnapshotUpdateDto.id!!)
            .orElseThrow { RuntimeException("Daily snapshot not found") }
        dailySnapshotMapper.partialUpdate(dailySnapshotUpdateDto, entity)
        dailySnapshotRepository.save(entity)
        return dailySnapshotMapper.toResponseDto(entity)
            .apply { ExtraFieldsCalculator.calculateAndAppendExtraFields(this) }
    }

    fun deleteDailySnapshot(id: UUID) {
        dailySnapshotRepository.deleteById(id)
    }
}
