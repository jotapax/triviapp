package javiperez.triviapp.model

import java.util.*

object Difficulty {

    const val ANY = "Any"
    const val EASY = "Easy"
    const val MEDIUM = "Medium"
    const val HARD = "Hard"

    fun asList() = listOf(EASY.toLowerCase(Locale.ROOT),
                        MEDIUM.toLowerCase(Locale.ROOT),
                        HARD.toLowerCase(Locale.ROOT))
}