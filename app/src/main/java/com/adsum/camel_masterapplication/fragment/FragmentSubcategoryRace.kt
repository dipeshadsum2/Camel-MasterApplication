package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Model.MaleResponse
import com.adsum.camel_masterapplication.Adapter.SubCategoryRaceAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Model.*
import com.adsum.camel_masterapplication.R


import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.databinding.FragmentSubcategoryRaceBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject


class FragmentSubcategoryRace : Fragment(), SubCategoryRaceAdapter.OnsubdeleteClickListener {

    private lateinit var rootView: View
    private lateinit var fragmentSubcategoryRaceBinding: FragmentSubcategoryRaceBinding

    private lateinit var subCategoryRaceAdapter: SubCategoryRaceAdapter
    private var roundid: Int = 0
    private var userid = 0
    private var rl_id = 0
    private var race_id: Int = 0
    private var position: Int = 0
    private var gender: String? = null
    private var racename = ""
    private var customization: String? = null
    private var roundname: String? = null
    private lateinit var response2: ArrayList<SubcategoryRaceResponse>
    private lateinit var res: MaleResponse
    private lateinit var data: MaleResponse.MaleResponseItem
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var malelist: ArrayList<MaleResponse.MaleResponseItem>
    private lateinit var femalelist: ArrayList<MaleResponse.MaleResponseItem>
    private lateinit var mixModel: ArrayList<MixModel>
    val malelist2 = ArrayList<String>()
    var malelisttemp = ArrayList<String>()
    var malelistID = ArrayList<String>()
    var malelistIDtemp = ArrayList<String>()
    val femalelist2 = ArrayList<String>()
    var femaletemp = ArrayList<String>()
    var femalelistID = ArrayList<String>()
    var femalelistIDtemp = ArrayList<String>()
    var from: String? = null
    companion object {
        fun newInstance(
            param1: Int,
            param2: Int,
            param3: String,
            param4: String,
            param5: String,
            param6: String,
            from: String,
            position: Int
        ): FragmentSubcategoryRace {
            val fragment = FragmentSubcategoryRace()
            val args = Bundle()
            args.putInt(Constants.id, param1)
            args.putInt(Constants.race_id, param2)
            args.putString(Constants.type, param3)
            args.putString(Constants.round_name, param4)
            args.putString(Constants.race_name, param6)
            args.putString(Constants.customization, param5)
            args.putString(Constants.from, from)
            args.putInt(Constants.position, position)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentSubcategoryRaceBinding =
            FragmentSubcategoryRaceBinding.inflate(inflater, container, false)
        rootView = fragmentSubcategoryRaceBinding.root
        roundid = requireArguments().getInt(Constants.id)
        race_id = requireArguments().getInt(Constants.race_id)
        racename = requireArguments().getString(Constants.race_name).toString()
        position = requireArguments().getInt(Constants.position)
        gender = requireArguments().getString(Constants.type)
        from = requireArguments().getString(Constants.from)
        customization = requireArguments().getString(Constants.customization)
        roundname = requireArguments().getString(Constants.round_name)
        malelist = ArrayList<MaleResponse.MaleResponseItem>()
        femalelist = ArrayList<MaleResponse.MaleResponseItem>()
        fragmentSubcategoryRaceBinding.tvSubSex.text = gender
        fragmentSubcategoryRaceBinding.tvSubCostomization.text = customization
        fragmentSubcategoryRaceBinding.tvStrok.text = roundname
        fragmentSubcategoryRaceBinding.tvRaceid.text = racename
        init()
        if(from.equals(Constants.isFromHistory)){
            fragmentSubcategoryRaceBinding.spnCamelname.visibility=View.GONE
            fragmentSubcategoryRaceBinding.btnAddCamel.visibility=View.GONE
        }
        return rootView
    }

    private fun initMale(context: Context, gender: String) {
        userid = CommonFunctions.getPreference(activity, Constants.ID, 0)
        var url: String = CamelConfig.WEBURL+CamelConfig.malelist
        CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(requireActivity()))
            .build()

        AndroidNetworking.get(url)
            .addQueryParameter(Constants.user_id, userid.toString())
            .addHeaders(Constants.Authorization, Constants.Authkey)
            .setTag(url)
//            .setOkHttpClient(okHttpClient)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    CommonFunctions.destroyProgressBar()
                    var gson = Gson()
                    res = gson.fromJson(
                        response.toString(),
                        MaleResponse::class.java
                    )


                    var i = 0
                    malelistID.clear()
                    malelistIDtemp.clear()
                    femalelistIDtemp.clear()
                    femalelistID.clear()
                    malelist2.clear()
                    femalelist2.clear()
                    while (i < res.size) {
                        if (res[i].rcGender == Constants.male) {
                            malelist.add(res[i])
                            malelist2.add(res[i].rcCamel)
                            malelistID.add(res[i].rcId)
                        } else {
                            femalelist.add(res[i])
                            femalelist2.add(res[i].rcCamel)
                            femalelistID.add(res[i].rcId)
                        }
                        i++
                    }


                    if (gender .equals("جعدان",true)) {
                        malelisttemp = malelist2
                        malelistIDtemp = malelistID
                        for (j in 0..mixModel.size - 1) {
                            val subcategory = mixModel[j]
                            for (k in 0..malelist2.size - 1) {
                                if (subcategory.rc_camel.equals(malelist2.get(k), true)) {
                                    malelisttemp.removeAt(k)
                                    malelistIDtemp.removeAt(k)
                                    break
                                }
                            }
                        }
                        arrayAdapter = ArrayAdapter<String>(
                            context,
                            R.layout.dropdown_menu_popup_item,
                            malelisttemp
                        )
                    } else {
                        femaletemp = femalelist2
                        femalelistIDtemp = femalelistID
                        for (j in 0..mixModel.size - 1) {
                            val subcategory = mixModel[j]
                            for (k in 0..femalelist2.size - 1) {
                                if (subcategory.rc_camel.equals(femalelist2.get(k), true)) {
                                    femaletemp.removeAt(k)
                                    femalelistIDtemp.removeAt(k)
                                    break
                                }
                            }
                        }
                        arrayAdapter = ArrayAdapter<String>(
                            context,
                            R.layout.dropdown_menu_popup_item,
                            femaletemp
                        )
                    }


                    fragmentSubcategoryRaceBinding.spnCamelname.adapter = arrayAdapter


                }

