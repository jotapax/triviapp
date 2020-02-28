package javiperez.triviapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.model.Category
import javiperez.triviapp.strategy.CompetitionMode
import javiperez.triviapp.strategy.GameListener
import javiperez.triviapp.strategy.GameMode
import javiperez.triviapp.strategy.NormalMode
import javiperez.triviapp.utils.CategoryHelper
import javiperez.triviapp.utils.CountingDown
import javiperez.triviapp.utils.Timer
import javiperez.triviapp.viewmodel.TriviaViewModel
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.coroutines.*
import org.jetbrains.anko.*
import kotlin.math.ceil

class QuizActivity : AppCompatActivity(), CountingDown, GameListener {

    private lateinit var answerCards : List<CardView>
    private lateinit var answerTexts : List<TextView>
    private lateinit var timer : Timer
    private var timeleft = 0

    private lateinit var strategy : GameMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        setSupportActionBar(toolbar)

        val bundle = intent.extras!!
        val category = bundle.getParcelable<Category>("CATEGORY")!!
        val difficulty = bundle.getString("DIFFICULTY")!!
        val amount = bundle.getInt("AMOUNT")
        val gameMode = bundle.getBoolean("GAME_MODE")

        if (gameMode) strategy = NormalMode(this, this, amount, category, difficulty)
        else strategy = CompetitionMode(this, this, category)

        timer = Timer(this)

        answerCards = listOf(answer1, answer2, answer3, answer4)
        answerTexts = listOf(answerText1, answerText2, answerText3, answerText4)



        showCategory(category)

        startNewIteration()
    }

    private fun startNewIteration() {

        setVisibility(View.INVISIBLE)
        hideAnswers()

        progressBar.visibility = View.VISIBLE

        if (strategy.nextQuestion()) {

            strategy.retrieveTrivia()

        } else {

            finishTrivia()
        }
    }

    override fun onGameInitError() {

        progressBar.visibility = View.GONE

        questionText.text = "Unfortunately, some sort of error has occurred, " +
                "please try again. Maybe you could check your internet connection"

        errorButton.setOnClickListener { goBackToMainActivity() }

        questionText.visibility = View.VISIBLE
        errorButton.visibility = View.VISIBLE
    }

    override fun onGameInitSuccess() {

        progressBar.visibility = View.GONE

        setVisibility(View.VISIBLE)

        showQuestion()
    }

    private fun showQuestion() {

        hideAnswers()

        val currentQuestion = strategy.getQuestion()
        val currentQuestionNumber = strategy.getQuestionNumber()
        val totalQuestionNumber = strategy.getTotalQuestionNumer()

        Log.d("TRIVIA", currentQuestion)

        number.text = "$currentQuestionNumber/$totalQuestionNumber"
        number.visibility = View.VISIBLE

        questionText.text = currentQuestion

        onTick(200)

        //WAIT 2 SECONDS SO USER CAN READ THE QUESTION

        CoroutineScope(Dispatchers.IO).launch {

            delay(2000)

            withContext(Dispatchers.Main) {

                showAnswers()
            }
        }
    }

    private fun showAnswers() {

        val answers = strategy.getAllAnswers()

        Log.d("TRIVIA", strategy.getCorrectAnswer())

        if (strategy.getQuestionType()) {

            //LAYOUT FOR MULTIPLE QUESTIONS

            for (i in 0 until 4) {

                answerCards[i].visibility = View.VISIBLE

                answerCards[i].background = getDrawable(R.drawable.card_border)

                answerTexts[i].text = answers[i]
            }

        } else {

            //LAYOUT FOR TRUE/FALSE QUESTIONS

            answerTexts[0].text = "True"
            answerTexts[1].text = "False"

            for (i in 0 until 4) {

                if (i in 0..1) {
                    answerCards[i].visibility = View.VISIBLE
                } else {
                    answerCards[i].visibility = View.INVISIBLE
                }

                answerCards[i].background = getDrawable(R.drawable.card_border)
            }
        }

        allowUserSelection(true)

        timer.start()
    }

    private fun answerFromUser(position : Int, response : String) {

        val result = strategy.checkResult(response, timeleft)

        if (result) {

            //CORRECT ANSWER

            answerCards[position].background = getDrawable(R.drawable.green_border)

            pointsText.text = strategy.getPoints().toString()

        } else  {

            //WRONG ANSWER

            answerCards[position].background = getDrawable(R.drawable.red_border)

            val allAnswers = strategy.getAllAnswers()
            val correctAnswer = strategy.getCorrectAnswer()

            val correctPosition = allAnswers.indexOf(correctAnswer)

            answerCards[correctPosition].background = getDrawable(R.drawable.green_border)
        }

        waitUserCanSeeTheResult()
    }

    override fun onFinish() {

        //UNANSWERED
        strategy.checkResult(null, 0)

        waitUserCanSeeTheResult()
    }

    private fun waitUserCanSeeTheResult() {

        allowUserSelection(false)

        timer.cancel()

        CoroutineScope(Dispatchers.IO).launch {

            delay(1500)

            withContext(Dispatchers.Main) {

                startNewIteration()
            }
        }
    }

    private fun finishTrivia() {

        val score = strategy.getScore()

        val viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(TriviaViewModel::class.java)

        viewModel.addScore(score)

        val bundle = Bundle()
        bundle.putParcelable("SCORE", score)

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onTick(remainingTime : Int) {

        timeleft = ceil(remainingTime.toFloat().div(10)).toInt()
        seconds.text = timeleft.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countDownBar.setProgress(remainingTime, true)
        } else {
            countDownBar.progress = remainingTime
        }
    }

    private fun setVisibility(visibility : Int) {
        categoryIcon.visibility = visibility
        questionText.visibility = visibility
    }

    private fun showCategory(category: Category) {
        categoryIcon.setImageResource(category.icon)
        supportActionBar?.title = CategoryHelper.prettyName(category.name)
        toolbar.setBackgroundColor(Color.parseColor(category.color))
        window.statusBarColor = Color.parseColor(category.color)
    }

    private fun hideAnswers() {
        for (i in 0 until 4) answerCards[i].visibility = View.INVISIBLE
    }

    private fun allowUserSelection(boolean : Boolean) {

        if (boolean) {

            for (i in 0 until 4) {

                answerCards[i].setOnClickListener {

                    answerFromUser(i, answerTexts[i].text.toString())
                }
            }

        } else {

            for (i in 0 until 4) answerCards[i].setOnClickListener {}
        }
    }


    override fun onBackPressed() {
        alert("You will lose all your progress in this quiz") {
            title = "Are you sure?"
            yesButton { goBackToMainActivity() }
            noButton { }
        }.show()
    }

    private fun goBackToMainActivity() {
        timer.cancel()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }
}