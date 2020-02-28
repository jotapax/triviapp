package javiperez.triviapp.utils

import android.widget.ImageView
import javiperez.triviapp.model.Category

interface ClickableCategory {

    fun onClick(category : Category, icon : ImageView)
}