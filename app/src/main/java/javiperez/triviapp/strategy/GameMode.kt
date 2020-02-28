package javiperez.triviapp.strategy

import javiperez.triviapp.model.Question
import javiperez.triviapp.model.Score


abstract class GameMode(listener: GameListener) {

    protected lateinit var currentQuestion: Question

    abstract fun retrieveTrivia()
    
    fun getQuestion() = currentQuestion.question

    fun getAllAnswers() = currentQuestion.answers

    fun getCorrectAnswer() = currentQuestion.correct_answer

    fun getQuestionType() = currentQuestion.multiple

    abstract fun getPoints() : Int

    abstract fun getQuestionNumber() : Int

    abstract fun getTotalQuestionNumer() : Int

    abstract fun checkResult(answerOfUser: String?, timeLeft: Int) : Boolean

    abstract fun nextQuestion() : Boolean

    abstract fun getScore() : Score
}