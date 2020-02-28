package javiperez.triviapp.strategy

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Difficulty
import javiperez.triviapp.model.Question
import javiperez.triviapp.model.Score
import javiperez.triviapp.utils.OpenTriviaHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class CompetitionMode(
    private val activity: AppCompatActivity,
    private val listener: GameListener,
    private val category: Category
) : GameMode(listener) {

    private var numberOfQuestions = 0
    private val totalNumberOfQuestions = category.hard + category.medium + category.easy
    private val correctAnswers = IntArray(3)
    private val wrongAnswers = IntArray(3)
    private val noAnswers = IntArray(3)

    private var firstTime = true
    private var finish = false

    override fun retrieveTrivia() {

        doAsync {

            val questions =
                OpenTriviaHelper.getQuestionFromUrl(activity, 1, category.id, Difficulty.ANY, firstTime)

            if (firstTime) firstTime = false

            uiThread {

                if (questions == null) {listener.onGameInitError()} else {

                    currentQuestion = questions[0]
                    listener.onGameInitSuccess()
                }
            }

        }
    }

    override fun getQuestionNumber() = numberOfQuestions

    override fun getTotalQuestionNumer() = totalNumberOfQuestions

    override fun getPoints() = numberOfQuestions

    override fun checkResult(answerOfUser: String?, timeLeft: Int) : Boolean {

        when (answerOfUser) {

            //UNANSWERED

            null -> {
                noAnswers[Difficulty.asList().indexOf(currentQuestion.difficulty)]++
                finish = true
                return false
            }

            //CORRECT ANSWER

            currentQuestion.correct_answer -> {

                numberOfQuestions++
                correctAnswers[Difficulty.asList().indexOf(currentQuestion.difficulty)]++
                return true

            }

            //WRONG ANSWER

            else -> {
                wrongAnswers[Difficulty.asList().indexOf(currentQuestion.difficulty)]++
                finish = true
                return false
            }
        }
    }

    override fun nextQuestion(): Boolean {

        if (numberOfQuestions < totalNumberOfQuestions && !finish) {

            return true
        }

        return false
    }

    override fun getScore() : Score {

        return Score(
            0,
            Score.COMPETITION_MODE,
            numberOfQuestions,
            totalNumberOfQuestions,
            numberOfQuestions/totalNumberOfQuestions*100,
            numberOfQuestions,
            category.id,
            Difficulty.ANY,
            correctAnswers,
            wrongAnswers,
            noAnswers,
            Calendar.getInstance()
        )
    }
}