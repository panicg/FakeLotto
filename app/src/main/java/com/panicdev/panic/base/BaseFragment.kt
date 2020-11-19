package com.panicdev.panic.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.panicdev.panic.common.alert
import com.panicdev.panic.common.AndroidUtilities

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment() {


    lateinit var viewDataBinding: T
    abstract val layoutResourceId: Int

    abstract val baseViewModel: R
    lateinit var progressView: ProgressView

    protected var isOnCreated = false

    open fun onBackPressed(): Boolean = false

    abstract fun initStartView()
    abstract fun initDataBinding()

    abstract fun initAfterBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        initStartView()
        initDataBinding()
        initProgressObserve()
        initAfterBinding()
        initAlert()
        AndroidUtilities.runOnUIThread({
            isOnCreated = true
        }, 1000)
    }

    private fun initProgressObserve() {
        progressView = ProgressView(context!!)
        baseViewModel.progress.observe(this, Observer { flag ->

            when (flag) {
                1 -> {
                    showProgress(Color.WHITE)
                }
                2 -> {
                    showProgress(Color.TRANSPARENT)
                }
                3 -> {
                    hideProgress()
                }
            }
        })
    }

    private fun initAlert() {
        baseViewModel.alert.observe(this, Observer {
            it?.let { msg ->
                alert(message = msg)
            }
        })
    }

    private fun showProgress(backgroundColor: Int) {
        progressView.show(0F, viewDataBinding.root as ViewGroup)
    }

    private fun hideProgress() {
        progressView.hide()
    }
}