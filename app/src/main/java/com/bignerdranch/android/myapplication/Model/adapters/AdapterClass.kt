package com.bignerdranch.android.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

class TagAdapter(var context: Context, private val tags: ArrayList<String>, private var listener: OnAdapterClickListener) :   RecyclerView.Adapter<TagAdapter.MyViewHolder>(){

    inner class MyViewHolder(view:View, private val listener: OnAdapterClickListener?, mainView: View):RecyclerView.ViewHolder(view),View.OnClickListener{
        val Teachers_name: TextView = view.findViewById<View>(R.id.Teachers_name) as TextView
        val kuf: TextView = view.findViewById<View>(R.id.kuf) as TextView
        val UserName:TextView = mainView.findViewById<View>(R.id.User_name) as TextView
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener?.onItemClick(adapterPosition, Teachers_name.text as String)
            UserName.text = Teachers_name.text
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.teachers_list, parent, false)
        val MainView = LayoutInflater.from(parent.context).inflate(R.layout.activity_second, parent, false)
        return MyViewHolder(itemView, listener, MainView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: String = tags[position]
        holder.Teachers_name.text = item
        holder.kuf.text = (position + 1).toString()
        holder.kuf.textSize = 16F
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    interface OnAdapterClickListener{
        fun onItemClick(pos: Int, numb: String)

    }






}

