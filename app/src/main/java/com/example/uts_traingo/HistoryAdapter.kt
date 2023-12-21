package com.example.uts_traingo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_traingo.databinding.FragmentHistoryOrderBinding
import com.example.uts_traingo.databinding.ItemOrderHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private var orderList: List<Train> = emptyList()
    inner class HistoryViewHolder(private val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Train) {
            Log.d("historyadapter", "$order")

            binding.textViewOrigin.text = "${order.asal} -> ${order.tujuan}" // Mengganti dengan data yang sesuai
            binding.textViewTrainClass.text = order.kelas // Ganti dengan data yang sesuai

            val pkgButtons = listOf(
                binding.pkgButton1,
                binding.pkgButton2,
                binding.pkgButton3,
                binding.pkgButton4,
                binding.pkgButton5,
                binding.pkgButton6,
                binding.pkgButton7
            )

            order.fasilitas.forEachIndexed { index, isEnabled ->
                if (isEnabled && index < pkgButtons.size) {
                    pkgButtons[index].visibility = View.VISIBLE
                } else {
                    pkgButtons[index].visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderHistoryBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order = orderList[position] // Ganti dengan sumber data yang sesuai
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    // Method untuk mengatur data baru ke adapter
    fun setData(newList: List<Train>) {
        orderList = newList
        notifyDataSetChanged()
    }
}
