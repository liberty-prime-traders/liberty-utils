package me.ezrahome.libertyutils.debttracker.business.transaction.mapping

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

@Mapper(config = MapperConfig::class,
    uses = [ContactBalanceQualifier::class, TransactionMappingQualifier::class]
)
interface TransactionMapper {

    @Mapping(source = "userId", target = "contactName", qualifiedBy = [ContactName::class])
    @Mapping(source = "userId", target = "contactBalance", qualifiedBy = [ContactBalance::class])
    @Mapping(source = ".", target = "amount", qualifiedBy = [SignedAmount::class])
    fun toResponseDto(entity: TransactionEntity): TransactionResponseDto

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amount", source = "amount", qualifiedBy = [AbsoluteValue::class])
    fun toEntity(dto: TransactionInsertDto): TransactionEntity

    @Mapping(target = "location", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "amount", source = "amount", qualifiedBy = [AbsoluteValue::class])
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(dto: TransactionUpdateDto, @MappingTarget entity: TransactionEntity)

    fun cloneEntity(entity: TransactionEntity): TransactionEntity

}
