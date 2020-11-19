package com.panicdev.panic.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    /**
     * 1 : showProgress with Dim
     * 2 : showProgress without Dim
     * 3 : hideProgress
     */
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int>
        get() = _progress

    protected val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    protected val _alert = MutableLiveData<String?>()
    val alert: LiveData<String?>
        get() = _alert

    //토큰만료
    protected val _tokenExpire = MutableLiveData<Boolean>()
    val tokenExpire: LiveData<Boolean>
        get() = _tokenExpire

    //로그인이 필요한 서비스
    val _needSignIn = MutableLiveData<Boolean>()
    val needSignIn: LiveData<Boolean>
        get() = _needSignIn

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun showProgress(isDim: Boolean = true) {
        _progress.postValue(if (isDim) 1 else 2)
    }

    fun hideProgress() {
        _progress.postValue(3)
    }

    protected fun throwFailure(result: DataResult) {
        _error.postValue(result.errorMessage)
    }

    protected fun throwError(`throw`: Throwable) {
        `throw`.printStackTrace()
        _error.postValue(`throw`.message)
    }

    protected fun errorMessage(message: String) {
        Log.e("BaseViewModel Error", message)
        _error.postValue(message)
    }


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


}