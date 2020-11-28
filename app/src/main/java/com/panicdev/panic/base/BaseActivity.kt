package com.panicdev.panic.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer

abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    abstract val layoutResourceId: Int

    abstract val baseViewModel: R

    abstract fun initStartView()
    abstract fun initAfterBinding()


    lateinit var progressView: ProgressView

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)



        initStartView()
        initProgressObserve()
        initAfterBinding()
    }

    override fun onBackPressed() {

        val manager = supportFragmentManager

        (manager.fragments.firstOrNull() as? BaseFragment<*, *>)?.let { fragment ->
            if (fragment.onBackPressed()) {
                return
            }
            super.onBackPressed()
        } ?: run {
            super.onBackPressed()
        }
    }

    private fun initProgressObserve() {
        progressView = ProgressView(this)
        baseViewModel.progress.observe(this, Observer { flag ->

            when (flag) {
                1 -> {
                    showProgress(false)
                }
                2 -> {
                    showProgress(true)
                }
                3 -> {
                    hideProgress()
                }
            }

        })
    }




    private fun showProgress(isTransparent : Boolean) {
        progressView.show(if (isTransparent) 0f else 1f, viewDataBinding.root as ViewGroup)
    }

    private fun hideProgress() {
        progressView.hide()
    }

}