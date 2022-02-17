package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentFemailBinding
import com.adsum.camel_masterapplication.Adapter.FemaleAdapter
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.*
import com.adsum.camel_masterapplication.databinding.AddCamelPopupBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class FemaleFragment: Fragment(), FemaleAdapter.OnfedeleteClickListener{


    companion object{
        var resmain = MaleResponse()
    }
    private lateinit var mainAkbarRes : ArrayList<CampleResp.Data>
   // private  var dataAkbarRes : ArrayList<CampleResp.Data>? = null
    private lateinit var femaleAdapter: FemaleAdapter
    private lateinit var binding: FragmentFemailBinding
    private lateinit var addCamelPopupBinding: AddCamelPopupBinding
    private var user_id by Delegates.notNull<String>()
    var count: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFemailBinding.inflate(inflater, container, false)
        addCamelPopupBinding = AddCamelPopupBinding.inflate(LayoutInflater.from(context))
        var rootView = binding.root
//        presenter = MaleFemale()
        user_id = CommonFunctions.getPreference(activity, Constants.ID, "").toString()
        getdata()
        init()
        return rootView
    }

    fun onCreateDialog(): Dialog? {

        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.setView(addCamelPopupBinding.root)
        addCamelPopupBinding.edtCamel.setHint("أدخل اسم الجمل")
        addCamelPopupBinding.edtCamel.setText("")
        return builder?.create()
    }


    private fun addCamel(camelName: String, gender:String, status:Int, id: String){
        val url: String =  CamelConfig.WEBURL+CamelConfig.addcamel
       // Log.e("san","url:---" +url)
        //Progress start

        CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(requireActivity()))
            .build()


        AndroidNetworking.get(url)
            .addHeaders(Constants.Authorization, Constants.Authkey)
            .addQueryParameter(Constants.camel, camelName)
            .addQueryParameter(Constants.gender, gender)
            .addQueryParameter(Constants.status, status.toString())
            .addQueryParameter(Constants.user_id, id.toString())
            .setTag(url)
            .setPriority(Priority.HIGH)
