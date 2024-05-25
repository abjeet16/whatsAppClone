package com.example.whatsappclone.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(private val context:Context,
                       fm:FragmentManager,
                       private val list:ArrayList<Fragment>)
    :FragmentPagerAdapter(fm){
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TabTitles[position]
    }
    companion object{
        val TabTitles = arrayOf("Chats","Status","Call")
    }
}