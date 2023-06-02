package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnRepeat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var widthSize = 0
    private var heightSize = 0
    private var valueAnimator = ValueAnimator()
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new -> }
    ///////////
    private var loadText:String?="2"
    private var downloadText:String?="1"
    private var loadingAnimation = 0f
    private var stopAnimation = false
    private var circleColor = 0


    ////////////////////////
    init {
        isClickable=true
        context.withStyledAttributes(attrs,R.styleable.LoadingButton)
        {
            loadText= getString(R.styleable.LoadingButton_LoadText)
            downloadText=getString(R.styleable.LoadingButton_downloadText)
            buttonState=ButtonState.Completed
            circleColor=Color.RED
        }

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBackground(canvas)
        drawLoadingBackground(canvas)
        drawLoadingCircle(canvas!!)
        drawText(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        val background=Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style=Paint.Style.FILL
            color=context.getColor(R.color.colorPrimaryDark)
        }
        canvas?.drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),background)
    }
    private fun drawLoadingBackground(canvas: Canvas?) {
        val loadbackground=Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style=Paint.Style.FILL
            color=context.getColor(R.color.colorPrimary)
        }
        canvas?.drawRect(0f,0f,widthSize.toFloat()*loadingAnimation,heightSize.toFloat(),loadbackground)
    }




    private fun animateButton() {

        valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 2000
        valueAnimator.addUpdateListener {
            loadingAnimation = it.animatedValue as Float
            if (loadingAnimation == 1f) {
                loadingAnimation = 0f
            }
            invalidate()
        }
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.start()
        valueAnimator.doOnRepeat {
            if (stopAnimation) {
                valueAnimator.cancel()
                buttonState = ButtonState.Completed
                invalidate()
                loadingAnimation = 0f
                stopAnimation = false
            }
        }
        valueAnimator.doOnEnd {
            isClickable = true
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        isClickable = false
        if (buttonState == ButtonState.Completed) buttonState = ButtonState.Loading
        animateButton()

        return true
    }
/////////////
private fun drawLoadingCircle(canvas: Canvas) {

    var circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = circleColor
    }

    val circleMarginFactor = 0.2f
    var circleLeft =
        widthSize.toFloat() - heightSize.toFloat() + heightSize.toFloat() * circleMarginFactor
    var circleTop = heightSize.toFloat() * circleMarginFactor
    var circleRight = widthSize.toFloat() - heightSize.toFloat() * circleMarginFactor
    var circleBottom = heightSize.toFloat() - heightSize.toFloat() * circleMarginFactor
    canvas.drawArc(circleLeft, circleTop, circleRight, circleBottom, 0F, 360F * loadingAnimation, true, circlePaint)
}
    fun animateflag()
    {
        stopAnimation=true
    }
private fun drawText(canvas: Canvas?)

{

    var textContent = if (buttonState == ButtonState.Loading) loadText
    else downloadText
    Log.i("xd","loadText $loadText")
    Log.i("xd","downloadText $downloadText")
    Log.i("xd","buttonState $buttonState")
//textContent="helo"
    val text=Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign=Paint.Align.CENTER
        textSize=35.0f
        color=Color.WHITE
    }
    val height = heightSize.toFloat() / 2 + ((text.descent() - text.ascent()) / 2) - text.descent()

    canvas?.drawText(textContent!!,(widthSize/2).toFloat(),height,text)
}
///////////////////


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) { // set the size depending on constraints
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}