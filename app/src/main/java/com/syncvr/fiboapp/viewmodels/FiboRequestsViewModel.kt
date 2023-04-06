package com.syncvr.fiboapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syncvr.fiboapp.FiboApplication
import com.syncvr.fiboapp.database.entities.FiboNumber
import com.syncvr.fiboapp.database.entities.FiboRequest
import com.syncvr.fiboapp.database.entities.JoinedFiboRequestsNumbers
import com.syncvr.fiboapp.repository.FiboRepository
import com.syncvr.fiboapp.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FiboRequestsViewModel(application: FiboApplication) : ViewModel() {

    private val fiboRepository = FiboRepository(application.database)

    private var lastCalculated = 1
    private var lastFiboValue: Long = 1
    private var lastMinusOneFiboValue: Long = 0;

    init {
        log("FiboRequestsViewModel init")
        viewModelScope.launch {

            val fiboNumbers = fiboRepository.getLastTwoFiboNumbers() // returned in descending order
            if (fiboNumbers.isEmpty()) { // means we don't have any values in fibo_number table
                // so we should add first two initial values: f(0) = 0, f(1) = 1
                addFiboNumber(FiboNumber(0, 0))
                addFiboNumber(FiboNumber(1, 1))
                log("FiboRequestsViewModel fiboNumbers.isEmpty() - ADDED 0+1")
            } else { // i.e. there at least two values in the fibo_number table
                log("FiboRequestsViewModel fiboNumbers NOT Empty")
                fiboNumbers.let {
                    lastCalculated = it.first().fiboNumber // the id, i.e. last fibo number requested (or 1 = f(1) in case the db is empty)
                    lastFiboValue = it.first().fiboValue // the large one, f(n)
                    lastMinusOneFiboValue = it.last().fiboValue // the small one, f(n-1)
                    log(
                        "FiboRequestsViewModel fiboNumbers NOT Empty - lastCalculated= $lastCalculated"
                                + " - lastFiboValue=$lastFiboValue - lastMinusOneFiboValue=$lastMinusOneFiboValue"
                    )
                }
            }
        }
    }

    suspend fun getHistory(): Flow<List<JoinedFiboRequestsNumbers>> {
        return fiboRepository.getJoinedFiboRequestsNumbers()
    }

    suspend fun getAllRequestsForFiboNumber(index:Int): Flow<List<JoinedFiboRequestsNumbers>>{
        return fiboRepository.getAllRequestsForFiboNumber(index)
    }

    fun addRequest(request: FiboRequest) = viewModelScope.launch {
        if (request.fiboNumber <= lastCalculated) { // means fibo number had already been calculated
            log("addRequest fiboNumber <= lastCalculated")
            fiboRepository.addFiboRequest(request)
        } else {
            log("addRequest fiboRequest > lastCalculated")
            // a list to save all the calculated fibo numbers in, so we can insert them at the end into db
            val fiboNumbers = mutableListOf<FiboNumber>()
            while (lastCalculated < request.fiboNumber) { // keep calculating fibos until we reach the requested one
                val newFiboValue = lastFiboValue + lastMinusOneFiboValue // f(n) = f(n-1) + f(n-2)
                lastMinusOneFiboValue = lastFiboValue // now f(n-1) becomes f(n-2) for next iteration
                lastFiboValue = newFiboValue // and f(n) becomes f(n-1) for next iteration
                // we increase the calculated number here and then we save to the list
                fiboNumbers.add(FiboNumber(++lastCalculated, newFiboValue))
            }
            log("addRequest lastCalculated = $lastCalculated")
            log("addRequest fiboNumbers.size = ${fiboNumbers.size}")
            // inserting all the fibo numbers in to database in one transaction (more efficient)
            fiboRepository.addAllFiboNumbers(fiboNumbers)
            // finally, inserting the request only after the respective fibo number was calculated
            fiboRepository.addFiboRequest(request)
            log("addRequest all added, date = ${request.requestDate}")
            //true;
        }
    }


    private suspend fun addFiboNumber(number: FiboNumber) = fiboRepository.addFiboNumber(number)


}