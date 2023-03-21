package com.syncvr.fiboapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncvr.fiboapp.FiboApplication
import com.syncvr.fiboapp.database.entities.FiboRequest
import com.syncvr.fiboapp.databinding.FragmentFiboRequestsBinding
import com.syncvr.fiboapp.ui.adapters.FiboRequestsAdapter
import com.syncvr.fiboapp.viewmodels.FiboRequestsViewModel
import com.syncvr.fiboapp.viewmodels.FiboRequestsViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class FiboRequestsFragment : Fragment() {
    private var _binding: FragmentFiboRequestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FiboRequestsViewModel

    private val maxFiboNumberAllowed = 92

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiboRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = FiboRequestsViewModelFactory(
            context?.applicationContext as FiboApplication
        ).create(FiboRequestsViewModel::class.java)

        binding.btnRequest.setOnClickListener {
            btnRequestClicked()
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = FiboRequestsAdapter {
            val action = FiboRequestsFragmentDirections.actionFiboRequestsFragmentToFiboNumberAllRequestsFragment(
                it.fiboNumber
            )
            view.findNavController().navigate(action)
        }
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
                context?.toast(
                    "This fibo number $number has already" +
                            " been requested and calculated"
                )
                recyclerView.scrollToPosition((recyclerView.adapter as FiboRequestsAdapter).itemCount - 1)
                return
            }

            // limits of fibonacci numbers in long value is f(92), any request with larger value will be rejected
            if(number > maxFiboNumberAllowed) {
                context?.toast("Can't calculate f($number), limit is f(92) :-(")
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