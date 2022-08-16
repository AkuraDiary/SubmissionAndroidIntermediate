package com.asthiseta.submissionintermediate.myViews

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.asthiseta.submissionintermediate.R

class MyCustomEmail: AppCompatEditText {


    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(s!!).matches()) {
                    error = context.getString(R.string.txt_invalid_email)
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        height = 48
        hint = "Email"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }
}