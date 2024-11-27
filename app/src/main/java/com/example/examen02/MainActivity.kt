package com.example.examen02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.examen02.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUsers.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        binding.btnCredits.setOnClickListener {
            startActivity(Intent(this, CreditosActivity::class.java))
        }

        binding.btnExit.setOnClickListener {
            finish()
        }
    }
}
