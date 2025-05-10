package me.ezrahome.libertyutils.platform.business.audit

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class UiLabel(val value: String = "")
