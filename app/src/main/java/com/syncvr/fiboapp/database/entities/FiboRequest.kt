package com.syncvr.fiboapp.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.syncvr.fiboapp.database.entities.FiboNumber

@Entity(tableName = "fibo_request", foreignKeys = [ForeignKey(
        entity = FiboNumber::class,
        childColumns = ["fibo_number"],
        parentColumns = ["id"]
    )])
data class FiboRequest (
    @NonNull @ColumnInfo(name = "fibo_number", index = true) val fiboNumber: Int,
    @NonNull @ColumnInfo(name = "date") val requestDate: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
