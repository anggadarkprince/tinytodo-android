package com.anggaari.tinytodo.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todos")
@Parcelize
data class TodoData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String,
): Parcelable