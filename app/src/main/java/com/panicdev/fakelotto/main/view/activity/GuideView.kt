package com.panicdev.fakelotto.main.view.activity

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import com.panicdev.fakelotto.databinding.ViewGuideBinding

class GuideView : FrameLayout {

    constructor(
        context: Context
    ) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initInternal()
    }

    lateinit var mBinding: ViewGuideBinding
    var parentView: ViewGroup? = null


    private val params = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    private fun initInternal() {
        mBinding = ViewGuideBinding.inflate(LayoutInflater.from(context), this, true)
        isClickable = true
    }

    fun show(parentView: ViewGroup) {
        this.parentView = parentView
        parentView.addView(this, params)
        this.bringToFront()
        mBinding.run {
            layoutRoot.animate().apply {
                alpha(1F)
                duration = 200
                interpolator = AccelerateInterpolator()
            }
            tvClose.setOnClickListener {
                hide()
            }
        }
    }

    fun hide() {
        mBinding.layoutRoot.animate().apply {
            alpha(0F)
            duration = 100
            this.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    parentView?.let {
                        it.removeView(this@GuideView)
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }


}