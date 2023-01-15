package com.bignerdranch.android.myapplication.View.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.myapplication.R
import com.bignerdranch.android.myapplication.TagAdapter

class CustomDialogFragment(tags: ArrayList<String>,point:Int, private var listener: ReturnData): DialogFragment(),
    TagAdapter.OnAdapterClickListener {
    val tags_ = tags
    val point_ = point
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view_:View = inflater.inflate(R.layout.activity_list_view, container, false)
        tags_.sort()
        //настройка адаптера
        var recyclerView:RecyclerView = view_.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = TagAdapter(requireContext(), tags_,this)
        recyclerView.adapter = adapter
        return view_
    }

    override fun onItemClick(pos: Int, numb: String) {
            val param: FragmentActivity? = activity
            listener?.ReturnDataFin(numb)
        Toast.makeText(context, numb, Toast.LENGTH_SHORT).show()
        dialog?.dismiss()
    }
    interface ReturnData{
        fun ReturnDataFin(s:String)
    }


}