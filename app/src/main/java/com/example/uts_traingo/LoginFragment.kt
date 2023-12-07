package com.example.uts_traingo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uts_traingo.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)

        if (isLoggedIn()) {
            val userRole = sharedPreferences.getString("userRole", "")
            when (userRole) {
                "admin" -> {
                    // Redirect ke halaman admin
                    // Contoh: requireActivity().supportFragmentManager.beginTransaction()...
                }
                "user" -> {
                    // Redirect ke halaman user
                    // Contoh: requireActivity().supportFragmentManager.beginTransaction()...
                }
                else -> {
                    // Peran pengguna tidak terdefinisi, tangani sesuai kebutuhan
                    Toast.makeText(
                        requireContext(),
                        "Role is not defined",
                        Toast.LENGTH_SHORT)
                }
            }

            // Contoh: requireActivity().supportFragmentManager.beginTransaction()...
        }

        with(binding) {
            signinButton.setOnClickListener {
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                // Login dengan Firebase
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Sign In Success",
                                Toast.LENGTH_SHORT
                            ).show()

                            val currentUser = firebaseAuth.currentUser
                            currentUser?.let {
                                firebaseFirestore.collection("users")
                                    .document(currentUser.uid)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        if (document != null && document.exists()) {
                                            val role = document.getString("role")
                                            if (role == "admin") {
                                                saveLoginStatus(true, "admin")
                                                // Redirect ke halaman admin
                                            } else {
                                                saveLoginStatus(true, "user")
                                                // Redirect ke halaman user
                                            }
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Email or Password is Incorrect",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun saveLoginStatus(isLoggedIn: Boolean, userRole: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("userRole", userRole)
        editor.apply()
    }
}