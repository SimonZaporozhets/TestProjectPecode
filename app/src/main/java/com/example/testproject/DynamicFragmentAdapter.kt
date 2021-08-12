package com.example.testproject

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class DynamicFragmentAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val fragments: ArrayList<Fragment> =
        arrayListOf(DynamicFragment())

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun updateList(size: Int) {
        for (x in 2..size) {
            fragments.add(DynamicFragment())
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = fragments.size

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(itemCount - 1)
    }

    fun removeFragment(): Int {
        val lastIndex = fragments.size - 1
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
        return lastIndex
    }
}