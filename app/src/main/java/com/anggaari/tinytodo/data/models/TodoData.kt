package com.anggaari.tinytodo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anggaari.tinytodo.data.models.Priority

@Entity(tableName = "todos")
data class TodoData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String,
)