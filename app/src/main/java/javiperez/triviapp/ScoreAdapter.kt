package javiperez.triviapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javiperez.triviapp.model.Score
import javiperez.triviapp.utils.ClickableScore
import kotlinx.android.synthetic.main.score.view.*
import java.text.SimpleDateFormat
import java.util.*

class ScoreAdapter(private val ctx : Context, val listener : ClickableScore)
    : RecyclerView.Adapter<ScoreAdapter.ViewHolder>() {

    private var scores = emptyList<Score>()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(score: Score) {

            val format = SimpleDateFormat("H:mm, d MMM yyyy", Locale.ENGLISH)
            itemView.date.text = format.format(score.calendar.time)

            itemView.pointsText.text = "${score.points} points"

            itemView.percentText.text = "${score.percentage}%"

            itemView.scoreCard.setOnClickListener {
                listener.onClick(score)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.score, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scores[position])
    }

    override fun getItemCount() = scores.size

    fun updateScores(scores : List<Score>) {
        this.scores = scores
        notifyDataSetChanged()
    }
}