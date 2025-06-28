package com.example.reservationapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.reservationapp.R
import com.example.reservationapp.model.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class Booking : Fragment() {

    private lateinit var atasNamaInput: EditText
    private lateinit var tanggalInput: EditText
    private lateinit var waktuInput: EditText
    private lateinit var durasiInput: EditText
    private lateinit var arenaInput: EditText
    private lateinit var bookingButton: Button

    private lateinit var databaseRef: DatabaseReference

    private fun checkBookingAvailability(tanggal: String, waktu: String, arena: String, callback: (Boolean) -> Unit) {
        val bookingSlot = "$tanggal-$waktu-$arena"
        Log.d("DEBUG", "Checking availability for: $bookingSlot")

        // Memeriksa apakah bookingSlot sudah ada di database
        val bookedSlotsRef = FirebaseDatabase.getInstance().getReference("bookedSlots")

        bookedSlotsRef.child(bookingSlot).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d("DEBUG", "Slot already exists: $bookingSlot")
                    callback(false) // Slot tidak tersedia
                } else {
                    Log.d("DEBUG", "Slot is available: $bookingSlot")
                    callback(true) // Slot tersedia
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DEBUG", "Firebase error: ${error.message}")
                callback(false) // Anggap slot tidak tersedia jika error
            }
        })
    }

    // Fungsi untuk menyimpan data booking
    private fun saveBooking(userId: String, bookingId: String, bookingData: Map<String, Any>, slotKey: String) {
        val userBookingRef = FirebaseDatabase.getInstance().getReference("users/$userId/bookings")
        val slotRef = FirebaseDatabase.getInstance().getReference("bookedSlots/$slotKey")

        // Simpan ke node users dan bookedSlots secara bersamaan
        val updates = hashMapOf<String, Any>(
            "users/$userId/bookings/$bookingId" to bookingData,
            "bookedSlots/$slotKey" to bookingData
        )

        FirebaseDatabase.getInstance().reference.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(activity, "Booking berhasil disimpan!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Gagal menyimpan booking.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        // Inisialisasi Firebase Database
        databaseRef = FirebaseDatabase.getInstance().getReference("users")

        atasNamaInput = view.findViewById(R.id.atasNamaInput)
        tanggalInput = view.findViewById(R.id.tanggalInput)
        waktuInput = view.findViewById(R.id.waktuInput)
        durasiInput = view.findViewById(R.id.durasiInput)
        arenaInput = view.findViewById(R.id.arenaInput)
        bookingButton = view.findViewById(R.id.bookingButton)

        val arenaName = arguments?.getString("arena_name")
        arenaInput.setText(arenaName)
        Log.d(TAG, "Received arena name: $arenaName")

        // Calendar instance
        val calendar = Calendar.getInstance()

        // Listener untuk tanggal
        tanggalInput.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                tanggalInput.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Listener untuk waktu
        waktuInput.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)

            val timePicker = TimePickerDialog(requireContext(), { _, selectedHour, _ ->
                // Format waktu menjadi "HH:00" untuk mengabaikan menit
                val formattedTime = String.format("%02d:00", selectedHour)
                waktuInput.setText(formattedTime)
            }, hour, 0, true) // Gunakan format 24 jam (true)

            timePicker.show()
        }

        // Listener untuk durasi
        durasiInput.setOnClickListener {
            val durasiOptions = arrayOf("1 Jam", "2 Jam", "3 Jam", "4 Jam", "5 Jam")

            AlertDialog.Builder(requireContext())
                .setTitle("Pilih Durasi")
                .setItems(durasiOptions) { _, which ->
                    durasiInput.setText(durasiOptions[which])
                }
                .create()
                .show()
        }

        // Listener untuk arena
        arenaInput.setOnClickListener {
            val arenaOptions = arrayOf("Arena 1", "Arena 2", "Arena 3")

            AlertDialog.Builder(requireContext())
                .setTitle("Pilih Arena")
                .setItems(arenaOptions) { _, which ->
                    arenaInput.setText(arenaOptions[which])
                }
                .create()
                .show()
        }

        bookingButton.setOnClickListener {
            val atasNama = atasNamaInput.text.toString().trim()
            val tanggal = tanggalInput.text.toString().trim()
            val waktu = waktuInput.text.toString().trim()
            val durasi = durasiInput.text.toString().trim()
            val arena = arenaInput.text.toString().trim()

            if (atasNama.isBlank() || tanggal.isBlank() || waktu.isBlank() || durasi.isBlank() || arena.isBlank()) {
                Toast.makeText(activity, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            bookingButton.isEnabled = false // Nonaktifkan tombol sementara
            val bookingSlot = "$tanggal-$waktu-$arena"
            checkBookingAvailability(tanggal, waktu, arena) { isAvailable ->
                bookingButton.isEnabled = true // Aktifkan kembali tombol setelah pengecekan selesai

                if (isAvailable) {
                    // Jika slot tersedia, simpan pemesanan berdasarkan UID pengguna
                    val firebaseAuth = FirebaseAuth.getInstance()
                    val userId = firebaseAuth.currentUser?.uid // Mendapatkan UID pengguna yang login

                    if (userId != null) {
                        val userBookingRef = FirebaseDatabase.getInstance().getReference("users/$userId/bookings")

                        val booking = BookingModel(
                            id = "",  // Isi dengan nilai yang sesuai jika perlu (misalnya bisa kosong atau ID unik)
                            atasNama = atasNama,
                            tanggal = tanggal,
                            waktu = waktu,
                            durasi = durasi,
                            arena = arena,
                            bookingSlot = bookingSlot,
                            status = "Aktif"
                        )
                        val bookingId = userBookingRef.push().key
                        if (bookingId != null) {
                            val bookingData = mapOf(
                                "id" to bookingId,
                                "atasNama" to atasNama,
                                "tanggal" to tanggal,
                                "waktu" to waktu,
                                "durasi" to durasi,
                                "arena" to arena,
                                "bookingSlot" to bookingSlot,
                                "status" to "Aktif"
                            )
                            saveBooking(userId, bookingId, bookingData, bookingSlot)
                        }
                    }
                } else {
                    // Jika slot sudah terisi
                    AlertDialog.Builder(requireContext())
                        .setTitle("Pemesanan Gagal")
                        .setMessage("Lapangan ini sudah terpesan. Silakan pilih waktu atau arena lain.")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
        }


        return view
    }
}
