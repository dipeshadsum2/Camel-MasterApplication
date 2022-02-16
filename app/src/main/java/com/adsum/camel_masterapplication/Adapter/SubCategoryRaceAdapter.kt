package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.MixModel
import com.adsum.camel_masterapplication.databinding.ItemRaceSubcategoryBinding


class SubCategoryRaceAdapter(var ctx: Context, val subcategory: ArrayList<MixModel>, val stroksubClickListener: OnsubdeleteClickListener, val from: String) : RecyclerView.Adapter<SubCategoryRaceAdapter.ViewHolder>() {
    private lateinit var itemRaceSubcategoryBinding: ItemRaceSubcategoryBinding

    inner class ViewHolder internal constructor(itemRaceSubcategoryBinding: ItemRaceSubcategoryBinding): RecyclerView.ViewHolder(itemRaceSubcategoryBinding.root)
    {

        fun bind(subcategory: MixModel, position: Int, clickListener: OnsubdeleteClickListener) {
            itemRaceSubcategoryBinding.btnDelete.setOnClickListener {
                clickListener.OndeleteClick(subcategory, position)
            }
        }


    }
    interface OnsubdeleteClickListener {
        fun OndeleteClick(subcategory: MixModel, position: Int)

    }
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryRaceAdapter.ViewHolder {
        itemRaceSubcategoryBinding = ItemRaceSubcategoryBinding.inflate(LayoutInflater.from(ctx), parent, false)

        return ViewHolder(itemRaceSubcategoryBinding)
    }


    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return subcategory.size
    }

    override fun onBindViewHolder(holder: SubCategoryRaceAdapter.ViewHolder, position: Int) {
        try {

            val subcategory = subcategory[position]
            itemRaceSubcategoryBinding.tvSubSex.text = subcategory.rcGender
            itemRaceSubcategoryBinding.tvSubCustomization.text = subcategory.customization
            itemRaceSubcategoryBinding.tvSubPrize.text = subcategory.description
            itemRaceSubcategoryBinding.tvSubCname.text=subcategory.rc_camel
            itemRaceSubcategoryBinding.tvSubDetails.text=subcategory.name_of_participant



            if(from.equals(Constants.isFromHistory))
            {
                itemRaceSubcategoryBinding.btnDelete.visibility=View.INVISIBLE
            }
            else

            {
                    if (CommonFunctions.getPreference(ctx, Constants.ID, 0).toString() == subcategory.userId){
                        itemRaceSubcategoryBinding.btnDelete.visibility=View.VISIBLE
                    } else{
                        itemRaceSubcategoryBinding.btnDelete.visibility=View.INVISIBLE
                    }
            }


            stroksubClickListener.let { holder.bind(subcategory, position, it) }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}