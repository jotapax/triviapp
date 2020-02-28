package javiperez.triviapp.utils

import javiperez.triviapp.model.Score


interface ClickableScore {

    fun onClick(score : Score)
}