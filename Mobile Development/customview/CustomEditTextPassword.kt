package com.example.storyapp.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.example.storyapp.R

class CustomEditTextPassword : AppCompatEditText {

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
        addTextChangedListener(onTextChanged = { password, _, _, _ ->
            error =
                if (password!!.length in 1..7) context.getString(R.string.error_password) else null
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.apply {
            hint = "Password"
            background = getDrawable(R.drawable.background_rounded)
        }
    }
}