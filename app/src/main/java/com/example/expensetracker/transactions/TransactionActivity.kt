package com.example.expensetracker.transactions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.*
import com.example.expensetracker.amount_send.AmountSendFragment
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.databinding.ActivityTransactionsBinding
import com.example.expensetracker.modal.Expense

class TransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val expense: Expense?
        val db = DBHelper(this)
        expense = intent.getParcelableExtra<Expense>(AmountSendFragment.USER_KEY)
        title = "${expense?.userName}"
        val binding: ActivityTransactionsBinding = DataBindingUtil.setContentView(this, R.layout.activity_transactions)
        val activity = this
        val application= requireNotNull(this).application
        val viewModelFactory = TransactionViewModelFactory(expense!!.userName, activity,db,application)
        val transactionViewModel= ViewModelProvider(this, viewModelFactory).get(TransactionViewModel::class.java)

        binding.transactionViewModel=transactionViewModel
        binding.lifecycleOwner = this

        val adapter= AdapterTransaction(
            MessageClickListener { id ->
                transactionViewModel.navigateToDeleteMessage(id)
            })

        binding.recyclerView.adapter=adapter

        transactionViewModel.data.observe(this, Observer {
            if(it.isNotEmpty()) {
              adapter.submitList(it)
              adapter.notifyDataSetChanged()
            }
        })

       binding.addTransactions.setOnClickListener{
            if(it!=null){
                val intent = Intent(application,AddTransactionActivity::class.java)
                intent.putExtra(AmountSendFragment.USER_KEY, expense)
                startActivity(intent)
                finish()
            }
        }


        transactionViewModel.navigateToDeleteMessage.observe(this,Observer{
            if(it!=null){
                val intent = Intent(application,TransactionActivity::class.java)
                intent.putExtra(AmountSendFragment.USER_KEY, expense)
                startActivity(intent)
                finish()
                transactionViewModel.doneNavigateToDeleteMessage()
            }
        })
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(application,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}