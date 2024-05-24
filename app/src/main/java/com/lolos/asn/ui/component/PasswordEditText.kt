package com.lolos.asn.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.lolos.asn.R
import com.lolos.asn.utils.dpToPx

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val passwordLength = s?.length ?: 0
                val containsNumber = s?.any { it.isDigit() } ?: false

                var lengthError: String? = null
                var formatError: String? = null

                if (passwordLength < 8) {
                    lengthError = context.getString(R.string.password_length_warning)
                }

                if (!containsNumber) {
                    formatError = context.getString(R.string.password_format_warning)
                }

                error = when {
                    lengthError != null && formatError != null -> "$lengthError\n$formatError"
                    lengthError != null -> lengthError
                    formatError != null -> formatError
                    else -> null
                }

                if (error != null) {
                    setPadding(paddingLeft, paddingTop, 40.dpToPx(context), paddingBottom)
                } else {
                    setPadding(paddingLeft, paddingTop, 0, paddingBottom)
                }
            }
        })
    }
}
