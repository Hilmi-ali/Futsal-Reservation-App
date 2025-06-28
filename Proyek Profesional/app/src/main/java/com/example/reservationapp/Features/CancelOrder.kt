package com.example.reservationapp.Features

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reservationapp.R
import com.example.reservationapp.model.BookingModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CancelOrder : AppCompatActivity() {

    private lateinit var cancelButton: Button
    private lateinit var keteranganEditText: EditText
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_order)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        cancelButton = findViewById(R.id.btnSend)
        keteranganEditText = findViewById(R.id.keterangan)

        // Inisialisasi Firebase Database
        databaseRef = FirebaseDatabase.getInstance().getReference("bookings")

        // Menambahkan Listener pada tombol kirim
        cancelButton.setOnClickListener {
            val keterangan = keteranganEditText.text.toString().trim()

            if (keterangan.isEmpty()) {
                Toast.makeText(this, "Keterangan harus diisi!", Toast.LENGTH_SHORT).show()
            } else {
                val bookingData = intent.getSerializableExtra("BOOKING_DATA") as? BookingModel

                bookingData?.let {
                    // Log untuk memeriksa data yang diterima
                    Log.d("CancelOrder", "Booking Data: $it")

                    val bookingId = it.id  // Pastikan Anda menyertakan id pesanan pada model
                    val updatedBooking = mapOf(
                        "status" to "canceled",
                        "keteranganPembatalan" to keterangan
                    )

                    // Log untuk memeriksa data yang akan dikirim
                    Log.d("CancelOrder", "Updating booking with ID: $bookingId, Data: $updatedBooking")

                    databaseRef.child(bookingId).updateChildren(updatedBooking).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Pesanan berhasil dibatalkan", Toast.LENGTH_SHORT).show()
                            finish()  // Menutup halaman setelah berhasil
                        } else {
                            Toast.makeText(this, "Gagal membatalkan pesanan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

}
