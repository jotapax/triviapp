package javiperez.triviapp.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.R
import javiperez.triviapp.model.Difficulty
import javiperez.triviapp.model.Question
import javiperez.triviapp.viewmodel.TriviaViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*

class OpenTriviaHelper {

    companion object {

        private var TOKEN_GENERATED = false

        fun getQuestionFromUrl(activity : AppCompatActivity,
                               amount : Int,
                               categoryId : Int,
                               difficulty: String,
                               generateToken : Boolean) : List<Question>? {

            if (generateToken) generateToken(activity)

            var url = "https://opentdb.com/api.php?amount=$amount&encode=base64"

            if (categoryId != 0) url = "$url&category=$categoryId"

            if (difficulty != Difficulty.ANY )
                url = "$url&difficulty=${difficulty.toLowerCase(Locale.ROOT)}"

            val urlWithToken = addTokenToUrl(activity, url)

            Log.d("TRIVIA", urlWithToken)

            val response : JSONObject

            try {

                response = JSONObject(URL(urlWithToken).readText())

            } catch (e : Exception) {

                return null
            }

            when( response.getInt("response_code") ) {

                0 -> {

                    TOKEN_GENERATED = false

                    val results : JSONArray = response.getJSONArray("results")

                    val viewModel = ViewModelProvider.AndroidViewModelFactory(activity.application)
                        .create(TriviaViewModel::class.java)

                    return createQuestions(results, viewModel)
                }

                1 -> {

                    return null
                }

                else -> {

                    if (!TOKEN_GENERATED) {

                        generateToken(activity)

                        TOKEN_GENERATED = true

                        return getQuestionFromUrl(activity, amount, categoryId, difficulty, false)

                    } else {

                        TOKEN_GENERATED = false

                        return null
                    }
                }
            }

            /*
            Code 0: Success Returned results successfully.
            Code 1: No Results Could not return results.
                    The API doesn't have enough questions for your query.
                    (Ex. Asking for 50 Questions in a Category that only has 20.)
            Code 2: Invalid Parameter Contains an invalid parameter.
                    Arguments passed in aren't valid. (Ex. Amount = Five)
            Code 3: Token Not Found Session Token does not exist.
            Code 4: Token Empty Session Token has returned all possible questions for the specified query.
                    Resetting the Token is necessary.
            */
        }

        private fun addTokenToUrl(activity: AppCompatActivity, url: String): String {

            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

            val preferenceName = activity.getString(R.string.preferences_token)

            val token = sharedPref.getString( preferenceName, "j")

            return "$url&token=$token"
        }

        private fun createQuestions(results: JSONArray, viewModel: TriviaViewModel): List<Question> {

            val questions = mutableListOf<Question>()

            for (i in 0 until results.length()) {

                val question = results.getJSONObject(i)

                val categoryName = decodeBase64(question.getString("category"))
                val category = viewModel.getCategory(categoryName)
                val multiple = decodeBase64(question.getString("type")) == "multiple"
                val difficulty = decodeBase64(question.getString("difficulty"))
                val questionText = decodeBase64(question.getString("question"))
                val correctAnswer = decodeBase64(question.getString("correct_answer"))

                val answers = mutableListOf<String>()

                if (multiple) {

                    val incorrectAnswers = question.getJSONArray("incorrect_answers")

                    for (j in 0 until incorrectAnswers.length())
                        answers.add(decodeBase64(incorrectAnswers[j].toString()))

                    val position = (0..answers.size).random()

                    answers.add(position, correctAnswer)

                } else {

                    answers.add("True")
                    answers.add("False")
                }

                Log.d("TRIVIA", category.name)

                questions.add(
                    Question(category,
                        multiple,
                        difficulty,
                        questionText,
                        answers,
                        correctAnswer)
                )
            }

            return questions
        }

        fun generateToken(activity: AppCompatActivity) {

            Log.d("TRIVIA", "Generating token...")

            val url = "https://opentdb.com/api_token.php?command=request"

            val response = JSONObject(URL(url).readText())

            if (response.getInt("response_code") != 0) {
                Log.d("TRIVIA", "Token could not be generated")
                return
            }

            val token = response.getString("token")

            val editor = activity.getPreferences(Context.MODE_PRIVATE).edit()

            val preferenceName = activity.getString(R.string.preferences_token)

            editor.putString(preferenceName, token)

            editor.apply()
        }

        private fun decodeBase64(base64 : String) : String {
            val data = Base64.decode(base64, Base64.DEFAULT)
            return String(data, charset("UTF-8"))
        }
    }
}