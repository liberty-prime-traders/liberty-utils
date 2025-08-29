package me.ezrahome.libertyutils.debttracker.business.contact.mapping

import me.ezrahome.libertyutils.configuration.mapping.MapperConfig
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactInsertDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactResponseDto
import me.ezrahome.libertyutils.debttracker.business.contact.dto.ContactUpdateDto
import me.ezrahome.libertyutils.debttracker.model.ContactEntity
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(config = MapperConfig::class, uses = [ContactBalanceQualifier::class])
interface ContactMapper {

    @Mapping(target = "balance", source = "id", qualifiedBy = [ContactBalance::class])
    fun toResponseDto(entity: ContactEntity): ContactResponseDto

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: ContactInsertDto): ContactEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: ContactUpdateDto, @MappingTarget entity: ContactEntity)

}
