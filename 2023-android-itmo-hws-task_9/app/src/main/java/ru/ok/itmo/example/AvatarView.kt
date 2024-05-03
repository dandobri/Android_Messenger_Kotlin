package ru.ok.itmo.example

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import kotlin.math.min
import kotlin.random.Random

@SuppressLint("Recycle")
class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var backgroundColor: Int
    private var textColor: Int
    private var text: Triple<String, Int, Int> = Triple("", 0, 0)
    private var bitmap: Bitmap? = null

    private val form = RectF()

    private var paint  = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize =  5f }

    init {
        val type :TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarView, defStyleAttr, defStyleRes)

        try {
            backgroundColor = type.getColor(R.styleable.AvatarView_backgroundColor,
                Color.rgb(Random.nextInt(0, 255),
                    Random.nextInt(0, 255), Random.nextInt(0, 255)))
            textColor = type.getColor(R.styleable.AvatarView_textColor, resources.getColor(R.color.white, context.theme))
        } finally {
            type.recycle()
        }
    }
    fun setBackgColor(@ColorRes colorId: Int) {
        backgroundColor = resources.getColor(colorId, context.theme)
        invalidate()
    }
    fun setTextColor(@ColorRes colorId: Int) {
        textColor = resources.getColor(colorId, context.theme)
        invalidate()
    }
    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }
    fun setText(text: String) {
        if (text.isNotBlank()) {
            val reg = text.split("\\s+".toRegex()).take(2).joinToString("") { it[0].uppercase() }
            val sum = reg.sumOf { it.code }
            fun accommodation(range: Int) = 255/2 - range/2 + sum % range
            backgroundColor = Color.rgb(accommodation(300), accommodation(200), accommodation(250))
            val form = Rect()
            paint.getTextBounds(reg, 0, reg.length, form)
            this.text = Triple(reg, form.width(), form.height())
        }
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        val radius = min(width, height) / 2f
        paint.apply { color = this@AvatarView.backgroundColor }
        form.set(0f, 0f, radius * 2f, radius * 2f)
        if (bitmap != null) {
            bitmap!!.width = (radius * 2).toInt()
            bitmap!!.height = (radius * 2).toInt()
            return
        }
        canvas.drawRoundRect(form, radius, radius, paint)
        paint.apply { color = this@AvatarView.textColor }
        canvas.drawText(text.first, radius - text.second / 2, radius + text.third / 2, paint)
        canvas.restoreToCount(save)
    }
}