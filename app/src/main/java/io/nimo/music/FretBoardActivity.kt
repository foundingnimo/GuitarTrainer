package io.nimo.music

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.TextView
import io.nimo.music.view.FretBoardView

class FretBoardActivity : AbstractGuitarActivity() {

    private val FRET_NUM = 12
    private val STRING_TAP_THRESHOLD = 15

    private val stringLocations = listOf(42, 109, 184, 254, 324, 396)
    private var fretBoardWidth = 0
    private var singleFretWidth = 0

    private lateinit var fretBoard: FretBoardView
    private lateinit var displayNoteText: TextView

    private var guessString = -1
    private var guessFret = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fretboard)
        statsTextView = findViewById(R.id.statsTextView)
        streakTextView = findViewById(R.id.streakTextView)

        fretBoard = findViewById(R.id.fretboardImageView)
        fretBoard.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    guessString = -1
                    guessFret = -1
                    val x = event.x
                    val y = event.y
                    val tappedStringIndex = stringLocations.indexOfFirst { l ->
                        y >= l - STRING_TAP_THRESHOLD && y <= l + STRING_TAP_THRESHOLD
                    }
                    if (tappedStringIndex != -1) {
                        guessString = tappedStringIndex + 1
                    }

                    guessFret = if (x > 0.04 * fretBoardWidth) {
                        val shr_x = x - 0.04 * fretBoardWidth
                        (shr_x / singleFretWidth).toInt() + 1
                    } else {
                        0
                    }
                    fretBoard.tap(event, checkAnswer(""))
                }
            }
            true
        }

        fretBoard.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                fretBoard.viewTreeObserver.removeOnGlobalLayoutListener(this)
                fretBoardWidth = fretBoard.width
                val relevantWidth = 0.96 * fretBoardWidth
                singleFretWidth = (relevantWidth / FRET_NUM).toInt()
                populateQuestion()
            }
        })

    }

    override fun populateQuestion() {
        displayNoteText = findViewById(R.id.displayNoteText)
        val (c, randomNote) = notesUtil.random(currentNote)
        displayNoteText.text = randomNote.name
        currentNote = c
        if (showHints()) {
            fretBoard.apply {
                clearHints()
                randomNote.fretPositions.forEach {
                    addHint(
                        Pair(
                            calcFretXPosition(it.second),
                            stringLocations[it.first - 1].toFloat(),
                        )
                    )
                }
                invalidate()
            }
        }
    }

    private fun calcFretXPosition(p: Int) = if (p == 0) (0.02 * fretBoardWidth).toFloat()
    else (singleFretWidth * (p-1) + singleFretWidth / 2 + 0.04 * fretBoardWidth).toFloat()

    override fun onResume() {
        super.onResume()
        fretBoard.apply {
            showHints = showHints()
            if (!showHints) clearHints()
            invalidate()
        }
    }

    override fun checkUserGuess(userGuess: String) =
        guessString > 0 &&
                guessFret >= 0 &&
                notesUtil.notes[currentNote].fretPositions.find { p ->
                    p.first == guessString && p.second == guessFret
                } != null

}
