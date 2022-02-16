package com.adsum.camel_masterapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Activity.AdminDashboardActivity
import com.adsum.camel_masterapplication.Activity.DashboardActivity
import com.adsum.camel_masterapplication.databinding.ActivityDashboardBinding
import com.adsum.camel_masterapplication.databinding.FragmentHomeBinding


class FragmentHome: Fragment() {

    private lateinit var binding:FragmentHomeBinding
   private lateinit var bindingprofile: ActivityDashboardBinding
    private lateinit var rootView: View
    private var lang=""
    private var token=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object{
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentHome {
            val fragment: FragmentHome =
                FragmentHome()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        rootView = binding.root

//        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        init()


        return rootView

//        lang = CommonFunctions.getPreference(activity, Constants.languageid, "0").toString()
//        val locale = Locale(if (lang.equals("0", ignoreCase = true)) "en" else "ar")
//        Locale.setDefault(locale)
//        val config = Configuration()
//        config.locale = locale
//        requireActivity().baseContext.resources.updateConfiguration(
//            config,
//            requireActivity().baseContext.resources.displayMetrics
//        )
//        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        bindingprofile = ActivityDashboardBinding.inflate(layoutInflater)
//        token = CommonFunctions.getPreference(context, Constants.api_token, "").toString()
//        rootView = bindingprofile.root
//        init()
//        return rootView



//
//        CommonFunctions.getPreference(context, Constants.profile_image, "")?.let {
//            loadBannerPhoto(bindingprofile.ivProfileImage, it)
//        }
//        bindingprofile.tvUsername.text = CommonFunctions.getPreference(context, Constants.name, "")
//        bindingprofile.tvEmail.text = CommonFunctions.getPreference(context, Constants.email, "")

//        profiledetail()







    }

    private fun init() {

//

        binding.cvCamel.setOnClickListener {

          (activity as DashboardActivity).FragmentCamel()

        }
        binding.cvUser.setOnClickListener {



            (activity as DashboardActivity).FragmentProfile()


        }
        binding.cvNotify.setOnClickListener {

            (activity as DashboardActivity).FragmentNotification()


        }
        binding.cvParticipant.setOnClickListener {

            (activity as DashboardActivity).FragmentParticipant()


        }
        binding.cvContolpanel.setOnClickListener {

            val intent= Intent(activity,AdminDashboardActivity::class.java)
            startActivity(intent)

           // (activity as DashboardActivity).FragmentControlPanel()

        }
        binding.cvArchive.setOnClickListener {

            (activity as DashboardActivity).fragmentRaceArchive()

        }
        binding.cvScedule.setOnClickListener {

            (activity as DashboardActivity).fragmentRaceSchedule()
        }
        binding.cvTerms.setOnClickListener {

            (activity as DashboardActivity).fragmentTermsCondition()
        }
//        bindingprofile.clLogout.setOnClickListener(View.OnClickListener {
//            CommonFunctions.setPreference(requireActivity(), Constants.isLogin, false)
//            SignInActivity.startActivity(requireActivity())
//        })
//
//        bindingprofile.clChangePassword.setOnClickListener {
//            ChangePasswordActivity.startActivity(requireActivity())
//        }
//        bindingprofile.clChangeLanguage.setOnClickListener {
//            setlanguage()
//        }







        // get reference to button
// set on-click listener
    }




}




