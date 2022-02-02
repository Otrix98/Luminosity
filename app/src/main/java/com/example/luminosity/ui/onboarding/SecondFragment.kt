package com.example.luminosity.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.luminosity.R
import com.example.luminosity.databinding.FragmentSecondBinding
import com.example.scopedstorage.utils.ViewBindingFragment

class SecondFragment: ViewBindingFragment<FragmentSecondBinding>(FragmentSecondBinding::inflate)  {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.next2.setOnClickListener {
            viewPager?.currentItem = 2
        }
    }
}