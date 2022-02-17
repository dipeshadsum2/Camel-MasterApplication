package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adsum.camel_masterapplication.Adapter.NoOfParticipateAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.NoOfParticipateResponse
import com.adsum.camel_masterapplication.databinding.FragmentNoOfParticipateBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject


class FragmentNoOfParticipate : Fragment(),NoOfParticipateAdapter.OnnoOfparticipateclickListner {

    private lateinit var binding: FragmentNoOfParticipateBinding
    private lateinit var rootView : View
    private lateinit var race_id : String
    private lateinit var round_id : String
    private var position :Int = 0

    private lateinit var noOfParticipateAdapter : NoOfParticipateAdapter

    companion object{
        fun newInstance(
            param1: String,
            param2:String?,
            param3:Int
        ): FragmentNoOfParticipate {
            val fragment: FragmentNoOfParticipate = FragmentNoOfParticipate()
            val args = Bundle()
            args.putString(Constants.race_id, param1)
            args.putString(Constants.round_id, param2)
            //args.putString(Constants.id,param1)
            args.putInt(Constants.position, param3)
            fragment.setArguments(args)
            return fragment
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoOfParticipateBinding.inflate(inflater,container,false)
        rootView = binding.root
        //race_id = requireArguments().getInt(Constants.race_id).toString()
        race_id = requireArguments().getString(Constants.race_id).toString()
        round_id = requireArguments().getString(Constants.round_id).toString()
        position = requireArguments().getInt(Constants.position)
        init()
        return rootView
    }
    var datamain : List<NoOfParticipateResponse.Data.Members.Member>? = null

    private fun init(){

        val url:String = CamelConfig.WEBURL + CamelConfig.ViewRoundMemberlisting
        CommonFunctions.createProgressBar(context,"Please Wait")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(requireActivity()))
            .build()

            AndroidNetworking.post(url)
                .addHeaders(Constants.Authorization,Constants.Authkey)
                .addBodyParameter(
                    "round_id",
                    round_id)
                .addBodyParameter(
                    "race_id",
                    race_id)
                .setTag(url)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.e("Tag","res" + response)
                        CommonFunctions.destroyProgressBar()
                        val gson = Gson()

                        val res = gson.fromJson(
                            response.toString(),
                            NoOfParticipateResponse::class.java
                        )
//                        if(res.status == 1){
                            CommonFunctions.destroyProgressBar()
//                            datamain = res.data
                            //CommonFunctions.showToast(context,res.response)
                            context?.let { initParticipateRv(it,res.data) }
//                        }
//                        else{
//                            CommonFunctions.showToast(context,"No Data")
//                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                        CommonFunctions.showToast(context,anError.toString())
                    }

                })
    }
private fun initParticipateRv(context:Context, data:ArrayList<NoOfParticipateResponse.Data>){
    noOfParticipateAdapter = NoOfParticipateAdapter(context,data,this)
    binding.rvCategoryDetails.adapter = noOfParticipateAdapter
}

    override fun OnClick(participate: NoOfParticipateResponse.Data.Members.Member, position: Int) {

    }


}