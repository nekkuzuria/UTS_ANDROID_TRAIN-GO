import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_traingo.databinding.ItemTrainTicketBinding
import com.example.uts_traingo.db.Train
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.uts_traingo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TrainAdapter : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    private var trainList: MutableList<com.example.uts_traingo.Train> = mutableListOf()
    private var userRole: String = ""

    inner class TrainViewHolder(val binding: ItemTrainTicketBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonPesan.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val train = trainList[position]

                    // Mendapatkan referensi Firebase Firestore
                    val db = FirebaseFirestore.getInstance()

                    // Mendapatkan ID pengguna dari Firebase Authentication
                    val firebaseAuth = FirebaseAuth.getInstance()
                    val currentUser = firebaseAuth.currentUser
                    val userID = currentUser?.uid

                    if (userID != null) {
                        // Objek data yang akan disimpan ke Firestore
                        val orderData = hashMapOf(
                            "idUser" to userID,
                            "asal" to train.asal,
                            "tujuan" to train.tujuan,
                            "harga" to train.harga,
                            "facilities" to train.fasilitas,
                            "kelas" to train.kelas
                        )

                        // Menyimpan data ke koleksi "order" di Firestore
                        db.collection("order")
                            .add(orderData)
                            .addOnSuccessListener { documentReference ->
                                Log.d("TrainAdapter", "DocumentSnapshot added with ID: ${documentReference.id}")
                                Toast.makeText(binding.root.context, "Tiket berhasil dibeli", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Log.w("TrainAdapter", "Error adding document", e)
                                Toast.makeText(binding.root.context, "Error terjadi, tiket gagal dibeli", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }

        fun bind(train: com.example.uts_traingo.Train) {
            binding.apply {
                asalTextView.text = train.asal
                tujuanTextView.text = train.tujuan
                textViewTrainClass.text = train.kelas
                textViewPrice.text = train.harga.toString()

                val fasilitasList = train.fasilitas
                setButtonVisibility(fasilitasList)

            }
        }

        private fun setButtonVisibility(fasilitasList: MutableList<Boolean>) {
            val pkgButtons = listOf(
                binding.pkgButton1,
                binding.pkgButton2,
                binding.pkgButton3,
                binding.pkgButton4,
                binding.pkgButton5,
                binding.pkgButton6,
                binding.pkgButton7
            )

            fasilitasList.forEachIndexed { index, isEnabled ->
                if (isEnabled && index < pkgButtons.size) {
                    pkgButtons[index].visibility = View.VISIBLE
                }
                else{
                    pkgButtons[index].visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTrainTicketBinding.inflate(inflater, parent, false)
        return TrainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val currentTrain = trainList[position]
        holder.bind(currentTrain)

        if (userRole == "admin") {
            holder.binding.buttonPesan.visibility = View.GONE // Jika rolenya adalah 'admin', tombol disembunyikan
        } else {
            holder.binding.buttonPesan.visibility = View.VISIBLE // Jika bukan 'admin', tombol ditampilkan
        }
    }

    override fun getItemCount(): Int = trainList.size

    fun setData(newList: MutableList<com.example.uts_traingo.Train>, role: String) {
        trainList = newList
        userRole = role
        notifyDataSetChanged()
    }

    fun setButtonVisibility(userRole: String) {

    }
}
