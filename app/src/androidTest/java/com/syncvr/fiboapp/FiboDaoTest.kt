package com.syncvr.fiboapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.syncvr.fiboapp.database.FiboDao
import com.syncvr.fiboapp.database.FiboDatabase
import com.syncvr.fiboapp.database.entities.FiboNumber
import com.syncvr.fiboapp.database.entities.FiboRequest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class FiboDaoTest {
    private lateinit var fiboDao: FiboDao
    private lateinit var db: FiboDatabase

    @Before
    fun createDb(){
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, FiboDatabase::class.java)
            .allowMainThreadQueries() // allowing main thread queries just for testing
            .build()

        fiboDao = db.getFiboDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFiboNumber() = runBlocking {
        val fiboNumber = FiboNumber(0,0)
        fiboDao.addFiboNumber(fiboNumber)
        val number = fiboDao.getAllFiboNumbers().first()
        assertEquals(number.fiboValue,fiboNumber.fiboValue)
    }

    @Test
    @Throws(Exception::class)
    fun insertAllFiboNumbers() = runBlocking {
        fiboDao.addAllFiboNumbers(listOf(
            FiboNumber(0,0),
            FiboNumber(1,1),
            FiboNumber(2,1)
        ))
        val numbers = fiboDao.getAllFiboNumbers()
        assertEquals(numbers.size,3)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetFiboRequest() = runBlocking {
        val fiboNumber = FiboNumber(1,1)
        fiboDao.addFiboNumber(fiboNumber)
        val fiboRequest = FiboRequest(1, Calendar.getInstance().time.toString())
        fiboDao.addFiboRequest(fiboRequest)
        val dbRequest = fiboDao.getRequestByNumber(fiboRequest.fiboNumber)
        assertEquals(dbRequest?.requestDate, fiboRequest.requestDate)
    }

//    @Test
//    @Throws(Exception::class)
//    fun testGetJoinedFiboRequestsNumbers() = runBlocking {
//        fiboDao.addAllFiboNumbers(listOf(
//            FiboNumber(0,0),
//            FiboNumber(1,1),
//            FiboNumber(2,1)
//        ))
//        fiboDao.addFiboRequest(FiboRequest(1, Calendar.getInstance().time.toString()))
//        fiboDao.addFiboRequest(FiboRequest(2, Calendar.getInstance().time.toString()))
//        val joined = fiboDao.getJoinedFiboRequestsNumbers()
//        assertEquals(joined.count(),2)
//    }
}