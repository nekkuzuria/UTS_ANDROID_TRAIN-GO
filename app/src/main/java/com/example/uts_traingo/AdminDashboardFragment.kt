package com.example.uts_traingo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uts_traingo.databinding.FragmentAdminDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminDashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminDashboardFragment : Fragment() {
    private lateinit var binding: FragmentAdminDashboardBinding
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        // Mendapatkan jumlah dokumen dari koleksi 'order'
        db.collection("order")
            .get()
            .addOnSuccessListener { documents ->
                binding.ordersCountTextView.text = documents.size().toString()
                Log.d("DashboardFragment", "Jumlah dokumen order: ${documents.size()}")
            }
            .addOnFailureListener { exception ->
                Log.w("DashboardFragment", "Gagal mengambil dokumen order", exception)
            }

        // Mendapatkan jumlah dokumen dari koleksi 'trains'
        db.collection("trains")
            .get()
            .addOnSuccessListener { documents ->
                binding.ticketsCountTextView.text = documents.size().toString()
                Log.d("DashboardFragment", "Jumlah dokumen trains: ${documents.size()}")
            }
            .addOnFailureListener { exception ->
                Log.w("DashboardFragment", "Gagal mengambil dokumen trains", exception)
            }
    }
}