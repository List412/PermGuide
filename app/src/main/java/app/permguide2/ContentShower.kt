package app.permguide2

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * TODO: document your custom view class.
 */
class ContentShower : View {
    private var textLabel: String? = null

    var text: TextView? = null
    var image: ImageView? = null

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the textLabel.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    var exampleDrawable: Drawable? = null

    private var mTextPaint: TextPaint? = null
    private var mTextWidth: Float = 0.toFloat()
    private var mTextHeight: Float = 0.toFloat()

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the textLabel to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    var Text: String?
        get() = textLabel
        set(exampleString) {
            textLabel = exampleString
            invalidateTextPaintAndMeasurements()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
//        val a = context.obtainStyledAttributes(
//                attrs, R.styleable.contentShower, defStyle, 0)
//
//        mExampleString = a.getString(
//                R.styleable.contentShower_exampleString)
//        mExampleColor = a.getColor(
//                R.styleable.contentShower_exampleColor,
//                mExampleColor)
//        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
//        // values that should fall on pixel boundaries.
//        mExampleDimension = a.getDimension(
//                R.styleable.contentShower_exampleDimension,
//                mExampleDimension)
//
//        if (a.hasValue(R.styleable.contentShower_exampleDrawable)) {
//            exampleDrawable = a.getDrawable(
//                    R.styleable.contentShower_exampleDrawable)
//            exampleDrawable!!.callback = this
//        }
//
//        a.recycle()

        text = this.findViewById(R.id.text)
        image = this.findViewById(R.id.image)

        textLabel = "WOW"

        // Set up a default TextPaint object
        mTextPaint = TextPaint()
        mTextPaint!!.flags = Paint.ANTI_ALIAS_FLAG
        mTextPaint!!.textAlign = Paint.Align.LEFT

        // Update TextPaint and textLabel measurements from attributes
        invalidateTextPaintAndMeasurements()
    }



    private fun invalidateTextPaintAndMeasurements() {
        mTextPaint!!.textSize = 12f
        mTextPaint!!.color = 2
        mTextWidth = mTextPaint!!.measureText(textLabel)

        val fontMetrics = mTextPaint!!.fontMetrics
        mTextHeight = fontMetrics.bottom
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        // Draw the textLabel.
        canvas.drawText(textLabel!!,
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint!!)

        // Draw the example drawable on top of the textLabel.
        if (exampleDrawable != null) {
            exampleDrawable!!.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight)
            exampleDrawable!!.draw(canvas)
        }
    }
}
