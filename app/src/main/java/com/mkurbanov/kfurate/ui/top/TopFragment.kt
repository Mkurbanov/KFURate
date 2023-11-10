package com.mkurbanov.kfurate.ui.top

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mkurbanov.kfurate.R
import com.mkurbanov.kfurate.data.config.TOKEN
import com.mkurbanov.kfurate.data.local.UserPreferencesRepository
import com.mkurbanov.kfurate.data.network.ApiHelper
import com.mkurbanov.kfurate.data.network.RetrofitBuilder
import com.mkurbanov.kfurate.data.repository.TopStudentsRepository
import com.mkurbanov.kfurate.dataStore
import com.mkurbanov.kfurate.databinding.FragmentTopBinding
import kotlinx.coroutines.launch

class TopFragment : Fragment() {

    private var _binding: FragmentTopBinding? = null
    private val binding get() = _binding!!
    private val repository:TopStudentsRepository by lazy { TopStudentsRepository(ApiHelper(RetrofitBuilder.apiService)) }
    private val topViewModel:TopViewModel by lazy { TopViewModelFactory(repository).create(TopViewModel::class.java) }
    private val prefRepository: UserPreferencesRepository by lazy { UserPreferencesRepository(requireContext().dataStore, requireContext()) }
    private lateinit var adapter:TopAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = TopAdapter(topViewModel.students.value!!)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        topViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
        }

        topViewModel.students.observe(viewLifecycleOwner) {
            adapter = TopAdapter(it)
            binding.recyclerView.adapter = adapter
        }

        binding.searchView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder
                .setMessage(getString(R.string.premium_message))
                .setTitle(getString(R.string.premium))
                .setPositiveButton(getString(R.string.buy)) { dialog, which ->
                    sendMail()
                }
                .setNegativeButton(getString(R.string.no_thanks)) { dialog, which ->
                    dialog.cancel()
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        return root
    }

    private fun sendMail(){
        lifecycleScope.launch {
            prefRepository.userPreferencesFlow.collect{
                var intent = Intent(Intent.ACTION_VIEW)
                val data: Uri = Uri.parse("mailto:?subject=" + getString(R.string.premium) + "&body=" + getString(R.string.i_want_buy_premium, it.phone) + "&to=" + "kfuwhich@gmail.com")
                intent.data = data
                startActivity(Intent.createChooser(intent, ""))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        topViewModel.getStudents(TOKEN)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}