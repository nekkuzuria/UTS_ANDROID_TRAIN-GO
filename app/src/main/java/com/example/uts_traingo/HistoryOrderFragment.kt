import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_traingo.HistoryAdapter
import com.example.uts_traingo.R
import com.example.uts_traingo.Train
import com.example.uts_traingo.databinding.FragmentHistoryOrderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryOrderFragment : Fragment() {
    private lateinit var binding: FragmentHistoryOrderBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            historyAdapter = HistoryAdapter()
            recyclerView = binding.recyclerViewHistory // Ganti dengan ID RecyclerView yang sesuai

            // Inisialisasi adapter dan set RecyclerView
            recyclerView.adapter = historyAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Load data dari Firebase
            loadDataFromFirebase()
        }

    }

    // Method untuk memuat data dari Firebase
    private fun loadDataFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val trainsCollection = db.collection("order")

        trainsCollection.get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    val trainList = mutableListOf<Train>()
                    for (document in result) {
                        val asal = document.getString("asal") ?: ""
                        val tujuan = document.getString("tujuan") ?: ""
                        val kelas = document.getString("kelas") ?: ""
                        val harga = document.getLong("harga")?.toInt() ?: 0
                        val fasilitasList = document.get("facilities") as? MutableList<Boolean> ?: mutableListOf()

                        val train = Train(asal, tujuan, kelas, fasilitasList, harga)
                        trainList.add(train)

                        Log.d("DataFirestore", "Asal: $asal, Tujuan: $tujuan, Kelas: $kelas, Harga: $harga, Fasilitas: $fasilitasList")
                    }

                    // Gunakan trainList untuk mengatur data adapter
                    historyAdapter.setData(trainList)

                    Log.d("berhasil", "item berhasil didapatkan")
                } else {
                    // Tidak ada data ditemukan
                    Log.d("gagal", "item tidak ditemukan di database")
                }
            }
            .addOnFailureListener { exception ->
                // Penanganan kesalahan saat mengambil data
                Log.d("gagal", "error saat mengambil data: $exception")
            }

    }



}
