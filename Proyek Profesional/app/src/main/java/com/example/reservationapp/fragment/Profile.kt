package com.example.reservationapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.reservationapp.Features.About
import com.example.reservationapp.Features.History
import com.example.reservationapp.MainActivity
import com.example.reservationapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : Fragment() {

    private lateinit var aboutUs: LinearLayout
    private lateinit var history: LinearLayout
    private lateinit var databaseRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        // Mengambil instance FirebaseAuth
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
                    val greetingTextView = view.findViewById<TextView>(R.id.tv_greeting)
                    username?.let {
                        greetingTextView.text = it
                    }
                    // Menampilkan nomor telepon pada TextView
                    val phoneTextView = view.findViewById<TextView>(R.id.tvPhone)
                    phoneNumber?.let {
                        phoneTextView.text = it
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Jika terjadi kesalahan dalam mengambil data
                }
            })
        } ?: run {
            // Jika tidak ada pengguna yang login, Anda bisa menampilkan pesan lain atau melakukan tindakan lain
        }

        aboutUs = view.findViewById(R.id.aboutUs)
        history = view.findViewById(R.id.riwayat)

        aboutUs.setOnClickListener {
            val intent = Intent(context, About::class.java)
            startActivity(intent)
        }

        history.setOnClickListener {
            val intent = Intent(context, History::class.java)
            startActivity(intent)
        }


        return view
    }

}