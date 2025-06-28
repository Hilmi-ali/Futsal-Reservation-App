package com.example.reservationapp.model

import java.io.Serializable

data class BookingModel (
    var id: String = "",
    var atasNama: String = "",
    var tanggal: String = "",
    var waktu: String = "",
    var durasi: String = "",
    var arena: String = "",
    var status: String = "Aktiv",
    val bookingSlot: String = "",
    ) : Serializable