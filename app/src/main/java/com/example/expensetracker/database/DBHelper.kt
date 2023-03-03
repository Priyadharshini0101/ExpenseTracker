package com.example.expensetracker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetracker.modal.Expense
import com.example.expensetracker.modal.Transaction
import java.lang.Math.abs


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY, " + USER_NAME + " TEXT," + AMOUNT_SENT + " INTEGER," +
                 AMOUNT_RECEIVED + " INTEGER," + REMAINING+ " INTEGER" + ")")
        val query1 = ("CREATE TABLE " + TABLE_NAME1 + " (" + ID + " INTEGER PRIMARY KEY, " + EXPENSE_ID + " INTEGER," + EXPENSE_USER_NAME + " TEXT," + AMOUNT + " INTEGER," +
                 NOTES + " TEXT," + TRANSACTIONS + " TEXT" + ")")
        db.execSQL(query)
        db.execSQL(query1)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
//       Do something
    }


    fun insertData(expense:Expense){
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(USER_NAME, expense.userName)
            values.put(AMOUNT_SENT, expense.amountSentByTheUser)
            values.put(AMOUNT_RECEIVED, expense.amountReceivedByTheUser)
            values.put(REMAINING, abs(expense.amountSentByTheUser - expense.amountReceivedByTheUser))
            db.insert(TABLE_NAME, null, values)

            db.close()
    }
    private var _sum_Send= MutableLiveData<Int?>()
    val sumSend: LiveData<Int?>
        get()=_sum_Send

    private var _sum_Receive= MutableLiveData<Int?>()
    val sumReceive: LiveData<Int?>
        get()=_sum_Receive

    fun amountToBeSend_Or_Received(sendOrReceive:String): MutableLiveData<List<Expense>>{
        val arrayList = ArrayList<Expense>()
        val list = MutableLiveData<List<Expense>>()
        val db = this.readableDatabase
        val result= db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        if(result.moveToFirst()){

            val send = "Send"
            var sum=0
            if(sendOrReceive == send) {

                do{

                if (result.getInt(result.getColumnIndex(AMOUNT_SENT)) <= result.getInt(result.getColumnIndex(AMOUNT_RECEIVED))) {
                    val expense = Expense(result.getInt(result.getColumnIndex(ID)),result.getString(result.getColumnIndex(USER_NAME)), result.getInt(result.getColumnIndex(AMOUNT_SENT)), result.getInt(result.getColumnIndex(AMOUNT_RECEIVED)), result.getInt(result.getColumnIndex(REMAINING)))
                    sum += result.getInt(result.getColumnIndex(REMAINING))
                    arrayList.add(expense)
                   }
                }while(result.moveToNext())
                _sum_Send.value = sum
            }else{

                do{
                 if (result.getInt(result.getColumnIndex(AMOUNT_SENT)) > result.getInt(result.getColumnIndex(AMOUNT_RECEIVED))) {
                        val expense = Expense(result.getInt(result.getColumnIndex(ID)),result.getString(result.getColumnIndex(USER_NAME)), result.getInt(result.getColumnIndex(AMOUNT_SENT)), result.getInt(result.getColumnIndex(AMOUNT_RECEIVED)), result.getInt(result.getColumnIndex(REMAINING)))
                     sum += result.getInt(result.getColumnIndex(REMAINING))
                     arrayList.add(expense)
                    }
                }while(result.moveToNext())
                _sum_Receive.value = sum
            }
        }
        list.postValue(arrayList)
        return list
    }




    fun insertDataTransaction(transaction:Transaction){
            val db = this.writableDatabase
            val values = ContentValues()

        values.put(EXPENSE_ID,transaction.expenseId)
           values.put(EXPENSE_USER_NAME, transaction.expenseUserName)
            values.put(AMOUNT, transaction.amount)
            values.put(NOTES, transaction.notes)
            values.put(TRANSACTIONS, transaction.transact)
            db.insert(TABLE_NAME1, null, values)
        val expense = getExpense(transaction.expenseId)
        updateValues(transaction,expense)

            db.close()
    }

    fun getExpense(id:Int): Expense {
        val db = this.readableDatabase
        val result= db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + "'" + id + "'", null)
        result.moveToFirst()
        return Expense(
            result.getInt(result.getColumnIndex(ID)),
            result.getString(result.getColumnIndex(USER_NAME)),
            result.getInt(result.getColumnIndex(AMOUNT_SENT)),
            result.getInt(result.getColumnIndex(AMOUNT_RECEIVED)),
            result.getInt(result.getColumnIndex(REMAINING))
        )
    }

    fun updateValues(transaction:Transaction, expense: Expense){
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(USER_NAME,expense.userName)


      var remaining = 0
        if(transaction.transact == "Send") {

            values.put(AMOUNT_SENT,expense.amountSentByTheUser + transaction.amount)
            values.put(AMOUNT_RECEIVED,expense.amountReceivedByTheUser)
            remaining =  abs((expense.amountSentByTheUser + transaction.amount) - expense.amountReceivedByTheUser)

        }else{

            values.put(AMOUNT_SENT,expense.amountSentByTheUser)
            values.put(AMOUNT_RECEIVED,expense.amountReceivedByTheUser + transaction.amount)
            remaining = abs(expense.amountSentByTheUser - (expense.amountReceivedByTheUser + transaction.amount))
        }
        values.put(REMAINING, remaining)

       db.update(TABLE_NAME,values, "id=" + "'" +transaction.expenseId + "'",null )
    }

    fun deleteRow(transaction: Transaction) {
        val db = this.writableDatabase

        val expense = getExpense(transaction.expenseId)
        updateDeleteValues(transaction,expense)
        db.delete(
            TABLE_NAME1,
            "id="+"'"+transaction.id+"'",null)

        db.close()
    }
    fun updateDeleteValues(transaction:Transaction, expense: Expense){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(USER_NAME,expense.userName)
        var remaining = 0
        if(transaction.transact == "Send") {
            remaining =  abs((expense.amountSentByTheUser - transaction.amount) - expense.amountReceivedByTheUser)
            values.put(AMOUNT_SENT,expense.amountSentByTheUser - transaction.amount)
            values.put(AMOUNT_RECEIVED,expense.amountReceivedByTheUser)

        }else{
            remaining = abs(expense.amountSentByTheUser - (expense.amountReceivedByTheUser - transaction.amount))
            values.put(AMOUNT_SENT,expense.amountSentByTheUser)
            values.put(AMOUNT_RECEIVED,expense.amountReceivedByTheUser - transaction.amount)
        }
        values.put(REMAINING, remaining)
        db.update(TABLE_NAME,values, "id=" + "'" +transaction.expenseId + "'",null )
    }

    fun readTransactionData(str:String): MutableLiveData<List<Transaction>>{
        val arrayList = ArrayList<Transaction>()
        val list = MutableLiveData<List<Transaction>>()
        val db = this.readableDatabase
        val result= db.rawQuery("SELECT * FROM " + TABLE_NAME1 + " WHERE " + EXPENSE_USER_NAME  + " = " + "'" + str + "'", null)
        if(result.moveToFirst()){

            do{
                val transaction = Transaction(result.getInt(result.getColumnIndex(ID)),result.getInt(result.getColumnIndex(EXPENSE_ID)),result.getString(result.getColumnIndex(EXPENSE_USER_NAME)), result.getInt(result.getColumnIndex(AMOUNT)), result.getString(result.getColumnIndex(NOTES)), result.getString(result.getColumnIndex(TRANSACTIONS)))
                arrayList.add(transaction)
            }
            while(result.moveToNext())
        }
        list.postValue(arrayList)
        return list
    }

