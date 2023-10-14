package com.example.uts_traingo

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_ASAL
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON1
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON2
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON3
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON4
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON5
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON6
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_BUTTON7
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_CLASS
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_HARGA
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_TANGGAL
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_TUJUAN
import com.example.uts_traingo.DashboardActivity.Companion.EXTRA_USERNAME
import com.example.uts_traingo.databinding.ActivityDashboardBinding
import com.example.uts_traingo.databinding.ActivityOrderBinding
import java.util.Calendar

class OrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderBinding
    private val harga = MutableLiveData<Int>()



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding){
            val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)
            var selectedBtn1 = false
            var selectedBtn2 = false
            var selectedBtn3 = false
            var selectedBtn4 = false
            var selectedBtn5 = false
            var selectedBtn6 = false
            var selectedBtn7 = false


//            get array from strings.xml
            val stasiun = resources.getStringArray(R.array.stasiun)
            val adapterStasiun = ArrayAdapter<String>(
                this@OrderActivity,
                android.R.layout.simple_spinner_item,
                stasiun
            )

//            spinner asal & tujuan
            asalSpinner.adapter = adapterStasiun
            tujuanSpinner.adapter = adapterStasiun

//            spinner class
            val class_train = resources.getStringArray(R.array.class_train)
            val adapterClass = ArrayAdapter<String>(
                this@OrderActivity,
                android.R.layout.simple_spinner_item,
                class_train
            )
            classSpinner.adapter = adapterClass

//            Observe perubahan nilai harga
            harga.value = 0
            harga.observe(this@OrderActivity, Observer { newHarga ->
                priceAmount.text = newHarga.toString()
            })

//            Fungsi updateharga
            fun updateHarga() {
                var newHarga = 0
                // Perbarui harga berdasarkan pemilihan kelas
                when (class_train[classSpinner.selectedItemPosition]) {
                    "Economy" -> newHarga += 8000
                    "Bussiness" -> newHarga += 10000
                    "Executive" -> newHarga += 16000
                    "Luxury" -> newHarga += 30000
                }
                // Perbarui harga berdasarkan pemilihan paket
                if (selectedBtn1) {
                    newHarga += 10000
                }
                if (selectedBtn2) {
                    newHarga += 7000
                }
                if (selectedBtn3) {
                    newHarga += 6000
                }
                if (selectedBtn4) {
                    newHarga += 12000
                }
                if (selectedBtn4) {
                    newHarga += 2500
                }
                if (selectedBtn5) {
                    newHarga += 14000
                }
                if (selectedBtn6) {
                    newHarga += 2500
                }
                if (selectedBtn7) {
                    newHarga += 15000
                }
                harga.value = newHarga
            }

//            selected class
            classSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when(class_train[position]){
                            "Economy" -> updateHarga()
                            "Bussiness" -> updateHarga()
                            "Executive" -> updateHarga()
                            "Luxury" -> updateHarga()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }

//            selected order date
            orderdateInput.setOnClickListener(){
                val c = Calendar.getInstance()

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    this@OrderActivity,
                    {view, selectedYear, monthOfYear, dayOfMonth ->
                        val date = (dayOfMonth.toString() + "-" + (monthOfYear+1)
                                + "-" + selectedYear)
                        orderdateInput.setText(date)
                    },
                    year, month, day
                )

                datePickerDialog.show()
            }

//            selected package
            pkgButton1.setOnClickListener(){
                selectedBtn1 = !selectedBtn1
                updateHarga()
            }
            pkgButton2.setOnClickListener(){
                selectedBtn2 = !selectedBtn2
                updateHarga()
            }
            pkgButton3.setOnClickListener(){
                selectedBtn3 = !selectedBtn3
                updateHarga()
            }
            pkgButton4.setOnClickListener(){
                selectedBtn4 = !selectedBtn4
                updateHarga()
            }
            pkgButton5.setOnClickListener(){
                selectedBtn5 = !selectedBtn5
                updateHarga()
            }
            pkgButton6.setOnClickListener(){
                selectedBtn6 = !selectedBtn6
                updateHarga()
            }
            pkgButton7.setOnClickListener(){
                selectedBtn7 = !selectedBtn7
                updateHarga()
            }

            // Button Pesan
            orderButton.setOnClickListener() {
                val asal = asalSpinner.selectedItem.toString()
                val tujuan = tujuanSpinner.selectedItem.toString()
                val orderdate = orderdateInput.text.toString()
                val kelasKereta = classSpinner.selectedItem.toString()
                val harga = priceAmount.text.toString()
                val tanggal = orderdateInput.text.toString()

                // Balik ke MainActivity
                val username = intent.getStringExtra(RegisterActivity.EXTRA_USERNAME)
                val intentToDashboard = Intent(this@OrderActivity, DashboardActivity::class.java)
                intentToDashboard.putExtra(EXTRA_USERNAME, username)
                intentToDashboard.putExtra(EXTRA_BUTTON1, selectedBtn1)
                intentToDashboard.putExtra(EXTRA_BUTTON2, selectedBtn2)
                intentToDashboard.putExtra(EXTRA_BUTTON3, selectedBtn3)
                intentToDashboard.putExtra(EXTRA_BUTTON4, selectedBtn4)
                intentToDashboard.putExtra(EXTRA_BUTTON5, selectedBtn5)
                intentToDashboard.putExtra(EXTRA_BUTTON6, selectedBtn6)
                intentToDashboard.putExtra(EXTRA_BUTTON7, selectedBtn7)
                intentToDashboard.putExtra(EXTRA_ASAL, asal)
                intentToDashboard.putExtra(EXTRA_TUJUAN, tujuan)
                intentToDashboard.putExtra(EXTRA_CLASS, kelasKereta)
                intentToDashboard.putExtra(EXTRA_HARGA, harga)
                intentToDashboard.putExtra(EXTRA_TANGGAL, tanggal)
                setResult(RESULT_OK, intentToDashboard)
                finish()
                }
            }
        }
    }


