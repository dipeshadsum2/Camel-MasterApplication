package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.AkbarResp
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemMaleBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.Locale.filter

class MaleAdapter(var ctx: Context, val malelist: ArrayList<AkbarResp.Data>, val strokClickListener: OndeleteClickListener) : RecyclerView.Adapter<MaleAdapter.ViewHolder>() {
    private lateinit var itemMaleBinding: ItemMaleBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaleAdapter.ViewHolder {
        itemMaleBinding = ItemMaleBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(itemMaleBinding)
    }

    override fun onBindViewHolder(holder: MaleAdapter.ViewHolder, position: Int) {
        try{
            val malelist = malelist[position]
            loadBannerPhoto(itemMaleBinding.ivDelete, "")
            itemMaleBinding.tvMsubname.text = malelist.rcCamel
            itemMaleBinding.tvMtypeCamel.text = malelist.rcId
            strokClickListener.let { holder.bind(malelist, position, it) }

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
    override fun getItemCount(): Int {
        return malelist.size
    }
    inner class ViewHolder internal constructor(binding: ItemMaleBinding): RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            malelist: AkbarResp.Data,
            position: Int,
            clickListener: OndeleteClickListener
        ) {
            itemMaleBinding.ivDelete.setOnClickListener {
                clickListener.OndeleteClick(malelist, position)
            }
        }
    }
    fun DeleteMaleCamel(position: Int){
        malelist.removeAt(position)
        notifyDataSetChanged()
    }
    interface OndeleteClickListener {
        fun OndeleteClick(malelist: AkbarResp.Data, position: Int)
    }
}