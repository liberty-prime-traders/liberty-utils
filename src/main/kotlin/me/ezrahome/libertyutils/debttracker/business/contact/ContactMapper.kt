package me.ezrahome.libertyutils.debttracker.business.contact

import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactInsertDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactResponseDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactUpdateDto
import me.ezrahome.libertyutils.debttracker.model.ContactEntity
import me.ezrahome.libertyutils.platform.configuration.mapping.MapperConfig
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(config = MapperConfig::class)
interface ContactMapper {

    fun toResponseDto(entity: ContactEntity): ContactResponseDto

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: ContactInsertDto): ContactEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: ContactUpdateDto, @MappingTarget entity: ContactEntity)

}
