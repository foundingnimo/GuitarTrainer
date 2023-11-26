package io.nimo.music

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.graphics.Color

class MainActivity : AbstractGuitarActivity() {

    private lateinit var buttonOption1: Button
    private lateinit var buttonOption2: Button
    private lateinit var buttonOption3: Button
    private lateinit var buttonOption4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statsTextView = findViewById(R.id.statsTextView)
        streakTextView = findViewById(R.id.streakTextView)
        staffView = findViewById(R.id.staffView)

        buttonOption1 = findViewById(R.id.buttonOption1)
        buttonOption2 = findViewById(R.id.buttonOption2)
        buttonOption3 = findViewById(R.id.buttonOption3)
        buttonOption4 = findViewById(R.id.buttonOption4)

        listOf(buttonOption1, buttonOption2, buttonOption3, buttonOption4).forEach { it ->
            it.setOnClickListener { _ -> checkAnswer(it.text.toString()) }
        }
        savedInstanceState?.let {
            restoreState(it)
        } ?: populateQuestion()
    }

    override fun populateQuestion() {
        val (c, randomNote) = notesUtil.random(currentNote)
        staffView.displayNote(randomNote)
        Log.i(TAG, randomNote.name)
        setButtonLabels(randomNote)
        currentNote = c
    }

    private fun setButtonLabels(correctNote: Note) {
        val randomNotes =
            notesUtil.notes.filter { it != correctNote }.shuffled().take(3) + correctNote
        val shuffledOptions = randomNotes.shuffled()

        buttonOption1.text = shuffledOptions[0].name
        buttonOption2.text = shuffledOptions[1].name
        buttonOption3.text = shuffledOptions[2].name
        buttonOption4.text = shuffledOptions[3].name
        showHintIfNeeded(correctNote)
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString("option1", buttonOption1.text.toString())
        bundle.putString("option2", buttonOption2.text.toString())
        bundle.putString("option3", buttonOption3.text.toString())
        bundle.putString("option4", buttonOption4.text.toString())
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        restoreState(bundle)
    }

    override fun restoreState(bundle: Bundle) {
        super.restoreState(bundle)
        buttonOption1.text = bundle.getString("option1")
        buttonOption2.text = bundle.getString("option2")
        buttonOption3.text = bundle.getString("option3")
        buttonOption4.text = bundle.getString("option4")
    }

    override fun onResume() {
        super.onResume()
        showHintIfNeeded(notesUtil.notes[currentNote])
    }

    private fun showHintIfNeeded(correctNote: Note) {
        listOf(buttonOption1, buttonOption2, buttonOption3, buttonOption4).forEach {
            if (it.text == correctNote.name && showHints())
                it.setTextColor(Color.parseColor("#AAFF00"))
            else
                it.setTextColor(Color.parseColor("#FFFFFF"))
        }

    }

    override fun checkUserGuess(userGuess: String) = userGuess == notesUtil.notes[currentNote].name
}
