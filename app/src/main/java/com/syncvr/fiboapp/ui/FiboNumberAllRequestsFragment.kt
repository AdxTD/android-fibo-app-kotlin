package com.syncvr.fiboapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syncvr.fiboapp.FiboApplication
import com.syncvr.fiboapp.R
import com.syncvr.fiboapp.databinding.FragmentFiboNumberAllRequestsBinding
import com.syncvr.fiboapp.databinding.FragmentFiboRequestsBinding
import com.syncvr.fiboapp.ui.adapters.FiboRequestsAdapter
import com.syncvr.fiboapp.viewmodels.FiboRequestsViewModel
import com.syncvr.fiboapp.viewmodels.FiboRequestsViewModelFactory
import kotlinx.coroutines.launch

class FiboNumberAllRequestsFragment : Fragment() {
    private var _binding: FragmentFiboNumberAllRequestsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FiboRequestsViewModel
    private lateinit var recyclerView: RecyclerView

    private var fiboNumberIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            fiboNumberIndex = it.getInt("fiboNumberIndex")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiboNumberAllRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvReadyHeader.text = getString(R.string.status_showing_requests_4number,fiboNumberIndex)
        viewModel = FiboRequestsViewModelFactory(
            context?.applicationContext as FiboApplication
        ).create(FiboRequestsViewModel::class.java)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = FiboRequestsAdapter{}
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.getAllRequestsForFiboNumber(fiboNumberIndex).collect() {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}