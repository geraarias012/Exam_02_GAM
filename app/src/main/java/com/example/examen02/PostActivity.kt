package com.example.examen02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examen02.databinding.ActivityPostBinding
import com.example.examen02.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Intent


class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var adapter: PostAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el ID del usuario desde el Intent
        userId = intent.getIntExtra("USER_ID", 0)

        adapter = PostAdapter { post ->
            // Ir a CommentsActivity al hacer clic sobre un post
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("POST_ID", post.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        fetchPosts()
    }

    private fun fetchPosts() {
        CoroutineScope(Dispatchers.Main).launch {
            val apiService = ApiService.create()
            val posts = apiService.getPostsByUserId(userId)
            adapter.submitList(posts)
        }
    }
}
