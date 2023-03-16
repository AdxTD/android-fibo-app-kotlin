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
            }
        }
    }

    private fun btnRequestClicked(){
        if(binding.etFibo.text!!.isEmpty())
            binding.etFibo.requestFocus()
        else {
            // TODO: check if this request already exist in adapter list
            // it is done in the viewModel now

            // limits of fibonacci numbers in long value is f(92), any request with larger value will be rejected
            val number = binding.etFibo.text.toString().toIntOrNull()
            if(number!! > 92) {
                toast("Can't calculate f($number), limit is f(92) :-(")
                return
            }

            // adding new request through the view model
            viewModel.addRequest(
                FiboRequest(
                    binding.etFibo.text.toString().toIntOrNull()!!,
                    Calendar.getInstance().time.toString()
                )
            )
            binding.etFibo.text?.clear()
        }
    }
}