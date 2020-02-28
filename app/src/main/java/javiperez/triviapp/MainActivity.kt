package javiperez.triviapp

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javiperez.triviapp.model.Category
import javiperez.triviapp.utils.CategoryHelper
import javiperez.triviapp.utils.ClickableCategory
import javiperez.triviapp.viewmodel.TriviaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity(), ClickableCategory {

    private var itemClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = CategoryAdapter(this, this)

        checkFirstTimeUse()

        showCategories()
    }

    private fun checkFirstTimeUse() {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)

        val preferenceName = getString(R.string.preferences_first_run)

        if (sharedPref.getBoolean(preferenceName, false) ) return

        /* Do first run stuff */

        Log.d("TRIVIA", "This is first run")

        doAsync {

            CategoryHelper.insertAll(application)

            sharedPref.edit().putBoolean(preferenceName, true).apply()
        }
    }

    private fun showCategories() {

        val viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(TriviaViewModel::class.java)

        val categories = viewModel.getAllCategories()

        categories.observe(this, Observer {

            (recyclerView.adapter as CategoryAdapter).updateCategories(it)
        })

    }

    override fun onClick(category: Category, icon : ImageView) {

        if (itemClicked) return

        val bundle = Bundle()
        bundle.putParcelable("CATEGORY", category)

        val options = ActivityOptions
            .makeSceneTransitionAnimation(this, icon, "categoryIcon")


        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent, options.toBundle())

        itemClicked = true
    }

    override fun onResume() {
        super.onResume()
        itemClicked = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
        return false
    }

}
