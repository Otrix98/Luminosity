package com.example.luminosity.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.luminosity.R
import com.example.luminosity.data.AccessToken
import com.example.luminosity.databinding.FragmentLoginBinding
import com.example.luminosity.databinding.FragmentThirdBinding
import com.example.luminosity.ui.onboarding.ViewPagerFragmentDirections
import com.example.luminosity.ui.viewmodels.AuthViewModel
import com.example.scopedstorage.utils.ViewBindingFragment
import com.skillbox.github.utils.toast
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

class LoginFragment: ViewBindingFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkToken()) {
            val action = LoginFragmentDirections.actionLoginFragmentToFeedFragment()
            findNavController().navigate(action)
        } else {
        bindViewModel()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTH_REQUEST_CODE && data != null) {
            val tokenExchangeRequest = AuthorizationResponse.fromIntent(data)
                ?.createTokenExchangeRequest()
            val exception = AuthorizationException.fromIntent(data)
            when {
                tokenExchangeRequest != null && exception == null ->
                    viewModel.onAuthCodeReceived(tokenExchangeRequest)
                exception != null -> viewModel.onAuthCodeFailed(exception)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                val tokenExchangeRequest = AuthorizationResponse.fromIntent(it.data!!)
                    ?.createTokenExchangeRequest()
                val exception = AuthorizationException.fromIntent(it.data)
                when {
                    tokenExchangeRequest != null && exception == null ->
                        viewModel.onAuthCodeReceived(tokenExchangeRequest)
                    exception != null -> viewModel.onAuthCodeFailed(exception)
                }
            } else {
                toast(R.string.authorization_error)
            }
            }


    private fun bindViewModel() {
        binding.loginButton.setOnClickListener { viewModel.openLoginPage() }
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
        viewModel.openAuthPageLiveData.observe(viewLifecycleOwner, ::openAuthPage)
        viewModel.toastLiveData.observe(viewLifecycleOwner, ::toast)
        viewModel.authSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedFragment())
        }
    }

    private fun checkToken(): Boolean {
        val token = context?.getSharedPreferences(AccessToken.TOKEN_PREFERENCES, Context.MODE_PRIVATE)
            ?.getString(AccessToken.TOKEN_STRING, null)
        return if (token != null) {
            Log.d("TOKEN", "Token ---> $token")
            AccessToken.value = token
            true
        } else false
    }

    private fun updateIsLoading(isLoading: Boolean) {
        binding.loginButton.isVisible = !isLoading
        binding.loginProgress.isVisible = isLoading
    }

    private fun openAuthPage(intent: Intent) {
        getResult.launch(intent)
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 342
    }
    }