package ru.pixelshop.database.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProjectData")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "height")
    var height: Int,

    @ColumnInfo(name = "width")
    var width: Int,

    @ColumnInfo(name = "preview")
    var preview: String,

    @ColumnInfo(name = "frames")
    var frames: String,

    @ColumnInfo(name = "lastModified")
    var lastModified: String
)

