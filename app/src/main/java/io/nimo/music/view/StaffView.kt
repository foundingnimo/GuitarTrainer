package io.nimo.music.view

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.graphics.Color
import android.graphics.Paint
import io.nimo.music.Note
import io.nimo.music.R


class StaffView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val BOTTOM_LINE = 16
    private val TOP_LINE = 8
    private val SPACING = 40f
    private val STEM_FLIP_POSITION = 12

    private var note: Note? = null
    private val clefBitmap = BitmapFactory.decodeResource(resources, R.drawable.clef)

    private val notePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 5f
    }

    fun displayNote(theNote: Note) {
        note = theNote
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTrebleClef(canvas)
        drawStaffLines(canvas)
        note?.let {
            drawNote(canvas, it)
        }
    }

    private fun drawTrebleClef(canvas: Canvas) {
        canvas.drawBitmap(clefBitmap, 5f, 121f, null)
    }

    private fun drawStaffLines(canvas: Canvas) {
        for (i in 4..8)
            canvas.drawLine(0f, i * SPACING, width.toFloat(), i * SPACING, linePaint)
    }

    private fun getNoteVerticalPosition(notePosition: Int): Float {
        return notePosition * SPACING / 2
    }

    private fun drawNote(canvas: Canvas, note: Note) {

        val verticalPosition = getNoteVerticalPosition(note.position)

        val noteWidth = 55f
        val noteHeight = 40f

        val left = (width / 2) - (noteWidth / 2)
        val top = verticalPosition - (noteHeight / 2)
        val right = (width / 2) + (noteWidth / 2)
        val bottom = verticalPosition + (noteHeight / 2)

        // draw ledger lines if needed
        if (note.position > BOTTOM_LINE) {
            val bottomLedgers = (note.position - BOTTOM_LINE) / 2
            if (bottomLedgers > 0) {
                for (i in 9 until 9 + bottomLedgers)
                    canvas.drawLine(left - 10, i * SPACING, right + 10, i * SPACING, linePaint)
            }
        }
        if (note.position < TOP_LINE) {
            val topLedgers = (TOP_LINE - note.position) / 2
            if (topLedgers > 0) {
                for (i in 3 downTo 4 - topLedgers)

                    canvas.drawLine(
                        left - 10,
                        i * SPACING,
                        right + 10,
                        i * SPACING,
                        linePaint
                    )
            }
        }

        canvas.drawOval(left, top, right, bottom, notePaint)
        drawStem(note, left, right, verticalPosition, canvas)
    }

    private fun drawStem(
        note: Note,
        left: Float,
        right: Float,
        verticalPosition: Float,
        canvas: Canvas
    ) {
        val stemHeight = 100f
        val stemX = if (note.position < STEM_FLIP_POSITION) left else right
        val stemStartY =
            if (note.position < STEM_FLIP_POSITION) verticalPosition else verticalPosition - stemHeight
        val stemEndY =
            if (note.position < STEM_FLIP_POSITION) verticalPosition + stemHeight else verticalPosition
        canvas.drawLine(
            stemX, stemStartY,
            stemX, stemEndY, linePaint
        )
    }

}
