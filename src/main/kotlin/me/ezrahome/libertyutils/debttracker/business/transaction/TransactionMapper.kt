package me.ezrahome.libertyutils.debttracker.business.transaction

import me.ezrahome.libertyutils.configuration.mapping.ContactName
import me.ezrahome.libertyutils.configuration.mapping.MapperConfig
import me.ezrahome.libertyutils.debttracker.business.contact.mapping.ContactBalance
import me.ezrahome.libertyutils.debttracker.business.contact.mapping.ContactBalanceQualifier
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionInsertDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionResponseDto
import me.ezrahome.libertyutils.debttracker.business.transaction.dto.TransactionUpdateDto
import me.ezrahome.libertyutils.debttracker.model.TransactionEntity
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(config = MapperConfig::class, uses = [ContactBalanceQualifier::class])
interface TransactionMapper {

    @Mapping(source = "userId", target = "contactName", qualifiedBy = [ContactName::class])
    @Mapping(source = "userId", target = "contactBalance", qualifiedBy = [ContactBalance::class])
    fun toResponseDto(entity: TransactionEntity): TransactionResponseDto

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: TransactionInsertDto): TransactionEntity

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: TransactionUpdateDto, @MappingTarget entity: TransactionEntity)

}
