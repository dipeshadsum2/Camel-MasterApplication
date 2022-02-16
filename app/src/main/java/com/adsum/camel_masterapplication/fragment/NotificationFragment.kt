package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Adapter.NotificationAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.NotificationErrorResponse
import com.adsum.camel_masterapplication.Model.NotificationResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentNotificationBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*


class NotificationFragment : Fragment() , NotificationAdapter.OnNotificationClickListener{
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var rootView: View
   // private var ID: Int = 0
    private var lang=""
    private lateinit var notificationAdapter: NotificationAdapter
   // private var newRecyclerView: RecyclerView? =null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<NotificationAdapter.ViewHolder>? = null

    companion object {


        fun newInstance(
            param1: String?,
            param2: String?
        ): NotificationFragment {
            val fragment: NotificationFragment =
                NotificationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        rootView = binding.root

        binding.rvNotification.setHasFixedSize(true)

        init()
        return rootView


    }
    private fun init(){
        try {
           if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                val url: String = CamelConfig.WEBURL + CamelConfig.get_notification


                val data = JSONObject()


                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))


                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()
                AndroidNetworking.post(url)
                    .setTag(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(data)
                    .setPriority(Priority.HIGH)
                   // .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("San","res"+response)


                            //Destroy Progressbar
                            val gson = Gson()
                            try{
                                val res = gson.fromJson(response.toString(), NotificationResponse::class.java)

                                if (res.status == 1) {
                                    CommonFunctions.destroyProgressBar()

                                    context?.let { initnotificationRv(it, res.data)}

                                }else{
                                    CommonFunctions.destroyProgressBar()

                                    CommonFunctions.showToast(activity,"else")
                                }

                           }catch (e : Exception){

                                val res = gson.fromJson(response.toString(), NotificationErrorResponse::class.java)

                                if (res.status == 0) {
                                    CommonFunctions.destroyProgressBar()

                                    CommonFunctions.showToast(activity, res.message)

                                }else{
                                    CommonFunctions.destroyProgressBar()

                                    CommonFunctions.showToast(activity,res.message)
                                }

                           }

                        }


                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                           // Log.e("San","res:--"+anError)
                           CommonFunctions.showToast(context, anError.toString())


                        }

                    })

            }


        }catch (e:Exception){

        }


    }
    private fun initnotificationRv(context: Context, data: List<NotificationResponse.Data>) {

        notificationAdapter = NotificationAdapter(context, data, this)
        binding.rvNotification.adapter = notificationAdapter

    }



    override fun onNotificationClick(notification: NotificationResponse.Data, position: Int) {

    }
}








