import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts_traingo.R
import com.example.uts_traingo.Train
import com.example.uts_traingo.databinding.FragmentListTrainBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListTrainFragment : Fragment() {
    private lateinit var binding: FragmentListTrainBinding
    private val CHANNEL_ID = "your_channel_id"
    private val NOTIFICATION_ID = 123

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

            val trainAdapter = TrainAdapter(requireContext())
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

    private fun showNotification(message: String) {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.round_notifications_24)
            .setContentTitle("Notification Title")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Your Channel Name"
            val descriptionText = "Your Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
