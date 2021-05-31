package com.anggaari.tinytodo.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.anggaari.tinytodo.data.TodoDatabase
import com.anggaari.tinytodo.data.models.TodoData
import com.anggaari.tinytodo.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getDatabase(application).todoDao()
    private val repository: TodoRepository

    val getAllData: LiveData<List<TodoData>>

    init {
        repository = TodoRepository(todoDao)
        getAllData = repository.getAllData
    }

    fun insertData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(todoData)
        }
    }

    fun updateData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(todoData)
        }
    }

    fun deleteData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(todoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun search(searchQuery: String): LiveData<List<TodoData>> {
        return repository.search(searchQuery)
    }
}