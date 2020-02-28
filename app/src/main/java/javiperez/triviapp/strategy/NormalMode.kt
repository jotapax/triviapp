package javiperez.triviapp.strategy

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Difficulty
import javiperez.triviapp.model.Question
import javiperez.triviapp.model.Score
import javiperez.triviapp.utils.OpenTriviaHelper
import javiperez.triviapp.viewmodel.TriviaViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class NormalMode(
    private val activity: AppCompatActivity,
    private val listener: GameListener,
    private val amount: Int,
    private val category: Category,
    private val difficulty: String
) : GameMode(listener) {

    private var questions: List<Question>? = null
    private var questionPosition = -1

    private var points = 0
    private val correctAnswers = IntArray(3)
    private val wrongAnswers = IntArray(3)
    private val noAnswers = IntArray(3)


    override fun retrieveTrivia() {

        if (questions == null) {

            doAsync {

                questions =
                    OpenTriviaHelper.getQuestionFromUrl(activity, amount, category.id, difficulty, false)

                uiThread {

                    if (questions == null) listener.onGameInitError() else {


                        currentQuestion = questions!![questionPosition]
                        listener.onGameInitSuccess()
                    }
                }

            }

        } else {

            currentQuestion = questions!![questionPosition]
            listener.onGameInitSuccess()
        }
    }

    override fun getQuestionNumber() = questionPosition + 1

    override fun getTotalQuestionNumer() = questions!!.size

    override fun getPoints() = points

    override fun checkResult(answerOfUser: String?, timeLeft: Int) : Boolean {

        when (answerOfUser) {

            //UNANSWERED

            null -> {
                noAnswers[Difficulty.asList().indexOf(currentQuestion.difficulty)]++
                return false
            }

            //CORRECT ANSWER

            currentQuestion.correct_answer -> {

                points += timeLeft
                correctAnswers[Difficulty.asList().indexOf(currentQuestion.difficulty)]++
                return true

            }

            //WRONG ANSWER

            else -> {
                wrongAnswers[Difficulty.asList().indexOf(currentQuestion.difficulty)]++
                return false
            }
        }
    }

    override fun nextQuestion(): Boolean {

        questionPosition++

        if (questionPosition < amount) {

            return true
        }

        return false
    }

    override fun getScore() : Score {

        return Score(
            0,
            Score.NORMAL_MODE,
            points,
            amount * 20,
            ((points.toFloat()) / (amount * 20) * 100).toInt(),
            amount,
            category.id,
            difficulty,
            correctAnswers,
            wrongAnswers,
            noAnswers,
            Calendar.getInstance()
        )
    }
}