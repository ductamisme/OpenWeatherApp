package com.aicontent.openweather.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.aicontent.openweather.adapter.SearchAdapter
import com.aicontent.openweather.databinding.FragmentSearchBinding
import com.aicontent.openweather.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class Search : Fragment() {
    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        // Recyclerview
        viewModel.readAllData.value?.forEach { city ->
            viewModel.getCurrentWeather(
                applicationContext = requireContext(), // Use applicationContext instead of Context
                binding = binding,
                city = city.name,
            )
        }

        val recyclerView = binding.recyclerViewForecast
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
}