package com.bignerdranch.android.myapplication


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bignerdranch.android.myapplication.View.Fragments.FirstFrag

class ViewPagerAdapter(
    fm: FragmentActivity,
    per: ArrayList<ArrayList<ArrayList<String>>>
): FragmentStateAdapter(fm) {

    val per = per

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        val names = per[position][0]
        val nazvs = per[position][1]
        val mestos = per[position][2]
        val lessons_name = per[position][3]
        val fragmemnt = FirstFrag("12",names,nazvs,mestos,lessons_name,position)
        return fragmemnt
    }


}