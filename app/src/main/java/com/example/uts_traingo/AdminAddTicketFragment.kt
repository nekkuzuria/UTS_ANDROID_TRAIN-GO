import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import com.example.uts_traingo.R
import com.example.uts_traingo.databinding.FragmentAdminAddTicketBinding
import com.example.uts_traingo.db.Train
import com.example.uts_traingo.db.TrainDao
import com.example.uts_traingo.db.TrainDatabase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdminAddTicketFragment : Fragment() {

    private lateinit var binding: FragmentAdminAddTicketBinding
    private val facilitiesList = mutableListOf<String>()
    private var priceLiveData = MutableLiveData<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminAddTicketBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            var selectedBtn1 = false
            var selectedBtn2 = false
            var selectedBtn3 = false
            var selectedBtn4 = false
            var selectedBtn5 = false
            var selectedBtn6 = false
            var selectedBtn7 = false

//                adapter spinner
            val stasiunArray = resources.getStringArray(R.array.stasiun)
            val kelasArray = resources.getStringArray(R.array.class_train)
            val stasiunAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stasiunArray)
            val kelasAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kelasArray)
            stasiunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            asalSpinner.adapter = stasiunAdapter
            tujuanSpinner.adapter = stasiunAdapter
            classSpinner.adapter = kelasAdapter

//            Observe perubahan nilai harga
            priceLiveData.value = 0
            priceLiveData.observe(requireActivity(), Observer { newHarga ->
                priceAmount.text = newHarga.toString()
            })

//            Fungsi updateharga
            fun updateHarga() {
                var newHarga = 0
                // Perbarui harga berdasarkan pemilihan kelas
                when (kelasArray[classSpinner.selectedItemPosition]) {
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
                priceLiveData.value = newHarga
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
                        when (kelasArray[position]) {
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

//            selected package
            pkgButton1.setOnClickListener() {
                selectedBtn1 = !selectedBtn1
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton1.text.toString())
                } else {
                    removeFacility(pkgButton1.text.toString())
                }
            }
            pkgButton2.setOnClickListener() {
                selectedBtn2 = !selectedBtn2
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton2.text.toString())
                } else {
                    removeFacility(pkgButton2.text.toString())
                }
            }
            pkgButton3.setOnClickListener() {
                selectedBtn3 = !selectedBtn3
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton3.text.toString())
                } else {
                    removeFacility(pkgButton3.text.toString())
                }
            }
            pkgButton4.setOnClickListener() {
                selectedBtn4 = !selectedBtn4
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton4.text.toString())
                } else {
                    removeFacility(pkgButton4.text.toString())
                }
            }
            pkgButton5.setOnClickListener() {
                selectedBtn5 = !selectedBtn5
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton5.text.toString())
                } else {
                    removeFacility(pkgButton5.text.toString())
                }
            }
            pkgButton6.setOnClickListener() {
                selectedBtn6 = !selectedBtn6
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton6.text.toString())
                } else {
                    removeFacility(pkgButton6.text.toString())
                }
            }
            pkgButton7.setOnClickListener() {
                selectedBtn7 = !selectedBtn7
                updateHarga()
                if (selectedBtn1) {
                    addFacility(pkgButton7.text.toString())
                } else {
                    removeFacility(pkgButton7.text.toString())
                }
            }

//            Button publish diklik
            publishButton.setOnClickListener() {
                Toast.makeText(requireActivity(), "Item ditambahkan", Toast.LENGTH_SHORT).show()
                val asal = asalSpinner.selectedItem.toString()
                val tujuan = tujuanSpinner.selectedItem.toString()
                val kelasKereta = classSpinner.selectedItem.toString()
                val harga = priceAmount.text.toString().toInt()

                val train = Train(asal, tujuan, kelasKereta, ArrayList(facilitiesList), harga)

                if (isInternetAvailable(requireContext())) {
                    saveToFirebase(train)
                } else {
                    saveToRoom(train)
                }
            }
        }
    }

    private fun addFacility(facility: String) {
        if (!facilitiesList.contains(facility)) {
            facilitiesList.add(facility)
        }
    }

    private fun removeFacility(facility: String) {
        facilitiesList.remove(facility)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun saveToFirebase(train: Train) {
        val db = FirebaseFirestore.getInstance()
        val trainsCollection = db.collection("trains")

        val trainData = hashMapOf(
            "asal" to train.asal,
            "tujuan" to train.tujuan,
            "kelasKereta" to train.kelas,
            "facilities" to train.fasilitas,
            "harga" to train.harga
        )

        trainsCollection
            .add(trainData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(requireContext(), "Item berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Item gagal ditambahkan", Toast.LENGTH_SHORT).show()
                Log.w(TAG, "Error adding document", e)
            }
    }


    private fun saveToRoom(train: Train) {
        GlobalScope.launch(Dispatchers.IO) {
            val trainDao = TrainDatabase.getInstance(requireContext())?.trainDao()
            trainDao?.let {
                try {
                    it.insertAll(train)
                    Toast.makeText(requireContext(), "Item berhasil ditambahkan ke Room", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Item gagal ditambahkan ke Room", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
