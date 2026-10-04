package com.example.playlistmaker.player.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.example.playlistmaker.R
import com.example.playlistmaker.player.presentation.PlayerState

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    var state: PlayerState = PlayerState.Default
        set(value) {
            field = value
            invalidate()
        }

    private var rect = RectF()
    private val playingImageBitmap: Bitmap?
    private val pausedImageBitmap: Bitmap?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playingImageBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_playingStateResId)?.toBitmap()
                pausedImageBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_pausedStateResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        getCurrentBitmap(state)?.let {
            canvas.drawBitmap(it, null, rect, null)
        }
    }

    private fun getCurrentBitmap(state: PlayerState) = when (state) {
        is PlayerState.Playing -> playingImageBitmap
        else -> pausedImageBitmap
    }
}
