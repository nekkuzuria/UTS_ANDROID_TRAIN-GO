import androidx.recyclerview.widget.RecyclerView
import com.example.uts_traingo.databinding.ItemTrainTicketBinding
import com.example.uts_traingo.db.Train
import android.view.ViewGroup
import android.view.LayoutInflater



class TrainAdapter : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    private var trainList = emptyList<Train>()

    inner class TrainViewHolder(private val binding: ItemTrainTicketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(train: Train) {
            binding.apply {
                asalTextView.text = train.asal
                tujuanTextView.text = train.tujuan
                textViewTrainClass.text = train.kelas
                textViewPrice.text = train.harga.toString()
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
    }

    override fun getItemCount(): Int = trainList.size

    fun setData(newList: List<Train>) {
        trainList = newList
        notifyDataSetChanged()
    }
}
