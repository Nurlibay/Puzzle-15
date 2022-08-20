package uz.unidev.numberpuzzle15.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResult(result: Result)

    @Query("SELECT * FROM result_table")
    fun getAllResults(): MutableList<Result>

}