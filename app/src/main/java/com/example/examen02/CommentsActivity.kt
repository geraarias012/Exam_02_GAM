package com.example.examen02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examen02.databinding.ActivityCommentsBinding
import com.example.examen02.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentsBinding
    private lateinit var adapter: CommentAdapter
    private var postId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el ID del post desde el Intent
        postId = intent.getIntExtra("POST_ID", 0)

        adapter = CommentAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        fetchComments()
    }

    private fun fetchComments() {
        CoroutineScope(Dispatchers.Main).launch {
            val apiService = ApiService.create()
            val comments = apiService.getCommentsByPostId(postId)
            adapter.submitList(comments)
        }
    }
}
