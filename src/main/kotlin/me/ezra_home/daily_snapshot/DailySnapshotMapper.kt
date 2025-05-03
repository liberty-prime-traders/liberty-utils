package me.ezra_home.daily_snapshot

import org.mapstruct.BeanMapping
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    unmappedSourcePolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = [MappingQualifier::class]
)
interface DailySnapshotMapper {

    @Mapping(target = "inflowCash", ignore = true)
    @Mapping(target = "grossInflow", ignore = true)
    @Mapping(target = "netInflow", ignore = true)
    @Mapping(target = "grossOutflow", ignore = true)
    @Mapping(target = "inflowBothAccounts", ignore = true)
    fun toResponseDto(entity: DailySnapshotEntity): DailySnapshotResponseDto

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    fun toEntity(dto: DailySnapshotInsertDto): DailySnapshotEntity

    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "snapshotDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: DailySnapshotUpdateDto, @MappingTarget entity: DailySnapshotEntity)

}