                override fun onError(anError: ANError?) {
                    CommonFunctions.destroyProgressBar()
                    CommonFunctions.showToast(context, anError.toString())

                }
            })


    }

    private fun init() {

        try {
            showList()

            var position2 = 0
            fragmentSubcategoryRaceBinding.spnCamelname.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        position2 = position


                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            //addcamel
            fragmentSubcategoryRaceBinding.btnAddCamel.setOnClickListener {
                var camelid = ""

                if (gender.equals("جعدان",true)) {
                    if(malelistIDtemp.size==0)
                        return@setOnClickListener
                    camelid = malelistIDtemp[position2]
                } else {
                    if(femalelistIDtemp.size==0)
                        return@setOnClickListener
                    camelid = femalelistIDtemp[position2]
                }
                var url: String = CamelConfig.WEBURL+CamelConfig.addCamelMember
//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addPathParameter(Constants.race_id, race_id.toString())
                    .addPathParameter(Constants.round_id, roundid.toString())
                    .addPathParameter(Constants.user_id, userid.toString())
                    .addPathParameter(Constants.camel_id, camelid)
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
                                AddRoundMemberResponse::class.java
                            )
                            if (res.status == 1) {
                                showList()
                                CommonFunctions.showToast(activity, res.response)
                            } else {
                                CommonFunctions.showToast(activity, res.response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            fragmentSubcategoryRaceBinding.btnAddCamel.isEnabled = true

                            CommonFunctions.destroyProgressBar()
                        }

                    })
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showList() {
        if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

            val url: String = CamelConfig.WEBURL+CamelConfig.get_sub_category_list
            //Progress start

            val data = JSONObject()
            CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(requireActivity()))
                .build()

            AndroidNetworking.post(url)
                .addHeaders(Constants.Authorization, Constants.Authkey)
                .addQueryParameter(Constants.race_id, race_id.toString())
                .addQueryParameter(Constants.round_id, roundid.toString())
                .setTag(url)
                .addJSONObjectBody(data)
                .setPriority(Priority.HIGH)
//                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {

                        //Destroy Progressbar
                        CommonFunctions.destroyProgressBar()
                        val gson = Gson()
                        val res = gson.fromJson(
                            response.toString(),
                            SubcategoryRaceResponse::class.java
                        )
                        if (res.get(0).members.size == 0) {
                            fragmentSubcategoryRaceBinding.subRaceRecyclerView.visibility =
                                View.GONE
                            fragmentSubcategoryRaceBinding.tvNotfound.visibility = View.VISIBLE

                        } else {
                            fragmentSubcategoryRaceBinding.subRaceRecyclerView.visibility =
                                View.VISIBLE
                            fragmentSubcategoryRaceBinding.tvNotfound.visibility = View.GONE

                        }

                        response2 = ArrayList()
                        response2.add(res)
                        context?.let {
                            initsubRace(it, res)
                        }
                        gender?.let {
                            initMale(requireActivity(), it)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                    }
                })

        }
    }


    private fun initsubRace(
        context: Context,
        subcategory: List<SubcategoryRaceResponse.SubcategoryRaceResponseItem>
    ) {

        mixModel = ArrayList<MixModel>()
        var rcGender = ""
        var customization = ""
        var description = ""
        var rcamelname = ""
        var rcdetails = ""
        var rl_id = ""
        var user_id = ""

        for (i in 0 until subcategory.size) {
            for (j in 0 until subcategory[i].members.size) {
                rcGender = subcategory[i].members[j].rcGender
                rl_id = subcategory[i].members[j].rlId
                customization = subcategory[i].rounds.customization
                description = subcategory[i].rounds.description
                rcamelname = subcategory[i].members[j].rcCamel
                rcdetails = subcategory[i].members[j].user.nameOfParticipant
                user_id = subcategory[i].members[j].userId

                mixModel.add(
                    MixModel(
                        rcGender,
                        customization,
                        description,
                        rl_id,
                        rcamelname,
                        rcdetails,
                        user_id
                    )
                )
            }
        }

        subCategoryRaceAdapter = SubCategoryRaceAdapter(context, mixModel,this,from.toString())
        fragmentSubcategoryRaceBinding.subRaceRecyclerView.adapter = subCategoryRaceAdapter

    }

    override fun OndeleteClick(
        subcategory: MixModel,
        position: Int
    ) {

        val builder = activity?.let { AlertDialog.Builder(it) }

        builder?.setTitle("Confirm")
        builder?.setMessage("Are you sure?")
        builder?.setPositiveButton("YES") { dialog, which -> // Do nothing but close the dialog
            rl_id = subcategory.rl_id.toInt()
            if (CommonFunctions.getPreference(requireContext(), Constants.ID, 0)
                    .toString() == subcategory.userId
            ) {
                Delete(rl_id)
//                subCategoryRaceAdapter.subcategory.removeAt(position)
//                subCategoryRaceAdapter.notifyDataSetChanged()
                showList()

            } else {
                CommonFunctions.showToast(activity, "not remove")
            }

        }

        builder?.setNegativeButton(
            "NO"
        ) { dialog, which -> // Do nothing
            dialog?.dismiss()
        }

        val alert = builder?.create()
        alert?.show()
    }

    private fun Delete(id: Int) {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.removecamel + id
//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addPathParameter(Constants.rl_id, rl_id.toString())
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
                                SubRaceResponse::class.java
                            )
                            if (res.status == 1) {

                                CommonFunctions.showToast(activity, res.response)
                                gender?.let {
                                    initMale(requireActivity(), it)
                                }
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



