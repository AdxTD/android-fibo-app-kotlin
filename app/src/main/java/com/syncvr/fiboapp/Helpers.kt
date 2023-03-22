package com.syncvr.fiboapp

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.toast(msg : String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    .show()

const val loggingEnabled = false
fun log(msg : String) {
    if (loggingEnabled) Log.e("FIBO", msg)
}