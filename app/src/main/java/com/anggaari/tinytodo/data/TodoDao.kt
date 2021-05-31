package com.anggaari.tinytodo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anggaari.tinytodo.data.models.TodoData

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY id ASC")
    fun getAllData(): LiveData<List<TodoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(todoData: TodoData)

    @Update
    suspend fun updateData(todoData: TodoData)

    @Delete
    suspend fun deleteData(todoData: TodoData)

    @Query("DELETE FROM todos")
    suspend fun deleteAll()

    @Query("SELECT * FROM todos WHERE title LIKE :searchQuery")
    fun search(searchQuery: String): LiveData<List<TodoData>>

    @Query("SELECT * FROM todos ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<TodoData>>

    @Query("SELECT * FROM todos ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<TodoData>>
}