package com.adsum.camel_masterapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adsum.camel_masterapplication.R
import android.widget.DatePicker

import androidx.fragment.app.DialogFragment
import com.adsum.camel_masterapplication.databinding.FragmentSelectDateDialogBinding
import android.app.DatePickerDialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog.*
import android.app.Dialog
import androidx.fragment.app.FragmentActivity
import com.adsum.camel_masterapplication.databinding.FragmentUpdateDateBinding
import com.bumptech.glide.manager.SupportRequestManagerFragment
import java.text.SimpleDateFormat
import java.util.*


class SelectDateDialogFragment : DialogFragment(),DatePickerDialog.OnDateSetListener {
    private  var selectDateDialogBinding: FragmentSelectDateDialogBinding? =null
    val cal = Calendar.getInstance()
    private  var updateDateBinding: FragmentUpdateDateBinding? =null




    companion object {

        fun newInstance(): SelectDateDialogFragment {
            val fragment: SelectDateDialogFragment =
                SelectDateDialogFragment()
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

        // Inflate the layout for this fragment
        var rootView:View= inflater.inflate(R.layout.fragment_select_date_dialog, container, false)
        return rootView
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(requireView(), savedInstanceState)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), AlertDialog.THEME_HOLO_LIGHT,this, year, month, day)
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)



        updateDateInView()


    }
    private fun updateDateInView() {
        val myFormat = "MM-dd-yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        updateDateBinding?.tvStartDateTime?.text = sdf.format(cal.getTime())
    }

//    class DatePickerFragment :DialogFragment() {
//        var ondateSet: OnDateSetListener? = null
//        private var year = 0
//        private var month:Int = 0
//        private  var day:Int = 0
//        fun DatePickerFragment() {}
//
//        fun setCallBack(ondate: OnDateSetListener?) {
//            ondateSet = ondate
//        }
//    }



}