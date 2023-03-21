package com.syncvr.fiboapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.syncvr.fiboapp.database.entities.FiboRequest
import com.syncvr.fiboapp.database.entities.JoinedFiboRequestsNumbers
import com.syncvr.fiboapp.databinding.ItemFiboRequestBinding
import java.text.SimpleDateFormat
import java.util.*

class FiboRequestsAdapter(private val onItemClicked: (JoinedFiboRequestsNumbers) -> Unit) : ListAdapter<JoinedFiboRequestsNumbers, FiboRequestsAdapter.FiboRequestViewHolder>(DiffCallback) {

    class FiboRequestViewHolder (private var binding: ItemFiboRequestBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(fiboRequest: JoinedFiboRequestsNumbers) {
            val fiboNumText = "f(${fiboRequest.fiboNumber})= ${fiboRequest.fiboValue}"
            binding.tvFiboNumber.text = fiboNumText
            binding.tvRequestDate.text = fiboRequest.requestDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiboRequestViewHolder {
        val viewHolder =  FiboRequestViewHolder(
            ItemFiboRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FiboRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<JoinedFiboRequestsNumbers>() {
            override fun areItemsTheSame(oldItem: JoinedFiboRequestsNumbers, newItem: JoinedFiboRequestsNumbers): Boolean {
                return oldItem.fiboNumber == newItem.fiboNumber
            }

            override fun areContentsTheSame(oldItem: JoinedFiboRequestsNumbers, newItem: JoinedFiboRequestsNumbers): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun checkItemExistence(number :Int) :Boolean {
        val fiboNumber = this.currentList.find { it.fiboNumber == number }
        return fiboNumber != null
    }

}
