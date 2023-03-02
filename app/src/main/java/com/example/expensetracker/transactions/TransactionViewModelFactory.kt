package com.example.expensetracker.transactions

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.amount_send.AmountSendViewModel
import com.example.expensetracker.database.DBHelper

class TransactionViewModelFactory(private val str:String,private val activity: Activity,private val db: DBHelper, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(str,activity,db,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
