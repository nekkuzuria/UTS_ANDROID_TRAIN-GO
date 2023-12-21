import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts_traingo.R
import com.example.uts_traingo.Train
import com.example.uts_traingo.databinding.FragmentListTrainBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListTrainFragment : Fragment() {
    private lateinit var binding: FragmentListTrainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListTrainBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            val sharedPref = requireActivity().getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)
            val userRole = sharedPref.getString("userRole", "user") ?: "user"

            val trainAdapter = TrainAdapter()
            recyclerViewListTrain.adapter = trainAdapter
            recyclerViewListTrain.layoutManager = LinearLayoutManager(requireContext())

            val db = FirebaseFirestore.getInstance()
            val trainsCollection = db.collection("trains")

            trainsCollection.get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        val trainList = mutableListOf<Train>()
                        for (document in result) {
                            val asal = document.getString("asal") ?: ""
                            val tujuan = document.getString("tujuan") ?: ""
                            val kelas = document.getString("kelasKereta") ?: ""
                            val harga = document.getLong("harga")?.toInt() ?: 0
                            val fasilitasList = document.get("facilities") as? MutableList<Boolean> ?: mutableListOf()

                            val train = Train(asal, tujuan, kelas, fasilitasList, harga)
                            trainList.add(train)

                            Log.d("DataFirestore", "Asal: $asal, Tujuan: $tujuan, Kelas: $kelas, Harga: $harga, Fasilitas: $fasilitasList")
                        }

                        // Gunakan trainList seperti yang telah dijelaskan sebelumnya
                        trainAdapter.setData(trainList, userRole)

                        Log.d("berhasil", "item berhasil didapatkan")

                    } else {
                        // Tidak ada data ditemukan
                        Log.d("gagal", "item nya gada di database")
                    }
                }
                .addOnFailureListener { exception ->
                    // Penanganan kesalahan saat mengambil data
                    Log.d("gagal", "eror saat mengambil data")
                }


        }
    }
}
