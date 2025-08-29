package me.ezrahome.libertyutils.dailysnapshot.business

import me.ezrahome.libertyutils.configuration.mapping.MapperConfig
import me.ezrahome.libertyutils.dailysnapshot.model.DailySnapshotEntity
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(config = MapperConfig::class)
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
    @Mapping(target = "location", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: DailySnapshotUpdateDto, @MappingTarget entity: DailySnapshotEntity)

    fun cloneEntity(entity: DailySnapshotEntity): DailySnapshotEntity
}
