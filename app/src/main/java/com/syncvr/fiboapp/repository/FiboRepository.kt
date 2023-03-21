package com.syncvr.fiboapp.repository

import androidx.room.Index
import com.syncvr.fiboapp.database.FiboDatabase
import com.syncvr.fiboapp.database.entities.FiboNumber
import com.syncvr.fiboapp.database.entities.FiboRequest
import com.syncvr.fiboapp.database.entities.JoinedFiboRequestsNumbers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FiboRepository(private val database: FiboDatabase) {

    suspend fun getLastTwoFiboNumbers(): List<FiboNumber> {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().getLastTwoFiboNumbers()
        }
    }

    suspend fun getJoinedFiboRequestsNumbers(): Flow<List<JoinedFiboRequestsNumbers>> {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().getJoinedFiboRequestsNumbers()
        }
    }

    suspend fun getRequestByNumber(fiboNumber: Int): FiboRequest? {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().getRequestByNumber(fiboNumber)
        }
    }

    suspend fun addFiboRequest(request: FiboRequest) {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().addFiboRequest(request)
        }
    }

    suspend fun addFiboNumber(number: FiboNumber) {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().addFiboNumber(number)
        }
    }

    suspend fun addAllFiboNumbers(fiboNumbers: MutableList<FiboNumber>) {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().addAllFiboNumbers(fiboNumbers)
        }
    }

    suspend fun getAllRequestsForFiboNumber(index: Int): Flow<List<JoinedFiboRequestsNumbers>> {
        return withContext(Dispatchers.IO) {
            database.getFiboDao().getAllJoinedFiboRequestsNumbersForFiboNumber(index)
        }
    }


}