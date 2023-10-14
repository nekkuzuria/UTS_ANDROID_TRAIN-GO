package com.example.uts_traingo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.uts_traingo.databinding.ActivityDashboardBinding
import com.example.uts_traingo.databinding.ActivityLoginBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    companion object {
        const val EXTRA_BUTTON1 = "extra_button1"
        const val EXTRA_BUTTON2 = "extra_button2"
        const val EXTRA_BUTTON3 = "extra_button3"
        const val EXTRA_BUTTON4 = "extra_button4"
        const val EXTRA_BUTTON5 = "extra_button5"
        const val EXTRA_BUTTON6 = "extra_button6"
        const val EXTRA_BUTTON7 = "extra_button7"
        const val EXTRA_USERNAME = "extra_button8"
        const val EXTRA_ASAL = "extra_asal"
        const val EXTRA_TUJUAN = "extra_tujuan"
        const val EXTRA_CLASS = "extra_class"
        const val EXTRA_HARGA = "extra_harga"
        const val EXTRA_TANGGAL = "extra_tanggal"
    }

    private val listTanggal = ArrayList<String>()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            Should update UI

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val asal = data?.getStringExtra(EXTRA_ASAL)
                val tujuan = data?.getStringExtra(EXTRA_TUJUAN)
                val kelas = data?.getStringExtra(EXTRA_CLASS)
                val harga = data?.getStringExtra(EXTRA_HARGA)
                val tanggal = data?.getStringExtra(EXTRA_TANGGAL)
                val button1 = data?.getBooleanExtra(EXTRA_BUTTON1, false)
                val button2 = data?.getBooleanExtra(EXTRA_BUTTON2, false)
                val button3 = data?.getBooleanExtra(EXTRA_BUTTON3, false)
                val button4 = data?.getBooleanExtra(EXTRA_BUTTON4, false)
                val button5 = data?.getBooleanExtra(EXTRA_BUTTON5, false)
                val button6 = data?.getBooleanExtra(EXTRA_BUTTON6, false)
                val button7 = data?.getBooleanExtra(EXTRA_BUTTON7, false)

                with(binding) {
//                    Last Order
                    stasiun.setText(asal + "      ->      " + tujuan)
                    kelasTextview.setText(kelas)
                    if (button1 == true) {
                        pkgButton1.visibility = View.VISIBLE
                    }
                    if (button2 == true) {
                        pkgButton2.visibility = View.VISIBLE
                    }
                    if (button3 == true) {
                        pkgButton3.visibility = View.VISIBLE
                    }
                    if (button4 == true) {
                        pkgButton4.visibility = View.VISIBLE
                    }
                    if (button5 == true) {
                        pkgButton5.visibility = View.VISIBLE
                    }
                    if (button6 == true) {
                        pkgButton6.visibility = View.VISIBLE
                    }
                    if (button7 == true) {
                        pkgButton7.visibility = View.VISIBLE
                    }

//                    Tanggal
                    if (tanggal != null) {
                        listTanggal.add(tanggal)
                    }
                    tanggalTextview.setText(tanggal)

//                    harga
                    priceAmount.setText(harga)
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)

//            display on dashboard
            welcomeTextview.setText("Welcome " + username.toString() + "!")

//            button order clicked
            orderButton.setOnClickListener() {
                val intentToOrder = Intent(this@DashboardActivity, OrderActivity::class.java)
                intentToOrder.putExtra(LoginActivity.EXTRA_USERNAME, username)
                launcher.launch(intentToOrder)
            }

//            Button
            pkgButton1.visibility = View.GONE
            pkgButton2.visibility = View.GONE
            pkgButton3.visibility = View.GONE
            pkgButton4.visibility = View.GONE
            pkgButton5.visibility = View.GONE
            pkgButton6.visibility = View.GONE
            pkgButton7.visibility = View.GONE


//            calender
            kalender.setOnDateChangeListener() { view, year, month, dayOfMonth ->
                if (listTanggal.contains(
                        dayOfMonth.toString() + "-" + (month + 1)
                                + "-" + year
                    )
                ) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Order already exists on $dayOfMonth/${month + 1}/$year",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Order doesn/'t exist on $dayOfMonth/${month + 1}/$year",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

