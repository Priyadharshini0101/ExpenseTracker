package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.modal.Expense
import com.example.expensetracker.databinding.ActivityAddUsersBinding


class AddUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Add Users"
        val db = DBHelper(this)
        val binding: ActivityAddUsersBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_users)
        binding.addusers.setOnClickListener {
            if (binding.name.text!!.isNotEmpty()) {
                val user = Expense(0,binding.name.text.toString(),0,0,0)
                db.insertData(user)
                binding.name.text!!.clear()
                Toast.makeText(this, "User is added", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please Fill All Data's", Toast.LENGTH_SHORT).show()
            }
        }
    }
}