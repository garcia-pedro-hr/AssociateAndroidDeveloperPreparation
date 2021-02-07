package com.phgarcia.aadp.utils

import android.content.Context
import com.ibm.icu.text.RuleBasedNumberFormat

fun Int.writtenInFull(context: Context): String {
    val currentLocale = context.resources.configuration.locales[0]
    val rule = RuleBasedNumberFormat(currentLocale, RuleBasedNumberFormat.SPELLOUT)
    return rule.format(this)
}