package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import me.ezrahome.libertyutils.platform.configuration.mapping.MapperConfig
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(config = MapperConfig::class)
interface TransactionMapper {

    @Mapping(source = "userID", target = "contactName", qualifiedBy = [ContactName::class])
    fun toResponseDto(entity: TransactionEntity): TransactionResponseDto

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: TransactionInsertDto): TransactionEntity

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: TransactionUpdateDto, @MappingTarget entity: TransactionEntity)

}
