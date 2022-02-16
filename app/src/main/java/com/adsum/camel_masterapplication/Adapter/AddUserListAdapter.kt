package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.AddUserListResponse
import com.adsum.camel_masterapplication.R


class AddUserListAdapter(
    var ctx: Context, val userList: ArrayList<AddUserListResponse.Data>,
    val checkedChangeListener: OnCheckedChangeListener
)
    : RecyclerView.Adapter<AddUserListAdapter.ViewHolder>() {



    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        var user_list: TextView = itemView.findViewById(R.id.tv_userList)
        var checkbox: ImageView = itemView.findViewById(R.id.checkbox)

        init {
          itemView.setOnClickListener {
              Toast.makeText(itemView.context, user_list.text, Toast.LENGTH_SHORT).show()
          }
        }
        fun bind(userList: AddUserListResponse.Data, position: Int, click: OnCheckedChangeListener)
        {
           checkbox.setOnClickListener{
               click.OnCheckedChangeListener(userList,position)
           }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var userlistdetail =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_users, parent, false)
        return ViewHolder(userlistdetail)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val userlist = userList[position]
        holder.user_list.text = userlist.nameOfParticipant

        checkedChangeListener.let { holder.bind(userlist,position,it) }


        if (userlist.ischecked){
            holder.checkbox.setBackgroundResource(R.drawable.checkbox_checked);
          //  holder.checkbox.isChecked = true
        }else{
            holder.checkbox.setBackgroundResource(R.drawable.unchecked);
        }
        holder.checkbox.setOnClickListener{
            userList[position].ischecked=!userList[position].ischecked
            notifyDataSetChanged()
        }
    }
    fun selectUser(tag: Int?) {
        for(i in 0..userList.size-1){
            if (tag==1){
                userList[i].ischecked=true
            }else{
                userList[i].ischecked=false
            }

        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    interface OnCheckedChangeListener{
        fun OnCheckedChangeListener(userList: AddUserListResponse.Data,position: Int)

    }



}







