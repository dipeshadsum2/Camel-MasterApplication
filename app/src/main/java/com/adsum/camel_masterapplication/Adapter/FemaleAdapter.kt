package com.adsum.camel_masterapplication.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.CampleResp
import com.adsum.camel_masterapplication.Model.MaleResponse
import com.adsum.camel_masterapplication.R

import com.adsum.camel_masterapplication.databinding.ItemFemaleBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FemaleAdapter(var ctx: Context, val femalelist: ArrayList<CampleResp.Data>, val strokClickListener: FemaleAdapter.OnfedeleteClickListener) : RecyclerView.Adapter<FemaleAdapter.ViewHolder>() {
    private lateinit var itemFemaleBinding: ItemFemaleBinding
    //this method is returning the view for each item in the list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FemaleAdapter.ViewHolder {
        itemFemaleBinding = ItemFemaleBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(itemFemaleBinding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: FemaleAdapter.ViewHolder, position: Int) {
        try{
            val female = femalelist[position]

            loadBannerPhoto(itemFemaleBinding.ivDelete, "")
            itemFemaleBinding.tvFesubname.text = female.rcCamel
            itemFemaleBinding.tvFetypeCamel.text = female.rcId
            strokClickListener.let { holder.bind(female, position, it) }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun loadBannerPhoto(imageView: ImageView, image: String?) {
        Glide.with(ctx)
            .load(image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_baseline_delete_24)
            )
            .into(imageView)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return femalelist.size
    }

    inner class ViewHolder internal constructor(binding: ItemFemaleBinding): RecyclerView.ViewHolder(
        binding.root
    ){
        fun bind(
            femalelist: CampleResp.Data,
            position: Int,
            clickListener: OnfedeleteClickListener
        ) {

            itemFemaleBinding.ivDelete.setOnClickListener {
                clickListener.OndeleteClick(femalelist, position)
            }
        }


    }

    interface OnfedeleteClickListener {
        fun OndeleteClick(malelist: CampleResp.Data, position: Int)

    }
}

