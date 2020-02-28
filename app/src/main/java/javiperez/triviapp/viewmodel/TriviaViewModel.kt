package javiperez.triviapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import javiperez.triviapp.db.TriviaDatabase
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Score
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TriviaViewModel(app : Application) : AndroidViewModel(app) {

    private val db = TriviaDatabase.getInstance(app.applicationContext)

    private val categoryDao = db.categoryDao()

    private val scoreDao = db.scoreDao()

    fun getAllCategories() = categoryDao.getAll()

    fun getCategory(name : String) = categoryDao.get(name)

    fun getCategoryByID(id : Int) = categoryDao.getByID(id)

    fun addCategory(category: Category) {

        Log.d("TRIVIA", "Inserting ${category.name}")

        GlobalScope.launch {

            categoryDao.insert(category)
        }
    }

    fun addScore(score : Score) {

        Log.d("TRIVIA", "Inserting new score")

        GlobalScope.launch {

            scoreDao.insert(score)
        }
    }

    fun getScores(category: Category, mode : Int) = scoreDao.getScores(category.id, mode)
}