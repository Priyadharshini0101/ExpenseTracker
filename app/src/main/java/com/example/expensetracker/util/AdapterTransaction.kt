package com.example.expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ListItemsReceivedBinding
import com.example.expensetracker.databinding.ListItemsSentBinding
import com.example.expensetracker.modal.Expense
import com.example.expensetracker.modal.Transaction
import com.example.expensetracker.transactions.TransactionViewModel


class AdapterTransaction (val clickListener1: MessageClickListener) : ListAdapter<Transaction, RecyclerView.ViewHolder>(MessageDiffCallback()) {

    private val holderTypeMessageReceived = 1
    private val holderTypeMessageSent = 2
    val send = "Send"

    class ReceivedViewHolder(private val binding: ListItemsReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction,clickListener1: MessageClickListener) {
           binding.data1 = item
            binding.messageClickListener=clickListener1
            binding.executePendingBindings()
        }
    }

    class SentViewHolder(private val binding: ListItemsSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction,clickListener1: MessageClickListener ) {
            binding.data = item
            binding.messageClickListener=clickListener1
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).transact != send) {
            holderTypeMessageReceived
        } else {
            holderTypeMessageSent
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            holderTypeMessageSent -> (holder as SentViewHolder).bind(
                getItem(position),clickListener1
            )
            holderTypeMessageReceived -> (holder as ReceivedViewHolder).bind(

                getItem(position),clickListener1
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            holderTypeMessageSent -> {
                val binding = ListItemsSentBinding.inflate(layoutInflater, parent, false)
                SentViewHolder(binding)
            }
            holderTypeMessageReceived -> {
                val binding = ListItemsReceivedBinding.inflate(layoutInflater, parent, false)
                ReceivedViewHolder(binding)
            }
            else -> {
                throw Exception("Error reading holder type")
            }
        }
    }
}

class MessageClickListener(val clickListener: (transition:Transaction) -> Unit){
    fun onClick(transition: Transaction)=clickListener(transition)
}
class MessageDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }
}