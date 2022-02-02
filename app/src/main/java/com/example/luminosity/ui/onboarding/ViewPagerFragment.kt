package com.example.luminosity.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.luminosity.R
import com.example.luminosity.databinding.FragmentFirstBinding
import com.example.luminosity.databinding.FragmentViewPagerBinding
import com.example.scopedstorage.utils.ViewBindingFragment
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class ViewPagerFragment :  ViewBindingFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager
        val dotsIndicator = binding.dotsIndicator
        val transformer = Transformation()

        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter
        dotsIndicator.setViewPager2(viewPager)
        viewPager.setPageTransformer(transformer)

    }

}