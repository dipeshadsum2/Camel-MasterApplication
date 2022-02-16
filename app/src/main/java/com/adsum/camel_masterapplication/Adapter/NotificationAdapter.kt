package com.adsum.camel_masterapplication.Adapter

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.NotificationResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemNotificationBinding

class NotificationAdapter (var ctx: Context, var list: List<NotificationResponse.Data>, val notificationClickListener: OnNotificationClickListener) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

        private lateinit var itemNotificationBinding:ItemNotificationBinding



    inner class ViewHolder internal constructor(itemNotificationBinding: ItemNotificationBinding): RecyclerView.ViewHolder(itemNotificationBinding.root){

        fun bind(notification: NotificationResponse.Data, position: Int , clickListener: OnNotificationClickListener)
        {

            itemView.setOnClickListener {
                clickListener.onNotificationClick(notification, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        itemNotificationBinding =
            ItemNotificationBinding.inflate(LayoutInflater.from(ctx), parent, false)

        return ViewHolder(itemNotificationBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = list[position]
        var des=notification.notificationDsc

        itemNotificationBinding.tvTitle.text = notification.notifiTitle

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemNotificationBinding.tvDecription.setText(Html.fromHtml(notification.notificationDsc, Html.FROM_HTML_MODE_COMPACT))
        }else{
            itemNotificationBinding.tvDecription.setText(Html.fromHtml(notification.notificationDsc))
        }
       // itemNotificationBinding.tvDecription.text = notification.notificationDsc

        notificationClickListener.let { holder.bind(notification, position, it) }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface OnNotificationClickListener {
        fun onNotificationClick(notification: NotificationResponse.Data, position: Int)

    }
}