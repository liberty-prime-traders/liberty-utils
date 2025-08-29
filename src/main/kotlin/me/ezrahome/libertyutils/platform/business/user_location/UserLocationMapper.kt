package me.ezrahome.libertyutils.platform.business.user_location

import me.ezrahome.libertyutils.configuration.mapping.FullName
import me.ezrahome.libertyutils.configuration.mapping.MapperConfig
import me.ezrahome.libertyutils.platform.model.UserLocationEntity
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
