package com.example.expensetracker.amount_received

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.modal.Expense

class AmountReceivedViewModel(database: DBHelper, application: Application): AndroidViewModel(application) {
    private var _navigateToTransactionPage= MutableLiveData<Expense?>()
    val navigateToTransactionPage: LiveData<Expense?>
        get()=_navigateToTransactionPage

    var data=database.amountToBeSend_Or_Received("Receive")

    fun navigateToTransactionPage(data:Expense){
        _navigateToTransactionPage.value=data
    }

    fun doneNavigateToTransactionPage(){
        _navigateToTransactionPage.value=null
    }
}