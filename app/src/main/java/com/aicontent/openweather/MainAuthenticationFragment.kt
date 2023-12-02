package com.aicontent.openweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.aicontent.openweather.databinding.ActivityLoginSignUpBinding
import com.aicontent.openweather.databinding.FragmentMainAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth

class MainAuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentMainAuthenticationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainAuthenticationBinding.inflate(layoutInflater)

        val navController = findNavController()

        // Set up the click listener after initializing binding
        binding.btLogin.setOnClickListener {
            navController.navigate(R.id.action_mainAuthenticationFragment_to_loginFragment3)
        }
        binding.btSignUp.setOnClickListener {
            navController.navigate(R.id.action_mainAuthenticationFragment_to_signUpFragment3)
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}