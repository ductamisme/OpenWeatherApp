package com.aicontent.openweather

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aicontent.openweather.databinding.ActivityMainBinding
import com.aicontent.openweather.search.Search
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var broadcast = BroadcastReceiverAirPlane()

    private val connectivityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ("com.example.CONNECTIVITY_CHANGE" == intent?.action) {
                val isConnected = intent.getBooleanExtra("is_connected", false)

                if (isConnected) {
                    // Handle online state
                    Toast.makeText(this@MainActivity, "Online", Toast.LENGTH_LONG).show()
                } else {
                    // Handle offline state
                    Toast.makeText(this@MainActivity, "Offline", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Register the receiver dynamically in onCreate
        registerReceiver(
            connectivityReceiver,
            IntentFilter("com.example.CONNECTIVITY_CHANGE")
        )
//        val filter = IntentFilter()
//        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
//        this.registerReceiver(broadcast, filter)

        replaceFragment(CityWeatherFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(CityWeatherFragment())
                R.id.profile -> replaceFragment(Profile())
                R.id.settings -> replaceFragment(Search())
//                R.id.settings -> {
//                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("http://tuhoc.cc"))
//                    startActivity(i)
//                }
                else -> {
                }
            }
            true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver in onDestroy to avoid memory leaks
        unregisterReceiver(connectivityReceiver)
    }

    override fun onStart() {
        super.onStart()
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            // No user is signed in
        }
    }

//    private fun checkCurrentUser() {
//        // [START check_current_user]
//        val user = Firebase.auth.currentUser
//        if (user != null) {
//            // User is signed in
//        } else {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//            // No user is signed in
//        }
//        // [END check_current_user]
//    }

    private fun getCurrentUser() {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            val photoUrl = it.photoUrl

            // Check if user's email is verified
            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }
    }

    private fun getProfilerData() {
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }

    private fun updateProfile() {
        val user = Firebase.auth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = "Jane Q. User"
            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG.TAG, "User profile updated.")
                }
            }
    }

    private fun updateEmail() {
        val user = Firebase.auth.currentUser

        user!!.updateEmail("user@example.com")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG.TAG, "User email address updated.")
                }
            }
    }

    private fun sendEmailVerification() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG.TAG, "Email sent.")
                }
            }
    }

    private fun updatePassword() {

        val user = Firebase.auth.currentUser
        val newPassword = "SOME-SECURE-PASSWORD"

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG.TAG, "User password updated.")
                }
            }
    }

    private fun sendPasswordToResetEmail() {
        val emailAddress = "user@example.com"

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG.TAG, "Email sent.")
                }
            }
    }

    private fun deleteAccount() {
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG.TAG, "User account deleted.")
                }
            }
    }

    private fun reAuthenticate() {
        val user = Firebase.auth.currentUser!!

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { Log.d(TAG.TAG, "User re-authenticated.") }
    }

    private fun signOut() {
        // [START auth_sign_out]
        Firebase.auth.signOut()
        // [END auth_sign_out]
    }

    companion object TAG {
        private const val TAG = "MainActivity"
    }

     private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}