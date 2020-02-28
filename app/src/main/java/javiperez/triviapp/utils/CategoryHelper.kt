package javiperez.triviapp.utils

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.R
import javiperez.triviapp.model.Category
import javiperez.triviapp.viewmodel.TriviaViewModel

class CategoryHelper {

    companion object {

        fun prettyName(oldName : String): String {

            val name = oldName.replace("Entertainment: ", "")
            return name.replace("Science: ", "")
        }

        fun insertAll(app : Application) {

            val viewModel =
                ViewModelProvider.AndroidViewModelFactory(app).create(TriviaViewModel::class.java)

            var category = Category(9, 100, 114, 53,"General Knowledge", "#DFCE7B", R.drawable.knowledge)
            viewModel.addCategory(category)

            category = Category(10, 27, 34, 33, "Entertainment: Books", "#DD5355", R.drawable.book)
            viewModel.addCategory(category)

            category = Category(11, 82, 96, 37, "Entertainment: Film", "#41464D", R.drawable.film)
            viewModel.addCategory(category)

            category = Category(12, 96, 165, 61,"Entertainment: Music", "#FF93C4", R.drawable.music)
            viewModel.addCategory(category)

            category = Category(14, 63, 62, 27, "Entertainment: Television", "#A8D3D8", R.drawable.television)
            viewModel.addCategory(category)

            category = Category(13,6, 10, 8, "Entertainment: Musicals & Theatres", "#DF6169", R.drawable.theatre)
            viewModel.addCategory(category)

            category = Category(15, 275, 389, 160, "Entertainment: Video Games", "#73DA8C", R.drawable.videogame)
            viewModel.addCategory(category)

            category = Category(16, 19, 14, 25, "Entertainment: Board Games", "#FECB6E", R.drawable.boardgame)
            viewModel.addCategory(category)

            category = Category(17, 56, 84, 55, "Science & Nature", "#21DFA6", R.drawable.science)
            viewModel.addCategory(category)

            category = Category(18, 45, 68, 24, "Science: Computers", "#69B0FF", R.drawable.computer)
            viewModel.addCategory(category)

            category = Category(19, 15, 19, 11,"Science: Mathematics", "#B6CDEC", R.drawable.math)
            viewModel.addCategory(category)

            category = Category(20, 18, 20, 11, "Mythology", "#B98050", R.drawable.myth)
            viewModel.addCategory(category)

            category = Category(21, 39, 51, 14, "Sports", "#C3BCB8", R.drawable.sports)
            viewModel.addCategory(category)

            category = Category(22, 76, 123, 51, "Geography", "#20AA49", R.drawable.geography)
            viewModel.addCategory(category)

            category = Category(23, 59, 141, 60, "History", "#FECF40", R.drawable.history)
            viewModel.addCategory(category)

            category = Category(24, 18, 22, 13, "Politics", "#DFAB60", R.drawable.politics)
            viewModel.addCategory(category)

            category = Category(25, 8, 10, 8, "Art", "#B59472", R.drawable.art)
            viewModel.addCategory(category)

            category = Category(26, 12, 26, 8, "Celebrities", "#D45E5E", R.drawable.celebrities)
            viewModel.addCategory(category)

            category = Category(27, 25, 28, 16, "Animals", "#BA997F", R.drawable.animals)
            viewModel.addCategory(category)

            category = Category(28, 19, 31, 17,"Vehicles", "#8FB52B", R.drawable.vehicle)
            viewModel.addCategory(category)

            category = Category(29, 9, 23, 13, "Entertainment: Comics", "#8BC3FF", R.drawable.comic)
            viewModel.addCategory(category)

            category = Category(30, 9, 8, 4,"Science: Gadgets", "#5A8CDB", R.drawable.gadget)
            viewModel.addCategory(category)

            category = Category(31, 53, 71, 37,"Entertainment: Japanese Anime & Manga", "#74A4B5", R.drawable.manga)
            viewModel.addCategory(category)

            category = Category(32, 30, 38, 16,"Entertainment: Cartoon & Animations", "#DDAC5E", R.drawable.cartoon)
            viewModel.addCategory(category)
        }
    }
}

/*

  {
      "trivia_categories": [
        {
          "id": 9,
          "name": "General Knowledge"
        },
        {
          "id": 10,
          "name": "Entertainment: Books"
        },
        {
          "id": 11,
          "name": "Entertainment: Film"
        },
        {
          "id": 12,
          "name": "Entertainment: Music"
        },
        {
          "id": 13,
          "name": "Entertainment: Musicals & Theatres"
        },
        {
          "id": 14,
          "name": "Entertainment: Television"
        },
        {
          "id": 15,
          "name": "Entertainment: Video Games"
        },
        {
          "id": 16,
          "name": "Entertainment: Board Games"
        },
        {
          "id": 17,
          "name": "Science & Nature"
        },
        {
          "id": 18,
          "name": "Science: Computers"
        },
        {
          "id": 19,
          "name": "Science: Mathematics"
        },
        {
          "id": 20,
          "name": "Mythology"
        },
        {
          "id": 21,
          "name": "Sports"
        },
        {
          "id": 22,
          "name": "Geography"
        },
        {
          "id": 23,
          "name": "History"
        },
        {
          "id": 24,
          "name": "Politics"
        },
        {
          "id": 25,
          "name": "Art"
        },
        {
          "id": 26,
          "name": "Celebrities"
        },
        {
          "id": 27,
          "name": "Animals"
        },
        {
          "id": 28,
          "name": "Vehicles"
        },
        {
          "id": 29,
          "name": "Entertainment: Comics"
        },
        {
          "id": 30,
          "name": "Science: Gadgets"
        },
        {
          "id": 31,
          "name": "Entertainment: Japanese Anime & Manga"
        },
        {
          "id": 32,
          "name": "Entertainment: Cartoon & Animations"
        }
      ]
    }
 */