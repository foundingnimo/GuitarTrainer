package io.nimo.music.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.AppCompatImageView

class FretBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {
    private val correctPaint = Paint().apply {
        color = Color.parseColor("#99AAFF00")
        style = Paint.Style.FILL
    }
    private val wrongPaint = Paint().apply {
        color = Color.parseColor("#99EE4B2B")
        style = Paint.Style.FILL
    }
    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var drawCircle = false
    private lateinit var currentColor: Paint
    var showHints = false
    private var hints = mutableListOf<Pair<Float, Float>>()

    fun tap(event: MotionEvent, correct: Boolean) {
        circleX = event.x
        circleY = event.y
        drawCircle = true
        currentColor = if (correct) correctPaint else wrongPaint
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (drawCircle && !showHints) {
            val handler = Handler(Looper.getMainLooper())
            canvas.drawCircle(circleX, circleY, 30f, currentColor)
            handler.postDelayed({
                drawCircle = false
                invalidate()
            },800)
        }
        if (hints.size > 0) {
            hints.forEach{
                placeHint(canvas, it)
            }
        }
    }

    private fun placeHint(canvas: Canvas, point: Pair<Float, Float>) {
        canvas.drawCircle(point.first, point.second, 30f, correctPaint )
    }

    fun addHint(p: Pair<Float, Float>) {
        hints.add(p)
    }

    fun clearHints() = hints.clear()
}