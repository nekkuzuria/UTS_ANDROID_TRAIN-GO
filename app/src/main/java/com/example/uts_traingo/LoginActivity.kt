package com.example.uts_traingo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.uts_traingo.databinding.ActivityLoginBinding
import com.example.uts_traingo.databinding.ActivityRegisterBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
//            catch the extra string
            val email = intent.getStringExtra(RegisterActivity.EXTRA_EMAIL)
            val username = intent.getStringExtra(RegisterActivity.EXTRA_USERNAME)
            val password = intent.getStringExtra(RegisterActivity.EXTRA_PASSWORD)

//            button login clicked
            signinButton.setOnClickListener(){
                if(emailInput.text.toString().isEmpty() || passwordInput.text.toString().isEmpty()){
                    Toast.makeText(this@LoginActivity, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }else{
                    if((emailInput.text.toString()==email || emailInput.text.toString()==username)
                        && passwordInput.text.toString()==password) {

                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()

                        val intentToDashboard = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intentToDashboard.putExtra(EXTRA_USERNAME, username)
                        startActivity(intentToDashboard)
                        finish()

                    }else{
                        Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}