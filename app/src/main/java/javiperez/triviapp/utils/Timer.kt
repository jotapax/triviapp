package javiperez.triviapp.utils

import android.os.CountDownTimer

class Timer(val countingDown: CountingDown) : CountDownTimer(20000, 100) {

    override fun onTick(millisUntilFinished: Long) {
        countingDown.onTick((millisUntilFinished / 100).toInt())
    }

    override fun onFinish() {
        countingDown.onFinish()
    }
}