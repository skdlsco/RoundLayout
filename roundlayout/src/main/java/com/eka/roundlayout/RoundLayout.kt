package com.eka.roundlayout

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class RoundLayout : FrameLayout {
    private val outerPaint = Paint().apply {
        isAntiAlias = true
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val strokePaint = Paint().apply {
        isAntiAlias = true
        color = Color.TRANSPARENT
    }

    var mTopLeftRadius = 0f
    var mTopRightRadius = 0f
    var mBottomLeftRadius = 0f
    var mBottomRightRadius = 0f
    var outerColor = 0
        set(value) {
            outerPaint.color = value
            field = value
        }

    var strokeWidth = 0f
    var strokeColor = 0
        set(value) {
            strokePaint.color = value
            field = value
        }

//    var isInnerRadius = false

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        getAttrs(attrs!!)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        getAttrs(attrs!!, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet, defStyleAttr: Int) {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout, defStyleAttr, 0)
        setTypeArray(typeArray)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val radius = typedArray.getDimension(R.styleable.RoundLayout_radius, 0f)
        mTopLeftRadius = radius
        mTopRightRadius = radius
        mBottomLeftRadius = radius
        mBottomRightRadius = radius

        mTopRightRadius = typedArray.getDimension(R.styleable.RoundLayout_topRightRadius, radius)
        mTopLeftRadius = typedArray.getDimension(R.styleable.RoundLayout_topLeftRadius, radius)
        mBottomRightRadius = typedArray.getDimension(R.styleable.RoundLayout_bottomRightRadius, radius)
        mBottomLeftRadius = typedArray.getDimension(R.styleable.RoundLayout_bottomLeftRadius, radius)
        outerColor = typedArray.getColor(R.styleable.RoundLayout_outerColor, Color.TRANSPARENT)

        strokeWidth = typedArray.getDimension(R.styleable.RoundLayout_strokeWidth, 0f)
        strokeColor = typedArray.getColor(R.styleable.RoundLayout_strokeColor, Color.TRANSPARENT)
        typedArray.recycle()
    }

    fun setRadius(topLeftRadius: Float, topRightRadius: Float, bottomRightRadius: Float, bottomLeftRadius: Float) {
        mTopLeftRadius = topLeftRadius
        mTopRightRadius = topRightRadius
        mBottomRightRadius = bottomRightRadius
        mBottomLeftRadius = bottomLeftRadius
        invalidate()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
        super.dispatchDraw(canvas)
        mBottomRightRadius = min(this@RoundLayout.mBottomRightRadius, height.toFloat(), width.toFloat())
        mBottomLeftRadius = min(this@RoundLayout.mBottomLeftRadius, height.toFloat(), width.toFloat())
        mTopRightRadius = min(this@RoundLayout.mTopRightRadius, height.toFloat(), width.toFloat())
        mTopLeftRadius = min(this@RoundLayout.mTopLeftRadius, height.toFloat(), width.toFloat())
        drawStroke(canvas)
        drawOuters(canvas)
    }

    private fun drawOuters(canvas: Canvas?) {

        val bottomRightRect =
            RectF(width - mBottomRightRadius, height - mBottomRightRadius, width.toFloat(), height.toFloat())
        val bottomLeftRect = RectF(0f, height - mBottomLeftRadius, mBottomLeftRadius, height.toFloat())
        val topLeftRect = RectF(0f, 0f, mTopLeftRadius, mTopLeftRadius)
        val topRightRect = RectF(width - mTopRightRadius, 0f, width.toFloat(), mTopRightRadius)
        val path = Path().apply {
            // TopLeft
            if (mTopLeftRadius != 0f) {
                addArc(topLeftRect, 180f, 90f)
                lineTo(0f, 0f)
                lineTo(0f, mTopLeftRadius)
            }
            // TopRight
            if (mTopRightRadius != 0f) {
                addArc(topRightRect, 270f, 90f)
                lineTo(width.toFloat(), 0f)
                lineTo(width - mTopRightRadius, 0f)
            }
            // BottomLeft
            if (mBottomLeftRadius != 0f) {
                addArc(bottomLeftRect, 90f, 90f)
                lineTo(0f, height.toFloat())
                lineTo(mBottomLeftRadius, height.toFloat())
            }
            // BottomRight
            if (mBottomRightRadius != 0f) {
                addArc(bottomRightRect, 0f, 90f)
                lineTo(width.toFloat(), height.toFloat())
                lineTo(width.toFloat(), height - mBottomRightRadius)
            }
        }
        outerPaint.xfermode =
                PorterDuffXfermode(if (outerPaint.color != Color.TRANSPARENT) PorterDuff.Mode.OVERLAY else PorterDuff.Mode.CLEAR)
        canvas?.drawPath(path, outerPaint)
    }

    private fun drawStroke(canvas: Canvas?) {

        val mInnerBottomRightRadius =
            min(this@RoundLayout.mBottomRightRadius, height - strokeWidth * 2, width - strokeWidth * 2)
        val mInnerBottomLeftRadius =
            min(this@RoundLayout.mBottomLeftRadius, height - strokeWidth * 2, width - strokeWidth * 2)
        val mInnerTopRightRadius =
            min(this@RoundLayout.mTopRightRadius, height - strokeWidth * 2, width - strokeWidth * 2)
        val mInnerTopLeftRadius =
            min(this@RoundLayout.mTopLeftRadius, height - strokeWidth * 2, width - strokeWidth * 2)

        val bottomRightOuterRect =
            RectF(width - mBottomRightRadius, height - mBottomRightRadius, width.toFloat(), height.toFloat())
        val bottomLeftOuterRect = RectF(0f, height - mBottomLeftRadius, mBottomLeftRadius, height.toFloat())
        val topLeftOuterRect = RectF(0f, 0f, mTopLeftRadius, mTopLeftRadius)
        val topRightOuterRect = RectF(width - mTopRightRadius, 0f, width.toFloat(), mTopRightRadius)

        val bottomRightInnerRect =
            RectF(
                width - mInnerBottomRightRadius - strokeWidth,
                height - mInnerBottomRightRadius - strokeWidth,
                width - strokeWidth,
                height - strokeWidth
            )
        val bottomLeftInnerRect =
            RectF(
                strokeWidth,
                height - mInnerBottomLeftRadius - strokeWidth,
                mInnerBottomLeftRadius + strokeWidth,
                height - strokeWidth
            )
        val topLeftInnerRect =
            RectF(strokeWidth, strokeWidth, strokeWidth + mInnerTopLeftRadius, strokeWidth + mInnerTopLeftRadius)
        val topRightInnerRect =
            RectF(
                width - mInnerTopRightRadius - strokeWidth,
                strokeWidth,
                width - strokeWidth,
                mInnerTopRightRadius + strokeWidth
            )

        canvas?.run {

            // TopLeft
            drawPath(Path().apply {
                addArc(topLeftOuterRect, 180f, 90f)
                lineTo(width / 2f, 0f)
                lineTo(width / 2f, strokeWidth)
                arcTo(topLeftInnerRect, 270f, -90f, false)
                lineTo(strokeWidth, height / 2f)
                lineTo(0f, height / 2f)
                lineTo(0f, strokeWidth)
            }, strokePaint)

            // TopRight
            drawPath(Path().apply {
                if (mTopRightRadius == 0f) {
                    lineTo(width.toFloat(), 0f)
                } else
                    addArc(topRightOuterRect, 270f, 90f)
                lineTo(width.toFloat(), height / 2f)
                lineTo(width - strokeWidth, height / 2f)
                arcTo(topRightInnerRect, 360f, -90f, false)
                lineTo(width / 2f, strokeWidth)
                lineTo(width / 2f, 0f)
                lineTo(width - mTopRightRadius, 0f)
            }, strokePaint)

            // BottomLeft
            drawPath(Path().apply {
                addArc(bottomLeftOuterRect, 90f, 90f)
                lineTo(0f, height / 2f)
                lineTo(strokeWidth, height / 2f)
                arcTo(bottomLeftInnerRect, 180f, -90f, false)
                lineTo(width / 2f, height - strokeWidth)
                lineTo(width / 2f, height.toFloat())
                lineTo(mBottomLeftRadius, height.toFloat())
            }, strokePaint)

            // BottomRight
            drawPath(Path().apply {
                if (mBottomLeftRadius == 0f) {
                    lineTo(width.toFloat(), height.toFloat())
                } else
                    addArc(bottomRightOuterRect, 0f, 90f)
                lineTo(width / 2f, height.toFloat())
                lineTo(width / 2f, height - strokeWidth)
                arcTo(bottomRightInnerRect, 90f, -90f, false)
                lineTo(width - strokeWidth, height / 2f)
                lineTo(width.toFloat(), height / 2f)
                lineTo(width.toFloat(), height - mBottomRightRadius)
            }, strokePaint)
        }
        val path = Path().apply {


        }
        canvas?.drawPath(path, strokePaint)
    }

    override fun performClick(): Boolean = super.performClick()


    private fun min(n1: Float, n2: Float, n3: Float): Float = Math.min(Math.min(n1, n2), n3)
}