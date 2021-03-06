package com.panicdev.panic.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.panicdev.fakelotto.databinding.ViewProgressBinding

class ProgressView : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initInternal()
    }

    lateinit var mBinding : ViewProgressBinding
    var parentView : ViewGroup? = null

    private val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    private fun initInternal(){
        mBinding = ViewProgressBinding.inflate(LayoutInflater.from(context), this, true)
        isClickable = false
    }

    fun show(backgroundAlpha : Float = 1.0f, parentView : ViewGroup){
        hide()
        this.parentView = parentView
        parentView.addView(this, params)
        this.bringToFront()
        mBinding.bg.alpha = backgroundAlpha
//        mBinding.progressBar.visibility = View.VISIBLE
    }
    fun hide(){
        parentView?.let {
            it.removeView(this)

            mBinding.root.animate().apply {
                alpha(0F)
                duration = 300
                this.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        parentView?.let {
                            it.removeView(this@ProgressView)
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
            }
//            mBinding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}