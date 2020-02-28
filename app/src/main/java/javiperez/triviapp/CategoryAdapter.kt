package javiperez.triviapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import javiperez.triviapp.model.Category
import javiperez.triviapp.utils.CategoryHelper
import javiperez.triviapp.utils.ClickableCategory
import kotlinx.android.synthetic.main.category.view.*


class CategoryAdapter(private val ctx : Context, val listener : ClickableCategory)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categories = emptyList<Category>()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bind(category: Category) {

            itemView.categoryName.text = CategoryHelper.prettyName(category.name)
            itemView.categoryCard.setCardBackgroundColor(Color.parseColor(category.color))

            itemView.categoryImage.setImageResource(category.icon)

            itemView.categoryCard.setOnClickListener {
                listener.onClick(category, itemView.categoryImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size

    fun updateCategories(categories : List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }
}