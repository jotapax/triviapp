package javiperez.triviapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.model.Category
import javiperez.triviapp.model.Difficulty
import javiperez.triviapp.model.Score
import javiperez.triviapp.utils.CategoryHelper
import javiperez.triviapp.utils.ClickableScore
import javiperez.triviapp.viewmodel.TriviaViewModel
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, ClickableScore{

    private lateinit var category: Category

    private var difficultyOptions = listOf(Difficulty.ANY, Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)
    private val numberOfQuestions = listOf(5, 10, 15, 20)

    private var normalMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras!!
        category = bundle.getParcelable("CATEGORY")!!

        categoryIcon.setImageResource(category.icon)
        supportActionBar?.title = CategoryHelper.prettyName(category.name)
        toolbar.setBackgroundColor(Color.parseColor(category.color))
        window.statusBarColor = Color.parseColor(category.color)
        playButton.setBackgroundColor(Color.parseColor(category.color))
        bestResultCard.setBackgroundColor(Color.parseColor(category.color))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyOptions)
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        difficultySelect.adapter = adapter

        difficultySelect.onItemSelectedListener = this

        val adapterNumber = ArrayAdapter(this, android.R.layout.simple_spinner_item, numberOfQuestions)
        adapterNumber.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        numberSelect.adapter = adapterNumber

        playButton.setOnClickListener { playQuiz() }

        setGameMode()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        showLatestResults()
    }

    private fun setGameMode() {

        if (normalMode) {

            normalModeButton.setBackgroundColor(Color.parseColor(category.color))
            compModeButton.setBackgroundColor(Color.parseColor("#868686"))
            compModeButton.setOnClickListener {
                normalMode = false
                setGameMode()
            }
            normalModeButton.setOnClickListener {  }
            labelDifficulty.visibility = View.VISIBLE
            difficultySelect.visibility = View.VISIBLE
            labelNumber.visibility = View.VISIBLE
            numberSelect.visibility = View.VISIBLE
            geniusText.visibility = View.INVISIBLE
            showLatestResults()

        } else {

            compModeButton.setBackgroundColor(Color.parseColor(category.color))
            normalModeButton.setBackgroundColor(Color.parseColor("#868686"))
            normalModeButton.setOnClickListener {
                normalMode = true
                setGameMode()
            }
            compModeButton.setOnClickListener {  }
            labelDifficulty.visibility = View.INVISIBLE
            difficultySelect.visibility = View.INVISIBLE
            labelNumber.visibility = View.INVISIBLE
            numberSelect.visibility = View.INVISIBLE
            geniusText.visibility = View.VISIBLE
            showLatestResults()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val count = when(difficultyOptions[position]) {
            Difficulty.ANY -> category.easy + category.medium + category.hard
            Difficulty.EASY -> category.easy
            Difficulty.MEDIUM -> category.medium
            Difficulty.HARD -> category.hard
            else -> 0
        }

        val possibleNumber = mutableListOf<Int>()
        for (number in numberOfQuestions) {
            if (number <= count) possibleNumber.add(number)
        }

        val adapterNumber = ArrayAdapter(this, android.R.layout.simple_spinner_item, possibleNumber)
        adapterNumber.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        numberSelect.adapter = adapterNumber
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }

    private fun playQuiz() {

        val difficulty = difficultySelect.selectedItem as String
        val amount = numberSelect.selectedItem as Int

        val bundle = intent.extras!!
        bundle.putString("DIFFICULTY", difficulty)
        bundle.putInt("AMOUNT", amount)
        bundle.putBoolean("GAME_MODE", normalMode)

        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun showLatestResults() {

        val adapter = ScoreAdapter(this, this)

        recyclerView.adapter = adapter

        val viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(TriviaViewModel::class.java)

        val mode = if (normalMode) Score.NORMAL_MODE else Score.COMPETITION_MODE
        val scores = viewModel.getScores(category, mode)

        scores.observe(this, Observer {

            bestResultCard.visibility = if (it.isEmpty()) View.INVISIBLE else View.VISIBLE

            adapter.updateScores(it)
        })
    }

    override fun onClick(score: Score) {
        val bundle = Bundle()
        bundle.putParcelable("SCORE", score)

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()
        return true
    }
}