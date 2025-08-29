package me.ezrahome.libertyutils.debttracker.business.contact.mapping

import org.mapstruct.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class ContactBalance()
