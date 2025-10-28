package com.vfd.client.ui.components.texts

import android.annotation.SuppressLint
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.vfd.client.R
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import kotlin.math.min

@Composable
fun AppAmountTextField(
    amount: BigDecimal?,
    onAmountChange: (BigDecimal?) -> Unit,
    label: String = stringResource(id = R.string.amount),
    maxFractionDigits: Int = 2,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val sep = remember { DecimalFormatSymbols.getInstance().decimalSeparator }
    var text by remember(amount) {
        mutableStateOf(
            amount?.toPlainString()?.replace('.', sep) ?: ""
        )
    }

    fun sanitize(raw: String): String {
        if (raw.isBlank()) return ""
        var sepSeen = false
        val filtered = buildString(raw.length) {
            for (ch in raw) {
                when {
                    ch.isDigit() -> append(ch)
                    (ch == '.' || ch == ',') && !sepSeen -> {
                        append(sep)
                        sepSeen = true
                    }
                }
            }
        }
        val s = if (filtered.startsWith(sep)) "0$filtered" else filtered
        val i = s.indexOf(sep)
        if (i >= 0) {
            val intPart = s.substring(0, i)
            val fracPart = s.substring(i + 1)
            val trimmed = fracPart.substring(0, min(fracPart.length, maxFractionDigits))
            return if (trimmed.isEmpty()) "$intPart$sep" else "$intPart$sep$trimmed"
        }
        return s
    }

    fun parse(display: String): BigDecimal? {
        if (display.isBlank() || display == "$sep") return null
        return runCatching { BigDecimal(display.replace(sep, '.')) }.getOrNull()
    }

    AppTextField(
        value = text,
        onValueChange = {
            text = sanitize(it)
            onAmountChange(parse(text))
        },
        label = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        modifier = modifier,
        errorMessage = if (amount == null && text.isNotBlank()) stringResource(id = R.string.invalid_amount) else null
    )
}