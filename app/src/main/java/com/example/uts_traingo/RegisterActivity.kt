package com.example.uts_traingo

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uts_traingo.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    companion object {
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_PASSWORD = "extra_password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding){
            var adult = false

//            Edit Date=======================================
            birthofdateInput.setOnClickListener(){
                val c = Calendar.getInstance()
                val today = Calendar.getInstance()

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@RegisterActivity,
                    {view, selectedYear, monthOfYear, dayOfMonth ->
                        val date = (dayOfMonth.toString() + "-" + (monthOfYear+1)
                                + "-" + selectedYear)
                        birthofdateInput.setText(date)

                        var age = today.get(Calendar.YEAR) - selectedYear
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
                    birthofdateInput.text.toString().isEmpty()){
                    Toast.makeText(this@RegisterActivity, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }else if(adult){
                    val intentToLogin = Intent(this@RegisterActivity, LoginActivity::class.java)
                    intentToLogin.putExtra(EXTRA_EMAIL, emailInput.text.toString())
                    intentToLogin.putExtra(EXTRA_USERNAME, usernameInput.text.toString())
                    intentToLogin.putExtra(EXTRA_PASSWORD, passwordInput.text.toString())
                    Toast.makeText(this@RegisterActivity, "Register Success", Toast.LENGTH_SHORT).show()
                    startActivity(intentToLogin)
                    finish()

                } else {
                    Toast.makeText(this@RegisterActivity, "Sorry, age must be 15 or older", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}