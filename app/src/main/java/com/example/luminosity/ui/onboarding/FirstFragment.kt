package com.example.luminosity.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.luminosity.R
import com.example.luminosity.databinding.FragmentFeedBinding
import com.example.luminosity.databinding.FragmentFirstBinding
import com.example.scopedstorage.utils.ViewBindingFragment

class FirstFragment: ViewBindingFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate)  {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.next.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }
}