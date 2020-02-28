package javiperez.triviapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        openTriviaCard.setOnClickListener {
            openUrl("https://opentdb.com/")
        }

        iconsCard.setOnClickListener {
            openUrl("https://www.flaticon.com/authors/freepik")
        }

        githubCard.setOnClickListener {
            openUrl("https://github.com/jotapax/triviapp")
        }
    }

    private fun openUrl(url : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}