//    fun readData(): MutableLiveData<List<Expense>>{
//        val arrayList = ArrayList<Expense>()
//        val list = MutableLiveData<List<Expense>>()
//        val db = this.readableDatabase
//        val result= db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
//        if(result.moveToFirst()){
//            do{
//                val expense = Expense(result.getString(result.getColumnIndex(USER_NAME)),
//                    result.getInt(result.getColumnIndex(AMOUNT_SENT)),
//                    result.getInt(result.getColumnIndex(AMOUNT_RECEIVED)),
//                    result.getInt(result.getColumnIndex(REMAINING)))
//                arrayList.add(expense)
//            }
//            while(result.moveToNext())
//        }
//        list.postValue(arrayList)
//        return list
//    }

    companion object{
        private val DATABASE_NAME = "STUDENT DB"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "STUDENT_TABLE"
        val TABLE_NAME1 = "TRANSACTIONS_HISTORY"
        val ID = "id"
        val USER_NAME = "user_name"
        val AMOUNT_SENT = "amount_send"
        val AMOUNT_RECEIVED = "amount_received"
        val REMAINING = "remaining"
        val EXPENSE_USER_NAME = "expense_user_name"
        val EXPENSE_ID = "expense_id"
        val AMOUNT = "amount"
        val NOTES = "notes"
        val TRANSACTIONS = "transactions"
    }
}