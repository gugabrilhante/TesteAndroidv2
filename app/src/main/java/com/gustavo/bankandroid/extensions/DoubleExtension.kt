package com.gustavo.bankandroid.extensions

import java.text.NumberFormat
import java.util.*

fun Double.toRealCurrency(): String {
    val ptBr = Locale("pt", "BR")
    return NumberFormat.getCurrencyInstance(ptBr).format(this)
}