package uz.unidev.numberpuzzle15.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result_table")
data class Result(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var time: String,
    var moves: Int
)