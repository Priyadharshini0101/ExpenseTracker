package com.example.expensetracker.amount_send

import android.app.Activity
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.modal.Expense

class AmountSendViewModelFactory(private val db:DBHelper, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AmountSendViewModel::class.java)) {
            return AmountSendViewModel(db,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
