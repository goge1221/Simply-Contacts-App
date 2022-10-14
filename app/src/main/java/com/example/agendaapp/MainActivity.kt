package com.example.agendaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.agendaapp.databinding.ActivityMainBinding
import com.example.agendaapp.viewPagerAdapter.ViewPager2Adapter
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    
    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.apply {
            viewPager.adapter = ViewPager2Adapter(this@MainActivity, 2)
        }
        addOnTabSelectedListener()
        addOnViewPagerChangedListener()
    }

    private fun addOnTabSelectedListener(){
        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding?.viewPager?.currentItem = tab!!.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


    private fun addOnViewPagerChangedListener(){
        binding?.viewPager?.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding?.tabLayout?.getTabAt(position)?.select()
            }
        })
    }



}