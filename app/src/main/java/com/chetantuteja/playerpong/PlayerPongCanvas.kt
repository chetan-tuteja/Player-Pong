package com.chetantuteja.playerpong

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import stanford.androidlib.graphics.*

class PlayerPongCanvas(context: Context, attrs: AttributeSet)
    : GCanvas(context, attrs)  {

    private val SIZE = 100f
    private var ball = GOval(10f,10f, SIZE, SIZE)
    private var paddle = GRect(5f, 0f, 50f, 250f)
    private var dx = 15f
    private var dy = 9f
    private var score = 0
    private var highScoreGame = 0
    private lateinit var scoreLabel: GLabel
    private lateinit var highScore: GLabel
    private lateinit var ballSprite: GSprite
    private lateinit var rectSprite: GSprite
    private var ignorePaddle = false

    override fun init() {
        //GSprite.setDebug(true)
        backgroundColor = GColor.makeColor(245,245,220)


        ballSprite = GSprite(ball)
        ballSprite.velocityX = dx
        ballSprite.velocityY = dy
        ballSprite.setAcceleration(0.1f, 0.1f)
        ballSprite.fillColor = GColor.RED
        add(ballSprite)

        rectSprite = GSprite(paddle)
        rectSprite.fillColor = GColor.BLACK
        rectSprite.y = (height/2).toFloat() - rectSprite.height/2
        //rectSprite.collisionMarginLeft = rectSprite.width
        add(rectSprite)

        setupScore()

        setOnTouchListener { _, motionEvent ->
            handleTouchEvent(motionEvent)
            true
        }

    }

    private fun setupScore() {
        scoreLabel = GLabel()
        highScore = GLabel()


        updateScore()

        scoreLabel.color = GColor.BLACK
        scoreLabel.fontSize = 60f
        scoreLabel.rightX = (width -30).toFloat()
        add(scoreLabel)

        highScore.color = GColor.RED
        highScore.fontSize = 50f
        highScore.x = (width/2).toFloat() - highScore.width/2
        highScore.y = height.toFloat() - highScore.height/2 - 50
        add(highScore)
    }

    private fun updateScore() {
        scoreLabel.text = "Score: $score"
        highScore.text = "HighScore: $highScoreGame"
    }

    private fun handleTouchEvent(motionEvent: MotionEvent) {
        val x = motionEvent.x
        val y = motionEvent.y
        if(motionEvent.action == MotionEvent.ACTION_DOWN) {
            if(y < rectSprite.y) {
                rectSprite.velocityY = -0.3f
                rectSprite.accelerationY = -2.5f
            }
            else if( y> rectSprite.y) {
                rectSprite.velocityY = 0.3f
                rectSprite.accelerationY = 2.5f
            }
        } else if(motionEvent.action == MotionEvent.ACTION_UP) {
            rectSprite.velocityY = 0f
            rectSprite.accelerationY = 0f
        }
    }

    private fun collideCheck() {
        if(!ignorePaddle) {
            if(ballSprite.collidesWith(rectSprite)) {
                ballSprite.flipVelocityX()
            }
        }
    }

    fun stopGame() {
        animationStop()
    }

    fun startGame() {
        animate(60) {
            ballCheck()
            collideCheck()
            paddleCheck()
        }
    }

    fun getHighScore(): Int {
        return highScoreGame
    }

    fun restoreHighScore(hs: Int) {
        highScoreGame = hs
    }




    private fun ballCheck() {
            ballSprite.update()

      if(ballSprite.rightX >= width) {
          ballSprite.flipVelocityX()
          ignorePaddle = false
          score++
          if(score > highScoreGame) {
              highScoreGame = score
          }
          updateScore()
      }
      if(ballSprite.leftX <=0) {
          ballSprite.flipVelocityX()
          ignorePaddle = true
          score--
          updateScore()
      }

        if(ballSprite.bottomY >= height || ballSprite.topY <= 0) {
            ballSprite.flipVelocityY()
        }

    }

    private fun paddleCheck() {
        rectSprite.update()
        if(rectSprite.bottomY >= height) {
            rectSprite.velocityY = -3f
        }
        if(rectSprite.topY <= 0) {
            rectSprite.velocityY = 3f
        }
    }


}