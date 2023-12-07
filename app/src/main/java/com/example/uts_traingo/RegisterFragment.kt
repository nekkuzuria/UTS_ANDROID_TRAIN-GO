package com.example.uts_traingo

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uts_traingo.databinding.ActivityLoginRegisterBinding
import com.example.uts_traingo.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.concurrent.Executor
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private val executor: Executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        with(binding) {
            var adult = false
            var age = 0

//            Edit Date=======================================
            birthofdateInput.setOnClickListener(){
                val c = Calendar.getInstance()
                val today = Calendar.getInstance()

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    {view, selectedYear, monthOfYear, dayOfMonth ->
                        val date = (dayOfMonth.toString() + "-" + (monthOfYear+1)
                                + "-" + selectedYear)
                        birthofdateInput.setText(date)

                        age = today.get(Calendar.YEAR) - selectedYear
                        // Periksa apakah tanggal ulang tahun sudah terjadi pada tahun ini
                        if (today.get(Calendar.MONTH) < monthOfYear || (today.get(Calendar.MONTH) == monthOfYear && today.get(Calendar.DAY_OF_MONTH) < dayOfMonth)) {
                            age--
                        }
                        if (age >= 15) {
                            adult = true
                        }

                    },
                    year, month, day
                )

                datePickerDialog.show()
            }

//          Button SignUp========================================
            signupButton.setOnClickListener(){
                if(emailInput.text.toString().isEmpty() ||
                    usernameInput.text.toString().isEmpty() ||
                    passwordInput.text.toString().isEmpty() ||
                    confirmPasswordInput.text.toString().isEmpty() ||
                    birthofdateInput.text.toString().isEmpty()){
                    Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }else if(!emailInput.text.toString().contains("@")){
                    Toast.makeText(requireContext(), "Please insert a valid email", Toast.LENGTH_SHORT).show()
                }else if(passwordInput.text.length < 8){
                    Toast.makeText(requireContext(), "Please fill minimal 8 characters password", Toast.LENGTH_SHORT).show()
                }else if(confirmPasswordInput.text.toString() != passwordInput.text.toString()){
                    Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
                }
                else if(age<0){
                    Toast.makeText(requireContext(), "Please insert a valid birth of date", Toast.LENGTH_SHORT).show()
                }
                else if(adult){
                    if(confirmPasswordInput.text.toString() == passwordInput.text.toString()) {
                        val email = emailInput.text.toString().trim()
                        val username = usernameInput.text.toString().trim()
                        val password = passwordInput.text.toString().trim()
                        val birthOfDate = birthofdateInput.text.toString().trim()

                        registerUser(email, username, password, birthOfDate)
                    }

                } else {
                    Toast.makeText(requireContext(), "Sorry, age must be 15 or older", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    fun registerUser(email: String, username: String, password: String, birthOfDate: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseFirestore = FirebaseFirestore.getInstance()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser
                    val user = HashMap<String, Any>()
                    user["email"] = email
                    user["username"] = username
                    user["birthOfDate"] = birthOfDate
                    user["role"] = "user"

                    if (currentUser != null) {
                        executor.execute {
                            firebaseFirestore.collection("users")
                                .document(currentUser.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign Up Success",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    LoginRegisterActivity.viewPager2.setCurrentItem(0, true)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Sign Up Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Email already exists",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
}