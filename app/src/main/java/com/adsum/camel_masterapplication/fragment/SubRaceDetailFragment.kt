package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import com.adsum.camel_masterapplication.Adapter.SubRaceDetailAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.RaceDetailResponse
import com.adsum.camel_masterapplication.Model.SubRaceDetailResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentSubRaceDetailBinding
import com.adsum.camel_masterapplication.databinding.ItemRaceDetailBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject


class SubRaceDetailFragment : Fragment() {

    private lateinit var subRaceDetailBinding: FragmentSubRaceDetailBinding
    private lateinit var itemRaceDetailBinding:ItemRaceDetailBinding
    //private var raceid :Int=0

    private lateinit var subRaceDetailAdapter:SubRaceDetailAdapter
    private lateinit var rootView: View
    private lateinit var raceid : String
    private lateinit var race_name: String
    private lateinit var startDate: String
    private lateinit var endDate:String
    private lateinit var isfromrace: String


    companion object {
        fun newInstance(
            id:String,
            raceid: String,
            race_name: String,
            startDate: String,
            endDate:String,
            isfromrace: String,
            position: Int,

        ): SubRaceDetailFragment {
            val fragment: SubRaceDetailFragment =
                SubRaceDetailFragment()
            val args = Bundle()
            args.putString(Constants.id,id)
            args.putString(Constants.race_id,raceid)
            args.putString(Constants.race_name,race_name)
            args.putString(Constants.start_time,startDate)
            args.putString(Constants.end_time,endDate)
            args.putString(Constants.isfromrace,isfromrace)
            args.putInt(Constants.position, position)
            fragment.setArguments(args)
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

        subRaceDetailBinding = FragmentSubRaceDetailBinding.inflate(inflater, container, false)
        rootView = subRaceDetailBinding.root



        raceid = requireArguments().getString(Constants.race_id).toString()
     //   Log.e("tag","race_id" + raceid)
        race_name=requireArguments().getString(Constants.race_name).toString()
        startDate=requireArguments().getString(Constants.start_time).toString()
        endDate=requireArguments().getString(Constants.end_time).toString()




        init()
        return rootView
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sub_race_detail, container, false)
    }

    private fun init() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                //raceid = CommonFunctions.getPreference(activity,"race_id",0)
                val url: String = CamelConfig.WEBURL + CamelConfig.subracelist+raceid
                Log.e("san", "url:----" + url)
                //val mparam= HashMap<String, String> = HashMap()
              //  mparam["raceid"]=raceid.toString()

//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    //.addPathParameter(Constants.race_id, raceid.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter("raceid",raceid)
                    //.addQueryParameter("raceid",raceid)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(response: JSONObject?) {
                            Log.e("san", "response:---" + response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()

                            val res = gson.fromJson(
                                response.toString(),
                                SubRaceDetailResponse::class.java
                            )
                            if (res.status== 1) {

                                context?.let {
                                    initRace(it, res.data)
                                }
                                subRaceDetailAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                        }
                    })

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun initRace(context: Context, subraceList: List<SubRaceDetailResponse.Data>){

        subRaceDetailAdapter = SubRaceDetailAdapter(context, subraceList, this)
        subRaceDetailBinding.subRaceRecyclerView.adapter = subRaceDetailAdapter

    }
}

