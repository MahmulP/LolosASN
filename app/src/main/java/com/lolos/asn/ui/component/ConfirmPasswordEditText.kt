package com.lolos.asn.ui.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.lolos.asn.R
import com.lolos.asn.utils.dpToPx

class ConfirmPasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    private var originalPasswordView: PasswordEditText? = null

    fun setOriginalPasswordView(passwordEditText: PasswordEditText) {
        originalPasswordView = passwordEditText

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val originalPassword = originalPasswordView?.text.toString()
                val confirmPassword = s.toString()

                error = if (originalPassword != confirmPassword) {
                    context.getString(R.string.password_confirm_error)
                } else {
                    null
                }

                if (error != null) {
                    setPadding(paddingLeft, paddingTop, 40.dpToPx(context), paddingBottom)
                } else {
                    // Reset padding if there's no error
                    setPadding(paddingLeft, paddingTop, 0, paddingBottom)
                }
            }
        })
    }
}

