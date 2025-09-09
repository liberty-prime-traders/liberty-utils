package me.ezrahome.libertyutils.debttracker.business.transaction.mapping

import org.mapstruct.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class AbsoluteValue()
