package com.adsum.camel_masterapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.fragment.RaceDetailFragment
import com.adsum.camel_masterapplication.databinding.ActivityAdminDashboardBinding


import com.adsum.camel_masterapplication.fragment.FragmentCamel

class AdminDashboardActivity : AppCompatActivity() {
    // private lateinit var adminDashboardBinding:AdminDashboardActivity
    private lateinit var fragment: Fragment
    private lateinit var binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_admin_dashboard)
        val binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.titleImage.setOnClickListener{
            //onBackPressed()
            val intent=Intent(this,AdminDashboardActivity::class.java)
            startActivity(intent)
        }


        binding.tvUserDetails.setOnClickListener {
            binding.tvTitlePage.setText("User Detail")
        }

        binding.tvCamelDetails.setOnClickListener {
            binding.tvTitlePage.setText("Camel Detail")
            fragment = FragmentCamel()
//            val textview = findViewById(R.id.tv_titlePage) as TextView
//            textview.setText("Camel profile")

            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.commit()


        }
        binding.tvRaceDetails.setOnClickListener {
            binding.imageAdd.visibility = View.VISIBLE
            binding.tvTitlePage.setText("Race Detail")
            fragment= RaceDetailFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.commit()

        }
        binding.tvRaceSchedule.setOnClickListener {
            binding.tvTitlePage.setText("Race Schedule")

        }
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        //return super.onOptionsItemSelected(item)
//        return when(item.itemId){
//            R.id.home -> {
//                this.finish()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}