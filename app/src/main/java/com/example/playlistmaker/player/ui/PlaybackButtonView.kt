package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.toRect
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    var state: State = State.Paused
        set(value) {
            field = value
            invalidate()
        }

    private var rect = RectF()
    private val playingDrawable: Drawable?
    private val pausedDrawable: Drawable?

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                playingDrawable =
                    getDrawable(R.styleable.PlaybackButtonView_playingStateResId)
                pausedDrawable =
                    getDrawable(R.styleable.PlaybackButtonView_pausedStateResId)
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
        getCurrentDrawable(state)?.let {
            it.bounds = rect.toRect()
            it.draw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                toggleState()
                callOnClick()
                return false
            }

            else -> return super.onTouchEvent(event)
        }
    }

    fun prepare() {
        state = State.Paused
    }

    private fun toggleState() {
        state = when (state) {
            State.Playing -> State.Paused
            State.Paused -> State.Playing
        }
    }

    private fun getCurrentDrawable(state: State) = when (state) {
        State.Playing -> playingDrawable
        State.Paused -> pausedDrawable
    }

    companion object {
        enum class State {
            Playing, Paused
        }
    }
}