//            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    //Log.e("san","responce1:--" +response)

                    //Destroy Progressbar
                    CommonFunctions.destroyProgressBar()
                    val gson = Gson()
                    val res = gson.fromJson(response.toString(), AddCamelResponse::class.java)


                    if(res.status == 1){
                        getdata()

                    }else{
                        CommonFunctions.showToast(activity, res.response)
                    }
                }
                override fun onError(anError: ANError?) {
                    CommonFunctions.destroyProgressBar()
                    CommonFunctions.showToast(context, anError.toString())
                }
            })
    }

    private fun init() {

        try {
            getdata()
            var d = onCreateDialog()
            binding.btnAddFemaleCamel.setOnClickListener{
                addCamelPopupBinding.edtCamel.setText("")
                d?.show()

            }
            addCamelPopupBinding.btnFemale.setOnClickListener {
                for (j in 0..resmain.size -1) {
                    val subcategory = resmain[j]
                    if (subcategory.rcCamel.equals(addCamelPopupBinding.edtCamel.text.toString())) {
                        CommonFunctions.showToast(activity, "الجمل موجود بالفعل")
                        return@setOnClickListener
                    }
                }
                if (count < 10) {
                    if (addCamelPopupBinding.edtCamel.text.toString().isNotEmpty()) {
                        addCamel(
                            addCamelPopupBinding.edtCamel.text.toString(),
                            Constants.female,
                            1,
                            user_id
                        )

                       // filter()

                        d?.dismiss()
                    } else {
                        addCamelPopupBinding.edtCamel.requestFocus()
                        CommonFunctions.showToast(
                            requireContext(),
                            "يجب ألا يكون اسم الجمل فارغًا"
                        )
                    }
                } else {
                    d?.dismiss()
                    CommonFunctions.showToast(
                        requireContext(),
                        "لا يمكن إضافة أكثر من 10 جمل"
                    )
                }
            }
            addCamelPopupBinding.btnCancel.setOnClickListener {
                addCamelPopupBinding.edtCamel.setText("")
                d?.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getdata(){
        if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
            // rc_id = CommonFunctions.getPreference(context, Constants.ID, "").toString()
            val url: String = CamelConfig.WEBURL + CamelConfig.malelist + user_id

            //   Log.e("san","urlList:---" +url)
            // Log.e("san","rcid-----"+rc_id)


//Progress start
            CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(requireActivity()))
                .build()
            AndroidNetworking.get(url)
                .addHeaders(Constants.Authorization, Constants.Authkey)
                .setTag(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(response: JSONObject?) {
                       // Log.e("san", "response2:---" + response)
                        CommonFunctions.destroyProgressBar()
                        var gson = Gson()
                        // CommonFunctions.showToast(context,"Data"+ )
                        val res = gson.fromJson(
                            response.toString(),
                            CampleResp::class.java

                        )
                        if (res.status == 1) {

                                mainAkbarRes = res.data as ArrayList<CampleResp.Data>
                                val filterlist = mainAkbarRes.filter  { it.rcGender == "Female" }

                              //  dataAkbarRes?.addAll(mainAkbarRes)
                                setadapterdata(filterlist)
                                femaleAdapter.notifyDataSetChanged()

                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                    }
                })




        }
    }
//    @SuppressLint("NotifyDataSetChanged")
//    private fun filter(){
//        dataAkbarRes?.clear()
//
//
//        for ( i in 0..mainAkbarRes.size-1)
//        {
//            if(mainAkbarRes[i].rcGender.equals("female"))
//            {
//                dataAkbarRes?.add(mainAkbarRes[i])
//            }
//        }
//
//        femaleAdapter?.notifyDataSetChanged()
//    }

    private fun setadapterdata(res: List<CampleResp.Data>) {
        femaleAdapter = FemaleAdapter(requireActivity(), res as ArrayList<CampleResp.Data>,this)
        binding.femaleRecycle.adapter = femaleAdapter
        count = femaleAdapter.itemCount
    }

//    private fun initMale(context: Context, list: ArrayList<CampleResp.Data>){
//
//        val malelist: ArrayList<CampleResp.Data> = ArrayList()
//        val femalelist: ArrayList<CampleResp.Data> = ArrayList()
//
//        var i = 0
//        while ( i< list.size) {
//            if (list[i].rcGender == Constants.male) {
//                malelist.add(list[i])
//            }
//            else{
//                femalelist.add(list[i])
//            }
//            i++
//        }
//
//        //femaleAdapter = FemaleAdapter(context, femalelist,this)
//       // binding.femaleRecycle.adapter = femaleAdapter
//      //  count = femaleAdapter!!.itemCount
//
//    }

    override fun OndeleteClick(malelist: CampleResp.Data, position: Int) {

        val builder = activity?.let { AlertDialog.Builder(it) }

        builder?.setTitle("Confirm")
        builder?.setMessage("Are you sure?")
        builder?.setPositiveButton("YES") { dialog, which -> // Do nothing but close the dialog
            DeleteCamel(malelist.rcId.toInt())
      //      femaleAdapter.malelist.removeAt(position)
       //     femaleAdapter.notifyDataSetChanged()
            getdata()
        }


        builder?.setNegativeButton(
            "NO"
        ) { dialog, which -> // Do nothing
            dialog?.dismiss()
        }

        val alert = builder?.create()
        alert?.show()

    }
    private fun DeleteCamel(id: Int) {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.removeCamel + id
//Progress start
                CommonFunctions.createProgressBar(context, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                DeleteCamelResponse::class.java

                            )
                            if(res.status == 1){
                                CommonFunctions.showToast(activity, res.response)
                            }else{
                                CommonFunctions.showToast(activity, res.response)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    interface A {
        fun getMaleDataa(res: ArrayList<MaleResponse.MaleResponseItem>): MaleResponse {
            return resmain
        }
    }
}



