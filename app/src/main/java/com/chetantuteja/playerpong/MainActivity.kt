package com.chetantuteja.playerpong

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getPreferences(Context.MODE_PRIVATE)
        val hs = prefs.getInt("highscore", 0)
        mycanvas.restoreHighScore(hs)
    }

    fun playClick(view: View){
        mycanvas.startGame()
    }

    fun stopClick(view: View){
        mycanvas.stopGame()
    }

    fun replayClick(view: View){

        val intent = intent
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        //finish()
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        val hs = mycanvas.getHighScore()
            val prefs = getPreferences(Context.MODE_PRIVATE)
            val prefsEditor = prefs.edit()
            prefsEditor.putInt("highscore", hs)
            prefsEditor.apply()
        }

    }

