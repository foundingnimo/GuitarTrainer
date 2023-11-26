package io.nimo.music

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.nimo.music.view.StaffView

abstract class AbstractGuitarActivity : AppCompatActivity() {

    val TAG = "Music Teacher"

    // Stats variables
    var totalQuestions = 0
    var correctAnswers = 0
    var streakCounter = 0
    var currentNote: Int = -1
    var notesUtil = NotesUtil()

    lateinit var statsTextView: TextView
    lateinit var streakTextView: TextView
    lateinit var staffView: StaffView

    fun updateStats() {
        val percentage = if (totalQuestions == 0) 0 else (correctAnswers * 100) / totalQuestions
        statsTextView.text = "Attempts: $correctAnswers/$totalQuestions ($percentage%)"
        streakTextView.text = "Streak: $streakCounter"
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putInt("totalQuestions", totalQuestions)
        bundle.putInt("correctAnswers", correctAnswers)
        bundle.putInt("streakCounter", streakCounter)
        bundle.putInt("currentNote", currentNote)
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        restoreState(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { restoreState(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_staff_activity -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }

            R.id.menu_fret_activity -> {
                val intent = Intent(this, FretBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }

            R.id.menu_settings_activity -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    open fun restoreState(bundle: Bundle) {
        totalQuestions = bundle.getInt("totalQuestions")
        correctAnswers = bundle.getInt("correctAnswers")
        streakCounter = bundle.getInt("streakCounter")
        currentNote = bundle.getInt("currentNote")
        notesUtil.notes[currentNote].let {
            staffView.displayNote(it)
        }
        updateStats()
    }

    abstract fun populateQuestion()
    abstract fun checkUserGuess(userGuess: String): Boolean

    fun checkAnswer(userGuess: String): Boolean {
        totalQuestions++
        var ret = false
        if (checkUserGuess(userGuess)) {
            correctAnswers++
            streakCounter++
            populateQuestion()
            ret = true
        } else {
            streakCounter = 0
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show()
        }
        updateStats()
        return ret
    }

    fun showHints(): Boolean {
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("ShowHints", false)
    }
}