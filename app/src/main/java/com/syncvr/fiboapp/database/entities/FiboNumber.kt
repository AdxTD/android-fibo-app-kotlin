package com.syncvr.fiboapp.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fibo_number")
data class FiboNumber(
    @PrimaryKey @ColumnInfo(name = "id") val fiboNumber: Int,
    @NonNull @ColumnInfo(name = "value") val fiboValue : Long
)