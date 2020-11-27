package com.panicdev.fakelotto.main.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.panicdev.panic.base.BaseViewModel
import com.panicdev.panic.common.L

class FakeViewModel : BaseViewModel() {

    val _test = MutableLiveData<String>("test")
    val test: LiveData<String>
        get() = _test

    val test2 = "test"


    fun sonClick(v : View){
        L.d("asddasadsadsasdasd")
    }
}