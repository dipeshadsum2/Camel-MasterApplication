package com.adsum.camel_masterapplication.Activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.LogoutResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.fragment.*
import com.adsum.camel_masterapplication.fragment.FragmentHome.Companion.newInstance

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class DashboardActivity : AppCompatActivity() {

    private var timer: Timer? = null
    private var content: FrameLayout? = null
    private var logintime : String = ""
    private var user_id by Delegates.notNull<String>()
    private val mOnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_home -> {

                        val fragment = FragmentHome()
                        addFragment(fragment)
                        val textview = findViewById(R.id.title_page) as TextView
                        textview.setText(R.string.title_home)
                        val imageView = findViewById(R.id.title_image) as ImageView
                        imageView.setImageResource(R.drawable.ic_home_black_24dp)
                        return true
                    }
                    R.id.navigation_camel -> {
                        val fragment = FragmentCamel()
                       // addFragment(fragment)
                        val textview = findViewById(R.id.title_page) as TextView
                        textview.setText(R.string.title_camel)
                        val imageView = findViewById(R.id.title_image) as ImageView
                        imageView.setImageResource(R.drawable.ic_camel_black_24dp)
                        return true
                    }
                    R.id.navigation_race -> {
                        val fragment = FragmentRace()
                        addFragment(fragment)
                        val textview = findViewById(R.id.title_page) as TextView
                        textview.setText(R.string.title_race)
                        val imageView = findViewById(R.id.title_image) as ImageView
                        imageView.setImageResource(R.drawable.ic_race_black_24dp)
                        return true
                    }
                    R.id.navigation_history -> {
                        val fragment = FragmentHistory()
                        addFragment(fragment)
                        val textview = findViewById(R.id.title_page) as TextView
                        textview.setText(R.string.title_history)
                        val imageView = findViewById(R.id.title_image) as ImageView
                        imageView.setImageResource(R.drawable.ic_history_black_24dp)
                        return true
                    }
                    R.id.profile -> {
                        val fragment = FragmentProfile()
                       // addFragment(fragment)
                        val textview = findViewById(R.id.title_page) as TextView
                        textview.setText(R.string.title_profile)
                        val imageView = findViewById(R.id.title_image) as ImageView
                        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
                        return true
                    }
                }
                return false
            }

        }

    /**
     * add/replace fragment in container [framelayout]
     */
     fun FragmentCamel(){

        //val fragment = FragmentCamel()
        openFragment(FragmentCamel.newInstance("", ""), "FragmentCamel")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_camel)
        val imageView = findViewById(R.id.title_image) as ImageView
        imageView.setImageResource(R.drawable.ic_camel_black_24dp)
        return

    }
     fun FragmentProfile(){
        // val fragment = FragmentProfile()
        //addFragment(fragment)
        openFragment(FragmentProfile.newInstance("", ""), "FragmentProfile")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        val imageView = findViewById(R.id.title_image) as ImageView
        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        return

    }
     fun FragmentNotification(){
        // val fragment = FragmentProfile()
        //addFragment(fragment)
        openFragment(NotificationFragment.newInstance("", ""), "FragmentNotification")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        val imageView = findViewById(R.id.title_image) as ImageView
        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        return
    }
     fun FragmentParticipant(){
        // val fragment = FragmentProfile()
        //addFragment(fragment)
        openFragment( FragmentCategory.newInstance("", ""), " FragmentCategory")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        val imageView = findViewById(R.id.title_image) as ImageView
        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        return
    }
    public fun FragmentControlPanel(){
        // val fragment = FragmentProfile()
        //addFragment(fragment)
       // openFragment(AdminDashboardActivity.newInstance("", ""), "ControlPanelFragment")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_profile)
        val imageView = findViewById(R.id.title_image) as ImageView
        imageView.setImageResource(R.drawable.ic_baseline_account_circle_24)
        return
    }

    fun fragmentRaceSchedule() {
        openFragment(FragmentRaceSchedule.newInstance("", ""), "FragmentRaceSchedule")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_schedule)
        return
    }
    fun fragmentRaceArchive(){
        openFragment(FragmentArchive.newInstance("",""),"FragmentHistory")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_archive)
        return
    }
    fun fragmentTermsCondition(){
        openFragment(FragmentTermAndCondition.newInstance("",""),"FragmentTermAndCondition")
        val textview = findViewById(R.id.title_page) as TextView
        textview.setText(R.string.title_archive)
        return
    }



    fun openFragment(fragment: Fragment?, name: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framee, fragment!!)
        transaction.addToBackStack(name)
        transaction.commit()
    }
















    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.design_bottom_sheet_slide_in,
                R.anim.design_bottom_sheet_slide_out
            )
            .replace(R.id.framee, fragment, fragment.javaClass.getSimpleName())
            .addToBackStack(fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        try {

            if (CommonFunctions.checkConnection(this)) {

                var url: String = CamelConfig.WEBURL + CamelConfig.logout
                val mParams: HashMap<String, String> = HashMap()
                CommonFunctions.createProgressBar(this, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(this))
                    .build()

                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
//                    .addPathParameter(mParams)
                    .addBodyParameter(Constants.user_id, user_id)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val (msg, status) = gson.fromJson(
                                response.toString(),
                                LogoutResponse::class.java
                            )
                            if (status == "1") {
                                CommonFunctions.showToast(this@DashboardActivity, msg)
                                CommonFunctions.setPreference(
                                    this@DashboardActivity,
                                    Constants.isLogin,
                                    false
                                )
                                LoginActivity.startActivity(this@DashboardActivity)

                            } else {
                                CommonFunctions.showToast(this@DashboardActivity, msg)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(this@DashboardActivity, anError.toString())
                        }
                    })

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar?.hide()

        if(CommonFunctions.getPreference(this, Constants.isLogin, false)){
            logintime=CommonFunctions.getPreference(this, Constants.logintime, "").toString()
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logintime)
            var currenttime=System.currentTimeMillis()/10000
            Log.e("San",currenttime.toString()+ ",,,"+date.time/10000);
            var usetime=currenttime-date.time
            var mins= usetime / (1000 * 60) % 60
            if(mins>=1){
                logout()
            }
        }
        timerstart()

        content = findViewById(R.id.framee) as FrameLayout
        val navigation = findViewById(R.id.bottomnavigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
        user_id = CommonFunctions.getPreference(this, Constants.ID, "").toString()
        val imageButton = findViewById(R.id.logout) as ImageButton
        imageButton.setOnClickListener(View.OnClickListener {
            logout()
        })
        val fragment = FragmentRace()
        addFragment(fragment)
    }

    companion object {
        fun startActivity(activity: Activity) {
            val intent = Intent(
                activity,
                DashboardActivity::class.java
            )
            activity.startActivity(intent)
        }
    }

    /* override fun onPause() {
         super.onPause()
         timer = Timer()
         Log.i("Main", "Invoking logout timer")
         val logoutTimeTask = LogOutTimerTask()
         timer!!.schedule(logoutTimeTask, 2000) //auto logout in 5 minutes
     }


     override fun onResume() {
         super.onResume()
         if (timer != null) {
             timer!!.cancel()
             Log.i("Main", "cancel timer")
             timer = null
         }
     }

     inner class LogOutTimerTask : TimerTask() {
         override fun run() {

             //redirect user to login screen
             logout()
             CommonFunctions
                     .changeactivity(this@DashboardActivity, LoginActivity::class.java)

             finish()
         }
     }*/
    fun timerstart() {
        try {
            val timer = object : CountDownTimer(30 * 60 * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    logout()

                }

            }
            timer.start()

        } catch (e: Exception) {
        }
    }
}