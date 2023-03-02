package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.expensetracker.amount_send.AmountSendFragment
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.databinding.ActivityAddTransactionsBinding
import com.example.expensetracker.modal.Expense
import com.example.expensetracker.modal.Transaction
import com.example.expensetracker.transactions.TransactionActivity

class AddTransactionActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Add Transactions"
        val expense: Expense?
        val db = DBHelper(this)
        val binding: ActivityAddTransactionsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_transactions)
        expense = intent.getParcelableExtra<Expense>(AmountSendFragment.USER_KEY)
        binding.addTransactions.setOnClickListener {
            if (binding.inputAmount.text!!.isNotEmpty() && binding.inputDetails.text!!.isNotEmpty() && binding.radioGroup.checkedRadioButtonId!=0) {
                val intSelectButton: Int = binding.radioGroup.checkedRadioButtonId
                val radioButton = findViewById<RadioButton>(intSelectButton)
                val inputTransaction = Transaction(0,expense!!.id,expense.userName, Integer.valueOf(binding.inputAmount.text.toString()), binding.inputDetails.text.toString(), radioButton.text.toString())
                db.insertDataTransaction(inputTransaction)
                binding.inputAmount.text!!.clear()
                binding.inputDetails.text!!.clear()
                Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TransactionActivity::class.java)
                intent.putExtra(AmountSendFragment.USER_KEY, expense)
                startActivity(intent)
                finish()
                } else {
                    Toast.makeText(this, "Please Fill All Data's", Toast.LENGTH_SHORT).show()
                }
        }
    }
}