package com.aicontent.openweather.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.aicontent.openweather.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ViewModelLoginScreen : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private fun updateUI(user: FirebaseUser?) {
    }

}