package com.example.reservationapp

import android.content.Intent
import android.os.Build
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.reservationapp.R.*
import com.example.reservationapp.R
import com.example.reservationapp.model.UserModel
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aktifkan full-screen mode dan hilangkan status bar dan navigation bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            // Untuk API level di bawah 30, gunakan full-screen flags yang kompatibel
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        enableEdgeToEdge()
        setContentView(layout.activity_register)
        val mainLayout = findViewById<android.widget.LinearLayout>(id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val registerButton: Button = findViewById(R.id.register)
        val nameField: EditText = findViewById(R.id.namaEt)
        val telpField: EditText = findViewById(R.id.telpEt)
        val emailField: EditText = findViewById(R.id.emailEt)
        val passwordField: EditText = findViewById(R.id.editPassword)
        val btn = findViewById<TextView>(R.id.login)

        btn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val name = nameField.text.toString()
            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val phone = telpField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty()) {
                // Registrasi menggunakan Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Dapatkan UID dari pengguna yang baru dibuat
                            val userId = auth.currentUser?.uid

                            // Simpan data tambahan ke Firebase Realtime Database
                            val user = UserModel(name, email,password ,phone) // Menambahkan nomor telepon
                            userId?.let {
                                val databaseRef = FirebaseDatabase.getInstance().getReference("usersData")
                                databaseRef.child(it).setValue(user)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                            // Arahkan ke LoginActivity
                                            startActivity(Intent(this, Login::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Gagal menyimpan data pengguna", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}