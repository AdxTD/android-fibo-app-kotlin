package com.syncvr.fiboapp.database.entities

data class JoinedFiboRequestsNumbers(
    val fiboNumber: Int,
    val fiboValue : Long,
    val requestDate: String
) {
}