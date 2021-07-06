package com.example.wordbuilder.utils

import android.content.Context
import android.icu.text.CompactDecimalFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*

fun View.hideKeyboard(){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


