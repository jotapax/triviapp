package javiperez.triviapp.utils

interface CountingDown {

    fun onTick(remainingTime : Int)

    fun onFinish()
}