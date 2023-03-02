package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.expensetracker.amount_received.AmountReceivedFragment
import com.example.expensetracker.amount_send.AmountSendFragment
import com.example.expensetracker.database.DBHelper
import com.example.expensetracker.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val db = DBHelper(this)
        val tabLayout = binding.tablayout
        val viewPager = binding.viewpager
        viewPager.adapter = FragmentAdapter(this)
      db.amountToBeSend_Or_Received("Send")
        db.amountToBeSend_Or_Received("Receive")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
           when(position){
               0 -> tab.text = "Amount to sent \n ₹ ${db.sumSend.value}"
               1 -> tab.text = "Amount to received \n ₹ ${db.sumReceive.value}"
           }
        }.attach()

        binding.addusers.setOnClickListener {
            val intent = Intent(this, AddUsersActivity::class.java)
            startActivity(intent)
        }
    }

    class FragmentAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AmountSendFragment()
                1 -> AmountReceivedFragment()
                else -> AmountSendFragment()
            }
        }
    }
}
