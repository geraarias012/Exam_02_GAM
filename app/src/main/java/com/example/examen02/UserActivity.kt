package com.example.examen02

import android.content.Context
import android.widget.Toast
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.examen02.databinding.ActivityUserBinding
import com.example.examen02.model.User
import com.example.examen02.network.ApiService
import com.example.examen02.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura RecyclerView
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(
            onLongClick = { user ->
                saveUserToLocalDatabase(user)
            },
            onClick = { user ->
                openUserPosts(user)
            }
        )
        binding.recyclerViewUsers.adapter = userAdapter

        // Inicializa la base de datos
        database = AppDatabase.getDatabase(this)

        // Carga los datos (API o Room)
        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = if (isOnline()) {
                // Si hay conexión, intenta cargar los datos de la API
                try {
                    val apiUsers = fetchUsersFromApi()
                    saveUsersToLocalDatabase(apiUsers) // Guardar datos en la base local
                    apiUsers
                } catch (e: Exception) {
                    // En caso de error (como un fallo en la API), carga los datos locales
                    fetchUsersFromRoom()
                }
            } else {
                // Si no hay conexión, carga los datos de la base de datos local
                fetchUsersFromRoom()
            }
            userAdapter.submitList(users) // Actualiza la lista en el adaptador
        }
    }


    private suspend fun fetchUsersFromApi(): List<User> {
        return withContext(Dispatchers.IO) {
            // Aquí obtenemos los usuarios usando Retrofit
            val apiService = ApiService.create()
            val response = apiService.getUsers() // Esto debe devolver una lista de usuarios
            response // Asegúrate de retornar la respuesta
        }
    }

    private suspend fun fetchUsersFromRoom(): List<User> {
        return withContext(Dispatchers.IO) {
            database.userDao().getAllUsers()
        }
    }

    private suspend fun saveUsersToLocalDatabase(users: List<User>) {
        withContext(Dispatchers.IO) {
            database.userDao().insertUsers(users)
        }
    }



    private fun saveUserToLocalDatabase(user: User) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.userDao().insertUser(user)
            }
            // Mostrar un mensaje al usuario indicando que el usuario fue guardado
            Toast.makeText(this@UserActivity, "${user.name} guardado en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun openUserPosts(user: User) {
        val intent = Intent(this, PostActivity::class.java).apply {
            putExtra("USER_ID", user.id)
        }
        startActivity(intent)
    }

}
