package com.example.expensetracker.amount_send

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.Adapter
import com.example.expensetracker.R
import com.example.expensetracker.UserClickListener
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.databinding.FragmentAmountSentBinding
import com.example.expensetracker.transactions.TransactionActivity

class AmountSendFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentAmountSentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_amount_sent,container,false)

        val db = DBHelper(this.requireActivity())

        val application= requireNotNull(this.activity).application
        val viewModelFactory = AmountSendViewModelFactory(db,application)
        val amountSendViewModel = ViewModelProvider(this, viewModelFactory).get(AmountSendViewModel::class.java)

        binding.amountSendViewModel=amountSendViewModel
        binding.lifecycleOwner = this

        val adapter= Adapter(UserClickListener { id ->
            amountSendViewModel.navigateToTransactionPage(id)
        })

        binding.recyclerView.adapter=adapter

            amountSendViewModel.data.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        amountSendViewModel.navigateToTransactionPage.observe(viewLifecycleOwner,Observer{
          if(it!=null){
              val intent = Intent(application,TransactionActivity::class.java)
              intent.flags =
                  Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
              intent.putExtra(USER_KEY, it)
              startActivity(intent)
              amountSendViewModel.doneNavigateToTransactionPage()
          }
        })
        return binding.root
    }

    companion object {
        val USER_KEY = "TransactionPage"
    }
}

