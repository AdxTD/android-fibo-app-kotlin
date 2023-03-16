package com.syncvr.fiboapp

import android.app.Application
import com.syncvr.fiboapp.database.FiboDatabase

class FiboApplication : Application() {
    val database: FiboDatabase by lazy {
        FiboDatabase(this)
    }
}