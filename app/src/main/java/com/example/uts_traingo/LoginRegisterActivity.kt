package com.example.uts_traingo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.uts_traingo.databinding.ActivityLoginRegisterBinding
import com.google.android.material.tabs.TabLayoutMediator


class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    companion object{
        lateinit var viewPager2 : ViewPager2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            viewPager.adapter = TabAdapter(this@LoginRegisterActivity)
            viewPager2 = viewPager

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> ""
                }
            }.attach()
        }
    }

    private class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }
    }
}