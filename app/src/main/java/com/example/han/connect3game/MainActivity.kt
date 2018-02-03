package com.example.han.connect3game

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val DROP_IN_DELAY: Long = 200L
    var messageToast: Toast? = null

    // 0 = yellow, 1 = red, 2 = empty
    var playerTurn = 0
    var gameState = mutableListOf<Int>(2, 2, 2, 2, 2, 2, 2, 2, 2)
    val winningPositions: MutableList<MutableList<Int>> = mutableListOf(
            mutableListOf(0, 1, 2), mutableListOf(3, 4, 5) ,mutableListOf(6, 7, 8),
            mutableListOf(0, 3, 6), mutableListOf(1, 4, 7), mutableListOf(2, 5, 8),
            mutableListOf(0, 4, 8), mutableListOf(2, 4, 6))
    var isWinnerFound: Boolean = false; var isGameover: Boolean = false
    var winner: Int = 2; var isGameOver: Boolean = false

    fun playAgain(view: View) {
        init()
        messageToast?.cancel()
        winnerText.setText("")
        winnerText.setVisibility(View.INVISIBLE)
        playAgainButton.setVisibility(View.INVISIBLE)
    }

    fun init() {
        // 0 = yellow, 1 = red, 2 = empty
        playerTurn = 0
        for (i in gameState.indices) {
            gameState[i] = 2
        }

        isWinnerFound = false; isGameover = false
        winner = 2; isGameOver = false

        for (i in 0 until grid.getChildCount()) {
            val counter: ImageView = grid.getChildAt(i) as ImageView
            counter.setImageDrawable(null)
        }
    }


    fun checkWinnerFound(): Boolean {
        for (row in winningPositions) {
            if (gameState[row[0]] == gameState[row[1]] && gameState[row[1]] == gameState[row[2]] && gameState[row[2]] != 2) {
                winner = if (gameState[row[0]] == 0) 0 else 1
                isGameOver = true
                return true
            }
        }
        return false
    }

    fun checkGameOver(): Boolean {
        for (pos in gameState) {
            if (pos == 2) {
                return false
            }
        }
        isGameOver = true
        return true
    }

    fun dropIn(view: View) {
        val counter: ImageView = view as ImageView
        val counterTag: Int = counter.getTag().toString().toInt()
        val counterId: Int = counter.getId()

        Log.i("Info", "tag: $counterTag, id: $counterId")
        if (gameState[counterTag] == 2 && !isGameOver) {
            counter.setTranslationY(-2000f)
            if (playerTurn == 0) {
                counter.setImageResource(R.drawable.yellow)
                gameState.set(counterTag, playerTurn)
                playerTurn = 1
            } else {
                counter.setImageResource(R.drawable.red)
                gameState.set(counterTag, playerTurn)
                playerTurn = 0
            }
            counter.animate().translationYBy(2000f).rotation(360f).setDuration(DROP_IN_DELAY)

            var message: String = ""
            if (checkWinnerFound()) {
                message = "Player $winner is the winner!"
            } else if (checkGameOver()) {
                message = "It's a draw!"
            }
            if (isGameOver) {
                Log.i("Info", message)
                Handler().postDelayed({
                    messageToast = Toast.makeText(this, message, Toast.LENGTH_LONG)
                    messageToast?.show()
                    winnerText.setText(message)
                    winnerText.setVisibility(View.VISIBLE)
                    playAgainButton.setVisibility(View.VISIBLE)
                }, DROP_IN_DELAY)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
