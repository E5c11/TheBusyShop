package com.ikhokha.techcheck.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.ikhokha.techcheck.R
import com.ikhokha.techcheck.data.entities.Product
import com.ikhokha.techcheck.databinding.HomeFragmentBinding
import com.ikhokha.techcheck.utils.adapters.ProductAdapter
import com.ikhokha.techcheck.utils.exhaustive
import com.ikhokha.techcheck.utils.getRotateAnimation
import com.ikhokha.techcheck.viewmodels.HomeViewModel
import com.ikhokha.techcheck.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment), ProductAdapter.OnItemClickedListener {

    private lateinit var binding : HomeFragmentBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    @Inject lateinit var fbStorage: FirebaseStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)

        checkLogin()
        setListeners()
        setProducts()
        Log.d("myT", "Home: ")
    }

    private fun checkLogin() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.homeEvent.collect { event ->
                when (event) {
                    is HomeViewModel.HomeEvents.LoggedInEvent -> {
                        binding.apply {
                            progress.visibility = View.GONE
                            login.visibility = View.GONE
                        }
                        observeProducts()
                        Snackbar.make(requireView(), "Login success", Snackbar.LENGTH_SHORT).show()
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

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner) {
                productAdapter.submitList(it)
            Log.d("myT", "observeProducts: $it")
        }
    }

    private fun setProducts() {
        binding.productsRecycler.apply {
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false).apply {
                layoutManager = this
                productAdapter = ProductAdapter(this@HomeFragment, requireActivity().application, fbStorage)
                adapter = productAdapter
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            login.setOnClickListener { findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment()) }
            basket.setOnClickListener { findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToBasketDialogFragment()) }
        }
    }

    override fun onItemClick(product: Product) {
        viewModel.insertItem(product)
    }

}