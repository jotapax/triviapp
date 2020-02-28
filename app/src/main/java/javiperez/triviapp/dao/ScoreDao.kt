package javiperez.triviapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Score

@Dao
interface ScoreDao {

    @Insert
    fun insert(score: Score)

    @Query("SELECT * FROM score WHERE categoryId = :categoryId AND mode = :mode ORDER BY percentage DESC, points DESC")
    fun getScores(categoryId: Int, mode: Int): LiveData<List<Score>>

}