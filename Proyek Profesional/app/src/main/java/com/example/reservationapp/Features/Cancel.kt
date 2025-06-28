package com.example.reservationapp.Features

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.reservationapp.R
import com.example.reservationapp.model.BookingModel
import com.google.firebase.database.FirebaseDatabase

class Cancel : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel)

        progressBar = findViewById(R.id.progressBar)

        // Mengambil data booking yang dikirimkan melalui intent
        val booking = intent.getSerializableExtra("BOOKING_DATA") as? BookingModel
        if (booking != null) {
            Log.d("CancelActivity", "Booking data received: $booking")
        } else {
            Log.d("CancelActivity", "No booking data received.")
        }

        // Inisialisasi tombol dan EditText
        val changeOrder: Button = findViewById(R.id.btnChange)
        val cancelOrder: Button = findViewById(R.id.btnCancelOrder)
        val alasanPembatalan: EditText = findViewById(R.id.editPembatalan)

        // Inisialisasi TextViews untuk menampilkan informasi booking
        val nama: TextView = findViewById(R.id.atasNama)
        val arena: TextView = findViewById(R.id.arena)
        val tanggal: TextView = findViewById(R.id.tanggal)
        val waktu: TextView = findViewById(R.id.waktu)
        val durasi: TextView = findViewById(R.id.durasi)

        // Menampilkan data booking jika ada
        booking?.let {
            nama.text = "Nama : ${it.atasNama}"
            arena.text = "Arena: ${it.arena}"
            tanggal.text = "Tanggal: ${it.tanggal}"
            waktu.text = "Waktu: ${it.waktu}"
            durasi.text = "Durasi: ${it.durasi}"
        }?: run {
            Toast.makeText(this, "Data booking tidak diterima", Toast.LENGTH_SHORT).show()
        }

        // Tombol untuk mengubah pesanan
        changeOrder.setOnClickListener {
            val alasan = alasanPembatalan.text.toString().trim()  // Sama seperti alasan pembatalan
            if (alasan.isEmpty()) {
                Toast.makeText(this, "Alasan harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                sendScheduleChangeToAdmin(booking, "Ganti Jadwal", alasan)  // Kirim dengan status "Ganti Jadwal"
            }
        }
        // Tombol untuk membatalkan pesanan
        cancelOrder.setOnClickListener {
            val alasan = alasanPembatalan.text.toString().trim()
            if (alasan.isEmpty()) {
                Toast.makeText(this, "Alasan pembatalan harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                sendScheduleChangeToAdmin(booking, "Pembatalan", alasan)
            }
        }

    }
    private fun sendScheduleChangeToAdmin(booking: BookingModel?, status: String, alasan: String) {
        if (booking != null) {
            val database = FirebaseDatabase.getInstance().reference
            val scheduleChangeRef = database.child("scheduleChange").push()

            val scheduleChangeData = hashMapOf(
                "BookingId" to booking.id ,// Pastikan ID booking tetap sama
                "atasNama" to booking.atasNama,
                "arena" to booking.arena,
                "tanggal" to booking.tanggal,
                "waktu" to booking.waktu,
                "status" to status,
                "alasanPembatalan" to alasan
            )

            scheduleChangeRef.setValue(scheduleChangeData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Pengajuan $status terkirim ke admin", Toast.LENGTH_SHORT).show()
                    finish() // Menutup halaman setelah berhasil mengirim data
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengirim $status", Toast.LENGTH_SHORT).show()
                }
        }
    }



}
