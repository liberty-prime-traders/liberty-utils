package me.ezrahome.libertyutils.debttracker.business.user

import me.ezrahome.libertyutils.debttracker.model.UserEntity
import me.ezrahome.libertyutils.platform.configuration.mapping.MapperConfig
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(config = MapperConfig::class)
interface UserMapper {

    fun toResponseDto(entity: UserEntity): UserResponseDto

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: UserInsertDto): UserEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: UserUpdateDto, @MappingTarget entity: UserEntity)

}
