package me.ezrahome.libertyutils.dailysnapshot.business.user_location

import me.ezrahome.libertyutils.dailysnapshot.model.UserLocationEntity
import me.ezrahome.libertyutils.platform.configuration.mapping.FullName
import me.ezrahome.libertyutils.platform.configuration.mapping.MapperConfig
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(config = MapperConfig::class)
interface UserLocationMapper {

    @Mapping(target = "userName", source = "userId", qualifiedBy = [FullName::class])
    fun toResponseDto(entity: UserLocationEntity): UserLocationResponseDto

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startOn", ignore = true)
    @Mapping(target = "endOn", ignore = true)
    fun toEntity(dto: UserLocationInsertDto): UserLocationEntity
}
