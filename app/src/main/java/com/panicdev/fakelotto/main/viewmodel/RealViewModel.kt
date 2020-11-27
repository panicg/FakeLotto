package com.panicdev.fakelotto.main.viewmodel

import com.panicdev.panic.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup


class RealViewModel : BaseViewModel() {



    fun startParsing(url : String){


        var backgroundtask = Observable.fromCallable {
            val doc = Jsoup.connect(url).userAgent("Android").get()
//            val doc = Jsoup.connect("https://m.dhlottery.co.kr/qr.do?method=winQr&v=0868m041120213645m010306132438m142933354042m021823262731m1217284143441293818248").get()

            doc
            false
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
//                replaceFragment(title)
//                progressBar.setVisibility(View.GONE)
//                backgroundtask.dispose()
            }
    }
}