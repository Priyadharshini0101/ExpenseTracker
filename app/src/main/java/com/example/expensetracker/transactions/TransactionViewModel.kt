package com.example.expensetracker.transactions

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.modal.Expense
import com.example.expensetracker.modal.Transaction

class TransactionViewModel(str:String,activity: Activity,database: DBHelper, application: Application):AndroidViewModel(application){
    private var alert: AlertDialog.Builder
    val db =database
    private var _navigateToDeleteMessage= MutableLiveData<String?>()
    val navigateToDeleteMessage: LiveData<String?>
        get()=_navigateToDeleteMessage


    var data=db.readTransactionData(str)
    init {

        alert = AlertDialog.Builder(activity)


    }

    fun navigateToDeleteMessage(data: Transaction){
        alert.setMessage("Are you sure you want to Delete transaction?")
            .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                db.deleteRow(data)
                _navigateToDeleteMessage.value=data.id.toString()
            }).setNegativeButton("NO", null)

        val alert: AlertDialog = alert.create()
        alert.show()

    }

    fun doneNavigateToDeleteMessage(){
        _navigateToDeleteMessage.value=null
    }
}