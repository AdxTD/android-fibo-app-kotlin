package com.syncvr.fiboapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncvr.fiboapp.FiboApplication
import com.syncvr.fiboapp.database.entities.FiboRequest
import com.syncvr.fiboapp.databinding.ActivityFiboRequestsBinding
import com.syncvr.fiboapp.ui.adapters.FiboRequestsAdapter
import com.syncvr.fiboapp.viewmodels.FiboRequestsViewModel
import com.syncvr.fiboapp.viewmodels.FiboRequestsViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

private lateinit var  binding: ActivityFiboRequestsBinding

private lateinit var recyclerView: RecyclerView

private lateinit var viewModel: FiboRequestsViewModel

private const val maxFiboNumberAllowed = 92

class FiboRequestsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiboRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = FiboRequestsViewModelFactory(
            this.application as FiboApplication
        ).create(FiboRequestsViewModel::class.java)

        binding.btnRequest.setOnClickListener {
            btnRequestClicked()
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = FiboRequestsAdapter()
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.getHistory().collect() {
                adapter.submitList(it)
                //recyclerView.scrollToPosition(it.size - 1)
            }
        }
    }

    private fun btnRequestClicked(){
        if(binding.etFibo.text!!.isEmpty())
            binding.etFibo.requestFocus()
        else {
            val number = binding.etFibo.text.toString().toIntOrNull()
            // check if this request already exist in adapter list: done
            // it was done in the viewModel in previous versions
            val itemExist = (recyclerView.adapter as FiboRequestsAdapter).checkItemExistence(number!!)
            if (itemExist){
                toast(
                    "This fibo number $number has already" +
                            " been requested and calculated"
                )
                recyclerView.scrollToPosition((recyclerView.adapter as FiboRequestsAdapter).itemCount - 1)
                return
            }

            // limits of fibonacci numbers in long value is f(92), any request with larger value will be rejected
            if(number > maxFiboNumberAllowed) {
                toast("Can't calculate f($number), limit is f(92) :-(")
                return
            }

            // adding new request through the view model
            viewModel.addRequest(
                FiboRequest(
                    number,
                    Calendar.getInstance().time.toString()
                )
            )
            binding.etFibo.text?.clear()
        }
    }
}