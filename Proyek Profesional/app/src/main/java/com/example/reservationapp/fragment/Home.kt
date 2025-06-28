package com.example.reservationapp.fragment
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.reservationapp.R
import android.graphics.Matrix
import android.widget.FrameLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment() {

    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val firebaseAuth = FirebaseAuth.getInstance()

        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            // Menentukan referensi ke database Realtime Firebase untuk pengguna yang sedang login
            databaseRef = FirebaseDatabase.getInstance().getReference("usersData/$userId")

            // Mendapatkan data pengguna berdasarkan UID
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Mengambil nama pengguna dari data snapshot
                    val username = dataSnapshot.child("name").getValue(String::class.java)
                    val phoneNumber = dataSnapshot.child("phone").getValue(String::class.java)

                    // Menampilkan nama pengguna pada TextView
                    val greetingTextView = view.findViewById<TextView>(R.id.nama)
                    username?.let {
                        greetingTextView.text = it
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Jika terjadi kesalahan dalam mengambil data
                }
            })
        } ?: run {
            // Jika tidak ada pengguna yang login, Anda bisa menampilkan pesan lain atau melakukan tindakan lain
        }
        return view
        }
}