package com.example.expensetracker.modal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(
    var id:Int = 0,
    var userName:String="",
    var amountSentByTheUser:Int=0,
    var amountReceivedByTheUser:Int=0,
    var remaining:Int=0,
):Parcelable

data class Transaction(
    var id: Int= 0,
    var expenseId:Int = 0,
    var expenseUserName:String = "",
    var amount:Int=0,
    var notes:String="",
    var transact:String="",
)



