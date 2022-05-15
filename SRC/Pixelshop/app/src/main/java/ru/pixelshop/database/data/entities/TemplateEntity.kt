package ru.pixelshop.database.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TemplateData")
data class TemplateEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "height")
    var height: Int,

    @ColumnInfo(name = "width")
    var width: Int,

    @ColumnInfo(name = "matrix")
    var matrix: String,
)