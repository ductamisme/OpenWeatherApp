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
import com.aicontent.openweather.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        val firebaseAuth = FirebaseAuth.getInstance()
        val navController = findNavController()

        binding.btSignUp.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val passwordConfirm = binding.etPasswordConfirm.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && passwordConfirm.isNotEmpty() && username.isNotEmpty()) {
                if (password == passwordConfirm) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = firebaseAuth.currentUser

                                // Update user profile with display name
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { profileUpdateTask ->
                                        if (profileUpdateTask.isSuccessful) {
                                            Log.d(SignUpActivity.TAG, "User profile updated.")
                                        }
                                    }
                                navController.navigate(R.id.action_signUpFragment3_to_loginFragment3)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(SignUpActivity.TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    context,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Passwords do not match.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }
    companion object {
        const val TAG = "EmailPassword"
    }
}