package com.aicontent.openweather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.aicontent.openweather.databinding.ActivityLoginSignUpBinding
import com.aicontent.openweather.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        val firebaseAuth = FirebaseAuth.getInstance()
        val navController = findNavController()

        binding.tvDoNotHaveAccount.setOnClickListener {
            navController.navigate(R.id.action_loginFragment3_to_signUpFragment3)
        }

        binding.btLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(SignUpActivity.TAG, "signInWithEmail:success")
                        val user = firebaseAuth.currentUser
                        // Navigate to MainActivity after successful login
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(SignUpActivity.TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
        return binding.root
    }

}