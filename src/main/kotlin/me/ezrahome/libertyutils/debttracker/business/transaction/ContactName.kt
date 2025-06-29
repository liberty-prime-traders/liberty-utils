package me.ezrahome.libertyutils.debttracker.business.transaction

import org.mapstruct.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class ContactName()
