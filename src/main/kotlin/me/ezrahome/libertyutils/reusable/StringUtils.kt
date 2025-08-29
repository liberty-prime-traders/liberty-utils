package me.ezrahome.libertyutils.reusable

import org.apache.coyote.BadRequestException
import java.util.Optional

object StringUtils {

    fun getValueOrException(str: String?, exceptionMessage: String): String {
        return getValueOrException(Optional.ofNullable(str), exceptionMessage)
    }

    fun getValueOrException(str: Optional<String>?, exceptionMessage: String): String {
        if (hasValue(str)) {
            return str!!.get()
        }
        throw BadRequestException(exceptionMessage)
    }

    fun hasValue(str: String?): Boolean {
        return hasValue(Optional.ofNullable(str))
    }

    private fun hasValue(str: Optional<String>?): Boolean {
        return str != null && str.isPresent && str.get().isNotBlank()
    }

    fun isEquivalent(str1: String?, str2: String?): Boolean {
        if (str1.isNullOrBlank() && str2.isNullOrBlank()) {
            return true
        }
        if (str1.isNullOrBlank() && !str2.isNullOrBlank()) {
            return false
        }
        if (!str1.isNullOrBlank() && str2.isNullOrBlank()) {
            return false
        }
        val normalizedStr1 = removeAllSpaces(str1!!).lowercase()
        val normalizedStr2 = removeAllSpaces(str2!!).lowercase()
        return normalizedStr1 == normalizedStr2
    }

    private fun removeAllSpaces(str: String): String {
        return str.replace("\\s+".toRegex(), "")
    }
}
