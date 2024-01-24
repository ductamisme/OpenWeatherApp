package com.aicontent.openweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aicontent.openweather.databinding.FragmentProfileBinding
import com.aicontent.openweather.viewmodel.CityWeatherViewModel

class Profile : Fragment() {
    private lateinit var sheetLayoutBinding: FragmentProfileBinding
    private lateinit var viewModel: CityWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[CityWeatherViewModel::class.java]

        sheetLayoutBinding = FragmentProfileBinding.inflate(layoutInflater)
        viewModel.getCountry(
            context = requireContext(),
            binding = sheetLayoutBinding,
        )

        sheetLayoutBinding.recyclerViewCountry.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


}