package com.example.storyapp.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.example.storyapp.R

class CustomEditTextEmail : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        addTextChangedListener(onTextChanged = { email, _, _, _ ->
            error =
                if (!Patterns.EMAIL_ADDRESS.matcher(email.toString())
                        .matches() && !email.isNullOrEmpty()
                ) context.getString(R.string.error_email) else null
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        with(context) {
            hint = "Email"
            background = getDrawable(R.drawable.background_rounded)
        }
    }
}