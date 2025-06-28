package com.example.reservationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reservationapp.Features.Bayar
import com.example.reservationapp.Features.Cancel
import com.example.reservationapp.adapter.Adapter
import com.example.reservationapp.model.BookingModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Detail : AppCompatActivity() {

    private lateinit var bookingAdapter: Adapter
    private lateinit var bookingList: MutableList<BookingModel>
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val booking = intent.getSerializableExtra("BOOKING_DATA") as? BookingModel
        val tvAtasNama: TextView = findViewById(R.id.tvAtasNama)
        val tvTanggal: TextView = findViewById(R.id.tvTanggal)
        val tvWaktu: TextView = findViewById(R.id.tvWaktu)
        val tvDurasi: TextView = findViewById(R.id.tvDurasi)
        val tvArena: TextView = findViewById(R.id.tvArena)
        val btnBayar: Button = findViewById(R.id.btnBayar)
        val tvBiaya: TextView = findViewById(R.id.biaya)
        val cancel:Button = findViewById(R.id.btnCancel)

        booking?.let {
            tvAtasNama.text = "${it.atasNama}"
            tvTanggal.text = "${it.tanggal}"
            tvWaktu.text = "${it.waktu}"
            tvDurasi.text = "(${it.durasi})"
            tvArena.text = "${it.arena}"
            tvBiaya.text= "Rp 100.000"

        }
        btnBayar.setOnClickListener{
            val intent = Intent(this, Bayar::class.java)
            startActivity(intent)
        }

        // Inisialisasi database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("bookings")

        // Ambil data dari Firebase
        bookingList = mutableListOf()
        bookingAdapter = Adapter(bookingList) { booking ->
            val cancelIntent = Intent(this, Cancel::class.java)
            cancelIntent.putExtra("BOOKING_DATA", booking) // Kirim data ke halaman Cancel
            startActivity(cancelIntent)
        }
        cancel.setOnClickListener{
            val cancelIntent = Intent(this, Cancel::class.java)
            cancelIntent.putExtra("BOOKING_DATA", booking) // Kirim data yang sama ke halaman Cancel
            startActivity(cancelIntent)
        }


    }
}