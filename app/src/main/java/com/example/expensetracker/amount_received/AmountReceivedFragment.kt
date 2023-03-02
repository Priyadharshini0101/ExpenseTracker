package com.example.expensetracker.amount_received

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
import com.example.expensetracker.amount_send.AmountSendFragment
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.databinding.FragmentAmountReceivedBinding
import com.example.expensetracker.transactions.TransactionActivity

class AmountReceivedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAmountReceivedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_amount_received,container,false)

        val db = DBHelper(this.requireActivity())

        val application= requireNotNull(this.activity).application
        val viewModelFactory = AmountReceivedViewModelFactory(db,application)
        val amountReceivedViewModel = ViewModelProvider(this, viewModelFactory).get(AmountReceivedViewModel::class.java)

        binding.amountReceivedViewModel=amountReceivedViewModel
        binding.lifecycleOwner = this

        val adapter= Adapter(UserClickListener { id ->
            amountReceivedViewModel.navigateToTransactionPage(id)
        })

        binding.recyclerView.adapter=adapter

        amountReceivedViewModel.data.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })

        amountReceivedViewModel.navigateToTransactionPage.observe(viewLifecycleOwner,Observer{
            if(it!=null){
                val intent = Intent(application, TransactionActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(AmountSendFragment.USER_KEY,it)
                startActivity(intent)

                amountReceivedViewModel.doneNavigateToTransactionPage()
            }
        })

        return binding.root
    }
}

