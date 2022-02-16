package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.adsum.camel_masterapplication.Adapter.AddUserListAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.AddUserListResponse
import com.adsum.camel_masterapplication.R

import com.adsum.camel_masterapplication.databinding.FragmentAddUsersBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import com.adsum.camel_masterapplication.databinding.ItemAddUsersBinding


class AddUsersFragment : Fragment(),AddUserListAdapter.OnCheckedChangeListener{

    private lateinit var addUserListAdapter: AddUserListAdapter
    private  var itemAddUsersBinding: ItemAddUsersBinding? =null
    private var addUsersBinding: FragmentAddUsersBinding?=null
    private lateinit var rootView: View
    lateinit var button : Button
    private lateinit var checkboxlist : ArrayList<AddUserListResponse.Data>


    companion object {
        var resmain=AddUserListResponse()

        fun newInstance(
        ): AddUsersFragment {
            val fragment: AddUsersFragment =
                AddUsersFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addUsersBinding = FragmentAddUsersBinding.inflate(inflater, container, false)
        rootView = addUsersBinding!!.root

        // getUser()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()

        addUsersBinding?.tvSelectAll?.setTag(1)
        addUsersBinding?.tvSelectAll?.setOnClickListener {
            addUserListAdapter.selectUser(addUsersBinding?.tvSelectAll?.getTag() as Int?)
            if(addUsersBinding?.tvSelectAll?.getTag()==1){
                addUsersBinding?.tvSelectAll?.setTag(0)
                addUsersBinding?.tvSelectAll?.setText(getString(R.string.deselect))
            }
            else
            {
                addUsersBinding?.tvSelectAll?.setText(getString(R.string.select))
                addUsersBinding?.tvSelectAll?.setTag(1)
            }
        }

        addUsersBinding?.tvAdd?.setOnClickListener {




            

        }
    }




    private fun getUser(){
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                val url: String = CamelConfig.WEBURL + CamelConfig.userList
                //Log.e("san", "url:----" + url)


//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(response: JSONObject?) {
                           // Log.e("san", "response:---" + response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()

                            val res = gson.fromJson(
                                response.toString(),
                                AddUserListResponse::class.java
                            )
                            if (res.status== 1) {

                                context?.let {
                                    inituser(it, res.data)

                                }
                                addUserListAdapter.notifyDataSetChanged()
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

    private fun inituser(context: Context, userList: ArrayList<AddUserListResponse.Data>){

        addUserListAdapter = AddUserListAdapter(context, userList,this)
        addUsersBinding?.userRecyclerview?.adapter = addUserListAdapter

    }

    override fun OnCheckedChangeListener(userList: AddUserListResponse.Data, position: Int) {
        TODO("Not yet implemented")
    }


}