package com.ikhokha.techcheck.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.databinding.HomeFragmentBinding
import com.ikhokha.techcheck.utils.exhaustive
import com.ikhokha.techcheck.utils.getRotateAnimation
import com.ikhokha.techcheck.viewmodels.HomeViewModel
import com.ikhokha.techcheck.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {

    private lateinit var binding : HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)

        checkLogin()
        setListeners()
        Log.d("myT", "Home: ")
    }

    private fun checkLogin() {
        binding.progress.animation = getRotateAnimation()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect { event ->
                when (event) {
                    is HomeViewModel.HomeEvents.LoggedInEvent -> {
                        binding.apply {
                            progress.animation = null
                            progress.visibility = View.GONE
                            login.visibility = View.GONE
                        }
                        Snackbar.make(requireView(), "Login success", Snackbar.LENGTH_LONG).show()
                    }
                    HomeViewModel.HomeEvents.NotLoggedInEvent -> {
                        Log.d("myT", "checkLogin: not logged in")
                        findNavController()
                            .navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                    }

                }.exhaustive
            }
        }
    }

    private fun setListeners() {
        binding.login.setOnClickListener { findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment()) }

    }

}