package com.syncvr.fiboapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.syncvr.fiboapp.database.entities.FiboNumber
import com.syncvr.fiboapp.database.entities.FiboRequest
import com.syncvr.fiboapp.database.entities.JoinedFiboRequestsNumbers
import kotlinx.coroutines.flow.Flow

@Dao
interface FiboDao {


    @Query("SELECT fibo_number.id AS fiboNumber, fibo_number.value AS fiboValue, fibo_request.date AS requestDate "+
            "FROM fibo_request INNER JOIN fibo_number ON fibo_number.id=fibo_request.fibo_number ORDER BY fibo_request.id ASC"
    )
    fun getJoinedFiboRequestsNumbers(): Flow<List<JoinedFiboRequestsNumbers>>

    @Insert
    suspend fun addFiboNumber(fiboNumber: FiboNumber)

    @Insert
    suspend fun addAllFiboNumbers(fiboNumbers: List<FiboNumber>)

    @Insert
    suspend fun addFiboRequest(request: FiboRequest)

    @Query("SELECT * FROM fibo_number ORDER BY id DESC LIMIT 0, 1")
    suspend fun getLastTwoFiboNumbers(): List<FiboNumber>

    @Query("SELECT * FROM fibo_request WHERE fibo_number = :number ORDER BY id DESC LIMIT 1")
    suspend fun getRequestByNumber(number: Int): FiboRequest?

    // following methods are just for testing -
    // check FiboDaoUnitTest in androidTest folder for more information
    @Query("SELECT * FROM fibo_number")
    suspend fun getAllFiboNumbers(): List<FiboNumber>

    @Query("DELETE FROM fibo_request")
    suspend fun deleteAllRequests()

    @Query("DELETE FROM fibo_number")
    suspend fun deleteAllNumbers()


}