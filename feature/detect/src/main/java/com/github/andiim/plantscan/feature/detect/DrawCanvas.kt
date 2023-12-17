package com.github.andiim.plantscan.feature.detect

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import java.util.LinkedList

class DrawCanvas(outputBitmap: Bitmap) : Canvas(outputBitmap) {
    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 0
        private const val TEXT_BACKGROUND_SIZE = 50f
        private const val TEXT_SIZE = 50f
        private const val STROKE_WIDTH = 8F
        private const val TEXT_HEIGHT_OFFSET = 0.9f
    }

    private var results: List<BoxWithText> = LinkedList<BoxWithText>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()
    private var scaleFactor: Float = 1f
    private var bounds = Rect()

    init {
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.RED
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = TEXT_BACKGROUND_SIZE

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = TEXT_SIZE

        boxPaint.color = Color.RED
        boxPaint.strokeWidth = STROKE_WIDTH
        boxPaint.style = Paint.Style.STROKE
    }

    private fun draw() {
        for (result in results) {
            val boundingBox = result.box
            val top = boundingBox.top * scaleFactor
            val bottom = boundingBox.bottom * scaleFactor
            val left = boundingBox.left * scaleFactor
            val right = boundingBox.right * scaleFactor

            // Draw bounding box around detected objects
            val rect = RectF(left, top, right, bottom)
            drawRect(rect, boxPaint)

            val text = result.text
            textBackgroundPaint.getTextBounds(text, 0, text.length, bounds)
            // val textWidth = bounds.width()
            val textHeight = bounds.height()

            var margin = (rect.width() - bounds.width()) / 2.0f
            if (margin < 0f) margin = 0f
            drawRect(
                left + margin,
                top,
                right + BOUNDING_RECT_TEXT_PADDING,
                top + textHeight + BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint,
            )

            val fontSize = textPaint.textSize * rect.width() / bounds.width()
            if (fontSize < textPaint.textSize) textPaint.textSize = fontSize

            // Draw text for detected object
            drawText(text, left + margin, (top + textHeight).times(TEXT_HEIGHT_OFFSET), textPaint)
        }
    }

    fun setResults(
        detectionResults: List<BoxWithText>,
        // imageHeight: Float,
        // imageWidth: Float,
    ) {
        results = detectionResults
        // scaleFactor = max(width * 1f / imageWidth, height * 1f / imageHeight)
        // Timber.tag("SCALE_FACTOR").d("setResults: $scaleFactor")
        draw()
    }
}
