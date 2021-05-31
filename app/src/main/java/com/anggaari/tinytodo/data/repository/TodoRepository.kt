package com.anggaari.tinytodo.data.repository

import androidx.lifecycle.LiveData
import com.anggaari.tinytodo.data.TodoDao
import com.anggaari.tinytodo.data.models.TodoData

class TodoRepository(private val todoDao: TodoDao) {

    val getAllData:LiveData<List<TodoData>> = todoDao.getAllData()

    suspend fun insertData(todoData: TodoData) {
        todoDao.insertData(todoData)
    }

    suspend fun updateData(todoData: TodoData) {
        todoDao.updateData(todoData)
    }

    suspend fun deleteData(todoData: TodoData) {
        todoDao.deleteData(todoData)
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    fun search(searchQuery: String): LiveData<List<TodoData>> {
        return todoDao.search(searchQuery)
    }
}