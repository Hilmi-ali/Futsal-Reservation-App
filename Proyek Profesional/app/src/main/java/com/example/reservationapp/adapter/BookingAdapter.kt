import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservationapp.R
import com.example.reservationapp.model.BookingModel

class BookingAdapter()
//    private val bookingList: List<BookingModel>,
//    private val onItemClick: (BookingModel) -> Unit // Tambahkan lambda untuk click listener
//) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
//        return BookingViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
//        val booking = bookingList[position]
//        holder.atasNama.text = booking.atasNama
//        holder.tanggal.text = booking.tanggal
//        holder.waktu.text = booking.waktu
//        holder.durasi.text = "(${booking.durasi})"
//        holder.arena.text = booking.arena
//
//        // Set click listener untuk item
//        holder.itemView.setOnClickListener {
//            onItemClick(booking) // Panggil lambda dengan data item yang di-klik
//        }
//    }
//
//
//    override fun getItemCount(): Int {
//        return bookingList.size
//    }
//
//    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val atasNama: TextView = itemView.findViewById(R.id.atasNama)
//        val tanggal: TextView = itemView.findViewById(R.id.tanggal)
//        val waktu: TextView = itemView.findViewById(R.id.waktu)
//        val durasi: TextView = itemView.findViewById(R.id.durasi)
//        val arena: TextView = itemView.findViewById(R.id.arena)
//    }
//}
