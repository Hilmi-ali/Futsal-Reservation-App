package com.example.reservationapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservationapp.R
import com.example.reservationapp.adapter.Adapter
//import com.example.reservationapp.adapter.BookingAdapter
import com.example.reservationapp.Detail
import com.example.reservationapp.Features.Cancel
import com.example.reservationapp.model.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale


class Activity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookingAdapter: Adapter
    private lateinit var bookingList: MutableList<BookingModel>
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activity, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Inisialisasi database reference untuk pengguna yang sedang login
        val firebaseAuth = FirebaseAuth.getInstance()
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            databaseRef = FirebaseDatabase.getInstance().getReference("users/$userId/bookings")
        } ?: run {
            // Jika tidak ada user yang login, tampilkan pesan atau lakukan tindakan lain
        }

        // Ambil data dari Firebase
        bookingList = mutableListOf()
        bookingAdapter = Adapter(bookingList) { booking ->
            val detailIntent = Intent(context, Detail::class.java)
            detailIntent.putExtra("BOOKING_DATA", booking) // Kirim data ke halaman detail
            startActivity(detailIntent)
        }

        recyclerView.adapter = bookingAdapter

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingList.clear()
                for (dataSnapshot in snapshot.children) {
                    val booking = dataSnapshot.getValue(BookingModel::class.java)
                    if (booking != null && booking.status == "Aktif") { // Tampilkan hanya pesanan aktif
                        booking.tanggal = formatTanggal(booking.tanggal)
                        bookingList.add(booking)
                    }
                }
                bookingAdapter.notifyDataSetChanged() // Update RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return view
    }
    // Fungsi untuk memformat tanggal
    private fun formatTanggal(tanggal: String?): String {
        return try {
            // Asumsikan tanggal input dalam format "dd-MM-yyyy"
            val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(tanggal ?: "")
            if (date != null) outputFormat.format(date) else ""
        } catch (e: Exception) {
            tanggal ?: "" // Jika format tidak sesuai, kembalikan input asli
        }
    }

}