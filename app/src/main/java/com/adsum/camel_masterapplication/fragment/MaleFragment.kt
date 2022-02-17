package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentMailBinding
import com.adsum.camel_masterapplication.Adapter.MaleAdapter
import com.adsum.camel_masterapplication.Config.Constants

import com.adsum.camel_masterapplication.Model.*
import com.adsum.camel_masterapplication.databinding.AddMaleCamelPopupBinding
import com.adsum.camel_masterapplication.databinding.AlertPopupBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import kotlin.properties.Delegates


class MaleFragment : Fragment(), MaleAdapter.OndeleteClickListener {

    private lateinit var mainAkbarRes : ArrayList<AkbarResp.Data>
    private  var dataAkbarRes : ArrayList<AkbarResp.Data>? = null
    private lateinit var maleAdapter: MaleAdapter
    private lateinit var malebinding: FragmentMailBinding
    private lateinit var alertPopupBinding: AlertPopupBinding
    private lateinit var addMaleCamelPopupBinding: AddMaleCamelPopupBinding
    private lateinit var rootView: View
    private var user_id by Delegates.notNull<String>()
    var count: Int = 0

    companion object {
        var resmain = MaleResponse()

        fun newInstance(
        ): MaleFragment {
            val fragment: MaleFragment =
                MaleFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        malebinding = FragmentMailBinding.inflate(inflater, container, false)
        addMaleCamelPopupBinding = AddMaleCamelPopupBinding.inflate(LayoutInflater.from(context))
        rootView = malebinding.root
        user_id = CommonFunctions.getPreference(activity, Constants.ID, "").toString()


        init()
        return rootView
    }

    fun onCreateDialog(): Dialog? {

        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.setView(addMaleCamelPopupBinding.root)
        addMaleCamelPopupBinding.edtCamel.setHint("أدخل اسم الجمل")
        addMaleCamelPopupBinding.edtCamel.setText("")
        return builder?.create()

    }

    fun onCreateDialog1(): Dialog? {

        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.setView(alertPopupBinding.root)
        return builder?.create()

    }

    private fun addCamel(camelName: String, gender: String, status: Int, id: String) {
        var d = onCreateDialog()
        val url: String = CamelConfig.WEBURL + CamelConfig.addcamel

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
//                .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    //  Log.e("san","responsee:--" +response)

                    //Destroy Progressbar
                    CommonFunctions.destroyProgressBar()
                    val gson = Gson()
                    val res = gson.fromJson(response.toString(), AddCamelResponse::class.java)

                    if (res.status == 1) {
                        getdata()
                    } else {
                        CommonFunctions.showToast(activity, res.response)
                    }
                }

                override fun onError(anError: ANError?) {
                    CommonFunctions.destroyProgressBar()
                    CommonFunctions.showToast(context, anError.toString())
                }
            })
        // }else{
        //  CommonFunctions.showToast(activity,"alert 10 data")
        // }


    }

    private fun init() {

        try {
            getdata()
            var d = onCreateDialog()
            malebinding.btnAddMaleCamel.setOnClickListener {
                addMaleCamelPopupBinding.edtCamel.setText("")
                d?.show()
            }


            addMaleCamelPopupBinding.btnMale.setOnClickListener {
                for (j in 0..resmain.size - 1) {
                    val subcategory = resmain[j]
                    if (subcategory.rcCamel.equals(addMaleCamelPopupBinding.edtCamel.text.toString())) {
                        CommonFunctions.showToast(activity, "الجمل موجود بالفعل")
                        return@setOnClickListener
                    }
                }
                if (count < 10) {
                    if (addMaleCamelPopupBinding.edtCamel.text.toString().isNotEmpty()) {
                        //filter()

                        addCamel(
                            addMaleCamelPopupBinding.edtCamel.text.toString(),
                            Constants.male,
                            1,
                            user_id
                        )


                        d?.dismiss()
                    } else {
                        addMaleCamelPopupBinding.edtCamel.requestFocus()
                        CommonFunctions.showToast(requireContext(), "Camel name should not blank")
                    }
                } else {
                    d?.dismiss()
                    CommonFunctions.showToast(requireContext(), "Cannot add more than 10 camel")
                }
            }

            addMaleCamelPopupBinding.btnCancel.setOnClickListener {
                addMaleCamelPopupBinding.edtCamel.setText("")
                d?.dismiss()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getdata() {

        if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
            // raceid = CommonFunctions.getPreference(context, Constants.ID, 0)
            val url: String = CamelConfig.WEBURL + CamelConfig.malelist + user_id
          //  Log.e("san", "url:---" + url)

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
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(response: JSONObject?) {
                       // Log.e("san", "response3:---" + response)
                        CommonFunctions.destroyProgressBar()
                        val gson = Gson()
                        // CommonFunctions.showToast(context,"Data"+ )
                        val res = gson.fromJson(
                            response.toString(),
                            AkbarResp::class.java
                        )
                        if (res.status == 1) {

                            mainAkbarRes  = res.data as ArrayList<AkbarResp.Data>
                           // Log.e("san","mainAkbarRes:---"+mainAkbarRes)
                            val filterlist = mainAkbarRes.filter  { it.rcGender == "Male" }
                          //  Log.e("san","filterList:---"+filterlist)
                            setadapterdata(filterlist)
                            maleAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                    }

                })
        }
    }



    private fun setadapterdata(res: List<AkbarResp.Data>) {
        maleAdapter = MaleAdapter(requireActivity(), res as ArrayList<AkbarResp.Data>,this)
        malebinding.maleRecycle.adapter = maleAdapter
        count = maleAdapter.itemCount
    }


    override fun OndeleteClick(malelist: AkbarResp.Data, position: Int) {

        val builder = activity?.let { AlertDialog.Builder(it) }

        builder?.setTitle("Confirm")
        builder?.setMessage("Are you sure?")
        builder?.setPositiveButton("YES") { dialog, which -> // Do nothing but close the dialog
            DeleteCamel(malelist.rcId.toInt())
            getdata()
//            maleAdapter.malelist.removeAt(position)
//            maleAdapter.notifyDataSetChanged()

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
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
//                    .setOkHttpClient(okHttpClient)
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
                            if (res.status == 1) {
                                CommonFunctions.showToast(activity, res.response)
                            } else {
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

}
