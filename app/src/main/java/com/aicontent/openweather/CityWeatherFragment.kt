package com.aicontent.openweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aicontent.openweather.databinding.BottomSheetLayoutBinding
import com.aicontent.openweather.databinding.FragmentCityWeatherBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Locale

class CityWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCityWeatherBinding

    private lateinit var sheetLayoutBinding: BottomSheetLayoutBinding

    private lateinit var dialog: BottomSheetDialog

    private var city: String = "ha noi"

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var pollutionFragment: PollutionFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCityWeatherBinding.inflate(layoutInflater)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val viewModel = ViewModelProvider(this)[CityWeatherViewModel::class.java]
        val navController = findNavController()

        pollutionFragment = PollutionFragment()

        fetchLocation( viewModel = viewModel)
        viewModel.getCurrentWeather(
            context = requireContext(),
            binding = binding,
            city = city,
        )
        // Initialize sheetLayoutBinding here
        sheetLayoutBinding = BottomSheetLayoutBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
        dialog.setContentView(sheetLayoutBinding.root)

        binding.tvForecast.setOnClickListener {
            openDialog(viewModel = viewModel)
        }

        binding.tvLocation.setOnClickListener {
            fetchLocation( viewModel = viewModel)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    city = query
                }
                viewModel.getCurrentWeather(
                    context = requireContext(),
                    binding = binding,
                    city = city,
                )
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun fetchLocation(viewModel: CityWeatherViewModel) {
        val task : Task<Location> =             fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        task.addOnSuccessListener {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                geocoder.getFromLocation(it.latitude,it.longitude,1
                ) { addresses -> city = addresses[0].locality }
            }else{
                val address = geocoder.getFromLocation(it.latitude, it.longitude, 1) as List<Address>

                city = address[0].locality
            }

            viewModel.getCurrentWeather(
                context = requireContext(),
                binding = binding,
                city = city,
                units = "metric",
                apiKey = "27063406787b538a94f3b5e032daf388"
            )
        }
    }

    private fun openDialog(viewModel: CityWeatherViewModel) {
        viewModel.getForecast(
            context = requireContext(),
            binding = sheetLayoutBinding,
            city = city,
        )

        sheetLayoutBinding.recyclerViewForecast.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }

}