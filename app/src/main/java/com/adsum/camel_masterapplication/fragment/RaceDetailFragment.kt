package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.adsum.camel_masterapplication.Adapter.RaceDetailAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.DeleteRaceDetailResponse
import com.adsum.camel_masterapplication.Model.RaceDetailResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentRaceDetailBinding
import com.adsum.camel_masterapplication.databinding.FragmentUpdateDateBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject


class RaceDetailFragment : Fragment(), RaceDetailAdapter.OnRaceDetailClickListener,
    UpdateDateFragment.UpdateDialogInterface {

    private lateinit var raceDetailBinding: FragmentRaceDetailBinding
    private var updateDateBinding: FragmentUpdateDateBinding?=null
    private lateinit var raceid:String
    private lateinit var raceDetailAdapter: RaceDetailAdapter

    //var treadingContentList: ArrayList<RaceDetailResponse.Data>? = ArrayList()
    private lateinit var rootView: View

    companion object {
        fun newInstance(
        ): RaceDetailFragment {
            val fragment: RaceDetailFragment =
                RaceDetailFragment()
            val args = Bundle()
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

        raceDetailBinding = FragmentRaceDetailBinding.inflate(inflater, container, false)
        rootView = raceDetailBinding.root

        init()
        getData()

        return rootView
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_race_detail, container, false)
    }

    private fun init() {

    }

    private fun getData() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                //raceid = CommonFunctions.getPreference(activity, Constants.ID, 0)
                var url: String = CamelConfig.WEBURL + CamelConfig.racelist
                //  Log.e("san","msg"+url)

//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization,Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                RaceDetailResponse::class.java
                            )
                           // treadingContentList?.addAll(res.data)
                           // date = res.data
                            context?.let {
                                initRace(it, res.data)
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

    private fun initRace(context: Context, raceList: ArrayList<RaceDetailResponse.Data>) {

        raceDetailAdapter = RaceDetailAdapter(context, raceList, this)
        raceDetailBinding.raceRecyclerView.adapter = raceDetailAdapter

    }

    override fun OnRaceDetailClickListener(raceList: RaceDetailResponse.Data, position: Int) {
        openFragment(
            SubRaceDetailFragment.newInstance(
                raceList.id,
                raceList.raceId,
                raceList.raceName,
                raceList.startDate,
                raceList.endDate,
                Constants.isfromrace,
                position
                // raceList.raceId,position
                //category.race_id,category.no_of_participants[0].round_id.toString(), position
            ), "racedetail"
        )
    }

    fun openFragment(fragment: Fragment?, name: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout1, fragment!!)
        transaction?.addToBackStack(name)
        transaction?.commit()
    }

    override fun OnDeleteRecord(raceList: RaceDetailResponse.Data,position: Int) {
        val builder = activity?.let { AlertDialog.Builder(it) }

        builder?.setTitle("Confirm")
        builder?.setMessage("Are you sure?")
        builder?.setPositiveButton("YES") { dialog, which -> // Do nothing but close the dialog
            DeleteRace(raceList.raceId.toInt(),position)

            //getData()
        }

        builder?.setNegativeButton(
            "NO"
        ) { dialog, which -> // Do nothing
            dialog?.dismiss()
        }

        val alert = builder?.create()
        alert?.show()

    }


    private fun DeleteRace(raceid: Int, position: Int) {

        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url:String = CamelConfig.WEBURL + CamelConfig.removeRaceList+raceid
               // var url: String = "https://uaqcrc.com/wp-json/camel/v1/rmvracedetail"
                Log.e("san", "DELETE:--" + url)
//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addBodyParameter("race_id", raceid.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    //.setOkHttpClient(okHttpClient)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("san", "DELETEresponse:--" + response)
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                DeleteRaceDetailResponse::class.java
                            )

                            if (res.status == 1) {
                                CommonFunctions.showToast(activity, res.response)
                                raceDetailAdapter.deleterace(position)


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




    override fun OnRaceScheduleClickListener(
        raceList: RaceDetailResponse.Data,
        position: Int,
        raceid: String
    ) {
        var dialog = UpdateDateFragment.newInstance(raceList.raceId, position, this)


        dialog.show(
            requireFragmentManager(),
            "updateFragment"
        )

    }

    override fun OnUserClickListener(raceList: RaceDetailResponse.Data, position: Int) {
        openFragment1(
            AddUserListFragment.newInstance(
                raceList.raceId,
                position
            ),"userList")
    }

    fun openFragment1(fragment: Fragment?, name: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.framee2, fragment!!)
        transaction?.addToBackStack(name)
        transaction?.commit()
    }

    override fun onFinishEditDialog(startdate: String?, endDate: String?, position: Int?) {
        //Toast.makeText(context,""+startdate,Toast.LENGTH_LONG).show()

        raceDetailAdapter.updateDate(startdate,endDate,position)
    }


}
