package com.example.luminosity.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.luminosity.databinding.FragmentSplashBinding
import com.example.scopedstorage.utils.ViewBindingFragment

class SplashFragment: ViewBindingFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate)  {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            if (isOnboardingFinished()) {
                val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(action)
            } else {
                val action = SplashFragmentDirections.actionSplashFragmentToViewPagerFragment()
                findNavController().navigate(action)
            }
        }, 2000)
    }

    private fun isOnboardingFinished(): Boolean {
        val sharedPrefs = requireActivity().getSharedPreferences("onboarding", Context.MODE_PRIVATE)
            return sharedPrefs.getBoolean("Finished", false)
    }
}