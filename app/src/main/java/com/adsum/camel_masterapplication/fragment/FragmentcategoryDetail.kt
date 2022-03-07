package com.adsum.camel_masterapplication.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.adsum.camel_masterapplication.Adapter.categoryDetailsAdapter2
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.categoryDetailsResponse3
import com.adsum.camel_masterapplication.R
//import com.adsum.camel_masterapplication.Model.categoryDetailsResponse
import com.adsum.camel_masterapplication.databinding.FragmentFragmentcategoryDetailBinding
import com.adsum.camel_masterapplication.databinding.PopupNoRecordBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import kotlin.properties.Delegates


class FragmentcategoryDetail : Fragment() {
    private lateinit var binding: FragmentFragmentcategoryDetailBinding
    private lateinit var rootView: View
    private var category_id: Int = 0
    private lateinit var category_name: String
    private var position: Int = 0
    private var user_id by Delegates.notNull<String>()
    private var popupNoRecordBinding : PopupNoRecordBinding? = null

    //private var user_id2 = 0
    //private lateinit var customAdapter: CustomAdapter
    private lateinit var CategoryDetailsAdapter2: categoryDetailsAdapter2

    companion object {
        fun newInstance(param1: String, param2: Int, param3: Int): FragmentcategoryDetail {
            val fragment: FragmentcategoryDetail = FragmentcategoryDetail()
            val args = Bundle()
            args.putString(Constants.categoryname, param1)
            args.putInt(Constants.category_id, param2)
            //args.putString(Constants.id,param1)
            args.putInt(Constants.position, param3)
            fragment.setArguments(args)
            return fragment
        }
    }


    fun openFragment(fragment: Fragment?, name: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.framee, fragment!!)
        transaction?.addToBackStack(name)
        transaction?.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val view = inflater.inflate(R.layout.fragment_fragmentcategory_detail,container,false)
//        val textView: TextView = view.findViewById(R.id.txtcategory)
//        val args = this.arguments
//        val inputData= args?.get("data")
//        textView.text = inputData.toString()
        binding = FragmentFragmentcategoryDetailBinding.inflate(inflater, container, false)
        popupNoRecordBinding = PopupNoRecordBinding.inflate(inflater,container,false)
        rootView = binding.root
        user_id = CommonFunctions.getPreference(activity, Constants.ID, "").toString()
        category_name = requireArguments().getString(Constants.categoryname).toString()
        category_id = requireArguments().getInt(Constants.category_id)
        position = requireArguments().getInt(Constants.position)
        binding.tvCategoryname.text = category_name

        init()
        return rootView
    }

    var datamain: List<categoryDetailsResponse3.Data>? = null
    private fun init() {
//        user_id2 = CommonFunctions.getPreference(activity,Constants.ID,0)

        val url: String = CamelConfig.WEBURL + CamelConfig.participateinrace
        Log.e("tag","url:-"+url)

        CommonFunctions.createProgressBar(context, "Please wait")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(requireActivity()))
            .build()

        AndroidNetworking.post(url)
            .addHeaders(Constants.Authorization, Constants.Authkey)
//                    .addPathParameter(mParams)
            .addBodyParameter("category_id", category_id.toString())
            .addBodyParameter("user_id", user_id)
            .setTag(url)
            .setPriority(Priority.HIGH)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val gson = Gson()
                    val res = gson.fromJson(
                        response.toString(),
                        categoryDetailsResponse3::class.java
                    )
                    if (res.status == 1) {
                        CommonFunctions.destroyProgressBar()
                        datamain = res.data

                        CategoryDetailsAdapter2 = categoryDetailsAdapter2(context,
                            datamain!!,
                            object : categoryDetailsAdapter2.OnCategoryDetilsclickListner {
                                override fun onroundnameClick(
                                    category: categoryDetailsResponse3.Data.Round,
                                    position: Int,
                                    racename: String
                                ) {
                                    openFragment(
                                        FragmentSubcategoryRace.newInstance(
                                            category.round_id.toInt(),
                                            category.race_id.toInt(),
                                            category.type,
                                            category.round_name,
                                            category.description,
                                            racename,
                                            Constants.isfromrace,
                                            position
                                        ), "Fragment"
                                    )
                                    CommonFunctions.setPreference(
                                        context,
                                        Constants.isFromHistory,
                                        true
                                    )
//                                    openFragment(
//                                        FragmentParticipateinRaceRound.newInstance(
//                                            category.race_id,category.round_id, position
//                                        ), "participateinraceround"
//                                    )
                                }

                                override fun onnoofparticipantClick(
                                    category: categoryDetailsResponse3.Data.Round,
                                    position: Int,
                                    racename: String
                                ) {
                                    openFragment(
                                        FragmentNoOfParticipate.newInstance(
                                            category.race_id, category.round_id, position
                                        ), "Noofparticipate"
                                    )
                                }

//                                override fun onnoofparticipantClick(
//                                    category: categoryDetailsResponse3.Data,
//                                    position: Int
//                                ) {
//                                    openFragment(
//                                        FragmentNoOfParticipate.newInstance(
//                                            category.race_id,category.round[0].round_id, position
//                                        ),"Noofparticipate"
//                                    )
//                                }

                            }
                        )
                        binding.rvCategoryDetails.adapter = CategoryDetailsAdapter2

                        binding.rvCategoryDetails.visibility = View.VISIBLE

//                            context?.let { initcategorydetailsRv(it,res.data) }
                    } else {
                        CommonFunctions.destroyProgressBar()
                        val dialog = activity?.let { Dialog(it) }
                        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.popup_no_record)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        val Done = dialog.findViewById(R.id.doneTVNoRecord) as TextView
                        Done.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
//                        CommonFunctions.showToast(activity, res.response)
                    }
                }


                override fun onError(anError: ANError?) {
                    CommonFunctions.destroyProgressBar()

                }
            })
    }


//    private fun initcategorydetailsRv(it: Context,data: List<categoryDetailsResponse2.Data>){
//        CategoryDetailsAdapter = categoryDetailsAdapter(it,data,this)
//        binding.rvCategoryDetails.adapter = CategoryDetailsAdapter
//    }


}