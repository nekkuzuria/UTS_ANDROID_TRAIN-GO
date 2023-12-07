package com.example.uts_traingo

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter  (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    val page = arrayOf<Fragment>(LoginFragment(), RegisterFragment())
    override fun getItemCount(): Int {
        return page.size
    }
    override fun createFragment(position: Int): Fragment {
        return page[position]
    }

}
