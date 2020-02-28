package javiperez.triviapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Score
import javiperez.triviapp.utils.CategoryHelper
import javiperez.triviapp.viewmodel.TriviaViewModel
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.activity_result.categoryIcon
import kotlinx.android.synthetic.main.activity_result.toolbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var viewModel : TriviaViewModel

    private lateinit var score : Score
    private lateinit var category : Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
                        .create(TriviaViewModel::class.java)

        val bundle = intent.extras!!
        score = bundle.getParcelable("SCORE")!!
        showResults()

        doAsync {

            category = viewModel.getCategoryByID(score.categoryId)

            uiThread {

                showCategory()

                againButton.setOnClickListener { tryAgain() }

                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun showResults() {

        val format = SimpleDateFormat("H:mm, d MMM yyyy", Locale.ENGLISH)
        date.text = format.format(score.calendar.time)

        pointsText.text = "${score.points} points"

        val rows = listOf(easyRow, mediumRow, difficultRow)
        val correct = listOf(easyCorrect, mediumCorrect, difficultCorrect )
        val wrong = listOf(easyWrong, mediumWrong, difficultWrong)
        val noAnswer = listOf(easyNoAnswer, mediumNoAnswer, difficultNoAnswer)

        val correctAnswers = score.correct
        val wrongAnswers = score.wrong
        val noAnswers = score.noAnswer

        for (i in 0 until 3) {

            if (correctAnswers[i] == 0 && wrongAnswers[i] == 0 && noAnswers[i] == 0) {

                rows[i].visibility = View.GONE

            } else {

                correct[i].text = correctAnswers[i].toString()
                wrong[i].text = wrongAnswers[i].toString()
                noAnswer[i].text = noAnswers[i].toString()
            }
        }

        questionText.text = "${score.numberQuestions} preguntas"
        maxPointsText.text = "out of a maximum of ${score.totalPoints}"
    }

    private fun showCategory() {
        categoryIcon.setImageResource(category.icon)
        categoryName.text = CategoryHelper.prettyName(category.name)
        againButton.setBackgroundColor(Color.parseColor(category.color))
        toolbar.setBackgroundColor(Color.parseColor(category.color))
        window.statusBarColor = Color.parseColor(category.color)
    }

    private fun goToCategoryActivity() {

        val bundle = Bundle()
        bundle.putParcelable("CATEGORY", category)

        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun tryAgain() {

        val bundle = Bundle()
        bundle.putParcelable("CATEGORY", category)
        bundle.putString("DIFFICULTY", score.difficulty)
        bundle.putInt("AMOUNT", score.numberQuestions)
        bundle.putBoolean("GAME_MODE", score.mode == Score.NORMAL_MODE)

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToCategoryActivity()
    }

    override fun onSupportNavigateUp(): Boolean {
        goToCategoryActivity()
        return true
    }
}