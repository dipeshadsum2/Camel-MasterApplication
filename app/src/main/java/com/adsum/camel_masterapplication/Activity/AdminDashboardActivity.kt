package com.adsum.camel_masterapplication.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityAdminDashboardBinding
import com.adsum.camel_masterapplication.fragment.*

class AdminDashboardActivity : AppCompatActivity() {
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
        binding.tvBack.setOnClickListener {
           finish()
        }

        binding.tvUserDetails.setOnClickListener {
            binding.imageAdduser.visibility = View.VISIBLE
            binding.tvTitlePage.setText(getString(R.string.user_detail))
            fragment= FragmentUserDetails()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.imageAdduser.setOnClickListener{
            binding.imageAdduser.visibility = View.GONE
            binding.imageAdd.visibility = View.GONE
            binding.addImageSchedule.visibility = View.GONE
            binding.tvTitlePage.setText(getString(R.string.user_detail))
            fragment = AddUserFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }

        binding.tvCamelDetails.setOnClickListener {
            binding.tvTitlePage.setText(getString(R.string.camel_detail))
            binding.addImageSchedule.visibility = View.GONE
            fragment = FragmentCamel()
//            val textview = findViewById(R.id.tv_titlePage) as TextView
//            textview.setText("Camel profile")
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.tvRaceDetails.setOnClickListener {
            binding.imageAdd.visibility = View.VISIBLE
            binding.addImageSchedule.visibility = View.GONE
            binding.tvTitlePage.setText(getString(R.string.race_detail))
            fragment= RaceDetailFragment()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.imageAdd.setOnClickListener {
            binding.imageAdd.visibility = View.GONE
            fragment = FragmentAddRaceDetail()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(null)
            ft.commit()
        }
        binding.tvRaceSchedule.setOnClickListener {
            binding.tvTitlePage.setText(getString(R.string.race_schedule))
            binding.addImageSchedule.visibility = View.VISIBLE
            fragment = FragmentAdminRaceSchedule()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.replace(R.id.frameLayout, fragment)
            ft.commit()
        }
        binding.addImageSchedule.setOnClickListener {
            binding.tvTitlePage.setText(getString(R.string.add_schedule))
            binding.addImageSchedule.visibility = View.GONE
            fragment = FragmentAdminImageAdd()
            val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.replace(R.id.fram, fragment)
            ft.commit()
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