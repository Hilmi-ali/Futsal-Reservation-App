package com.example.reservationapp

import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.view.View
import android.widget.Button
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.reservationapp.Server.APIService
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val mainLayout = findViewById<android.widget.LinearLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        auth = FirebaseAuth.getInstance()

        val loginButton: Button = findViewById(R.id.move)
        val emailField: EditText = findViewById(R.id.emailEditText)
        val passwordField: EditText = findViewById(R.id.passwordEditText)

        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            // Arahkan ke MainActivity atau halaman utama
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
