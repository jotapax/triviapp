package javiperez.triviapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import javiperez.triviapp.dao.CategoryDao
import javiperez.triviapp.dao.ScoreDao
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Score

@Database(entities = [Category::class, Score::class], version = 1)
abstract class TriviaDatabase : RoomDatabase() {

    abstract fun categoryDao() : CategoryDao

    abstract fun scoreDao() : ScoreDao

    companion object {

        @Volatile private var INSTANCE : TriviaDatabase? = null

        fun getInstance (ctx : Context) : TriviaDatabase {

            if (INSTANCE == null) {
                synchronized(TriviaDatabase::class) {

                    INSTANCE = Room.databaseBuilder(
                        ctx.applicationContext, TriviaDatabase::class.java, "TriviaBD").build()
                }
            }

            return INSTANCE!!
        }
    }
}