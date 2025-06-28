package com.example.reservationapp.Features

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservationapp.R
import com.example.reservationapp.adapter.Adapter
import com.example.reservationapp.model.BookingModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class History : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: Adapter
    private lateinit var historyList: MutableList<BookingModel>
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            databaseRef = FirebaseDatabase.getInstance().getReference("users/$userId/bookings")
        }
        historyList = mutableListOf()
        historyAdapter = Adapter(historyList) { booking ->
            // Handle item click (optional)
        }
        recyclerView.adapter = historyAdapter

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyList.clear()
                for (dataSnapshot in snapshot.children) {
                    val booking = dataSnapshot.getValue(BookingModel::class.java)
                    if (booking != null) {
                        Log.d("History", "Booking ID: ${dataSnapshot.key}, Status: ${booking.status}")
                        if (booking.status == "completed") {
                            historyList.add(booking)
                        }
                    }
                }
                historyAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}