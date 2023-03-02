package com.example.expensetracker.amount_received

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.amount_send.AmountSendViewModel
import com.example.expensetracker.database.DBHelper

class AmountReceivedViewModelFactory(private val db: DBHelper, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmountReceivedViewModel::class.java)) {
            return AmountReceivedViewModel(db,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
