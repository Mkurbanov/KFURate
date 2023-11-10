package com.mkurbanov.kfurate.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.mkurbanov.kfurate.data.config.TOKEN
import com.mkurbanov.kfurate.data.network.ApiHelper
import com.mkurbanov.kfurate.data.network.RetrofitBuilder
import com.mkurbanov.kfurate.data.repository.MainRepository
import com.mkurbanov.kfurate.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainRepository by lazy { MainRepository(ApiHelper(RetrofitBuilder.apiService)) }
    private val homeViewModel by lazy {
        HomeViewModelFactory(
            mainRepository
        ).create(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val width: Int = resources.displayMetrics.widthPixels
        val height: Int = resources.displayMetrics.heightPixels

        binding.apply {
            imageView.layoutParams.width = width / 2
            imageView.layoutParams.height = height / 3
            imageView2.layoutParams.width = width / 2
            imageView2.layoutParams.height = height / 3
            imageView.requestLayout()
            imageView2.requestLayout()
        }

        homeViewModel.students.observe(viewLifecycleOwner) {
            binding.apply {
                cardview.visibility = View.VISIBLE
                cardview2.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }

            val link1: String = it.students[0].image + width / 2 + "x" + height / 3
            val link2: String = it.students[1].image + width / 2 + "x" + height / 3

            binding.imageView.load(link1) {
                crossfade(true)
                listener(onStart = {
                    binding.imageView.visibility = View.INVISIBLE
                    binding.progressBar1.visibility = View.VISIBLE
                }, onSuccess = { request, result ->
                    binding.imageView.visibility = View.VISIBLE
                    binding.progressBar1.visibility = View.GONE
                })
            }

            binding.imageView2.load(link2) {
                crossfade(true)
                listener(onStart = {
                    binding.imageView2.visibility = View.INVISIBLE
                    binding.progressBar2.visibility = View.VISIBLE
                }, onSuccess = { request, result ->
                    binding.imageView2.visibility = View.VISIBLE
                    binding.progressBar2.visibility = View.GONE
                })
            }

        }

        homeViewModel.likedStatus.observe(viewLifecycleOwner) {
            loadData()
        }

        homeViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
        }


        binding.imageView.setOnClickListener {
            homeViewModel.postLike(TOKEN, 0)
        }

        binding.imageView2.setOnClickListener {
            if (!homeViewModel.students.value?.students.isNullOrEmpty())
                homeViewModel.postLike(TOKEN, 1)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadData()
        super.onViewCreated(view, savedInstanceState)
    }

    fun loadData() {
        binding.apply {
            cardview.visibility = View.INVISIBLE
            cardview2.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
        homeViewModel.getStudents(TOKEN)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}