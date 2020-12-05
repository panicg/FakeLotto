package com.panicdev.fakelotto.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.panicdev.panic.base.BaseViewModel
import com.panicdev.panic.common.convertCurrency
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import java.lang.Exception
import kotlin.random.Random


class RealViewModel : BaseViewModel() {

    private val winnerNum = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0)
    private val myNumList = ArrayList<MutableList<Int>>()
    private lateinit var myStatusList: List<String>


    private lateinit var tmpLottoData: myLottoData

    private val _myLottoData = MutableLiveData<myLottoData>()
    val myLottoData: LiveData<myLottoData>
        get() = _myLottoData

    private val _isNotYet = MutableLiveData<Boolean>()
    val isNotYet: LiveData<Boolean>
        get() = _isNotYet


    fun startParsing(url: String, isFake: Boolean) {
        showProgress(true)
//        val test = "http://m.dhlottery.co.kr/?v=0999q082122233242q040523253233q082530344045q030506132042q0105080929441084493665"
        val params = url.split("v=")[1]
        val realUrl = "https://m.dhlottery.co.kr/qr.do?method=winQr&v=$params"

        var parsingWeb = Observable.fromCallable {
            Jsoup.connect(realUrl).userAgent("Android").get()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->

                val resultString = result.select(".bx_notice strong").toString().removeTag()
                if (resultString.contains("미추첨")){
                  //미추
                    _isNotYet.value = true
                    return@subscribe
                }


                //회차
                val roundNum = result.select(".key_clr1").first().toString().removeTag()
                //발표일
                val publicDate = result.select(".date").first().toString().removeTag()
                //당첨금
                var prizeMoney: String? = null
                if (result.select(".key_clr1").count() > 1) {
                    prizeMoney = result.select(".key_clr1")[1].toString().removeTag()
                }

                val noticeTop =
                    result.select(".bx_notice div:eq(0) span").first().toString().removeTag()

                var noticeBottom: String? = null
                if (result.select(".bx_notice div").count() > 1) {
                    noticeBottom =
                        result.select(".bx_notice div:eq(1) strong").toString().removeTag()
                }

                for (i in 0 until 7) {
                    winnerNum[i] =
                        result.select(".list div:eq($i) span").toString().removeTag().toInt()
                }


                result.select(".tbl_basic table tbody tr").forEachIndexed { index, element ->
                    val myNum = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0)
                    result.select(".tbl_basic table tbody tr:eq($index) td span")
                        .forEachIndexed { index, element ->
                            myNum[index] = element.toString().removeTag().toInt()
                        }
                    myNumList.add(myNum)
                }

                myStatusList = result.select(".result").map {
                    it.toString().removeTag()
                }


                if (isFake) {
                    tmpLottoData = myLottoData(
                        roundNum = roundNum,
                        publicDate = publicDate,
                        prizeMoney = prizeMoney,
                        winnerNum = winnerNum,
                        myNum = myNumList,
                        myStatusList = myStatusList,
                        noticeTop = noticeTop,
                        noticeBottom = noticeBottom
                    )

                    get1stPrizeMoney(params.split("m").first())
                } else {
                    hideProgress()
                    _myLottoData.value = myLottoData(
                        roundNum = roundNum,
                        publicDate = publicDate,
                        prizeMoney = prizeMoney,
                        winnerNum = winnerNum,
                        myNum = myNumList,
                        myStatusList = myStatusList,
                        noticeTop = noticeTop,
                        noticeBottom = noticeBottom
                    )
                }

            }
    }

    private fun get1stPrizeMoney(round: String) {
        val url =
            "https://m.dhlottery.co.kr/gameResult.do?method=byWin&drwNo=$round&wiselog=M_A_1_1"
        var parsingWeb = Observable.fromCallable {
            Jsoup.connect(url).userAgent("Android").get()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                try{
                    val prizeMoney = result.select(".highlight strong").first().toString().removeTag()


                    tmpLottoData.prizeMoney = prizeMoney

                    hideProgress()
                    _myLottoData.value = tmpLottoData
                } catch (e : Exception){
                    val prizeMoney = "${Random.nextLong(3000000000, 4000000000).convertCurrency} 원"


                    tmpLottoData.prizeMoney = prizeMoney

                    hideProgress()
                    _myLottoData.value = tmpLottoData
                }

            }
    }


    private fun String.removeTag(): String {
        return this.split(">")[1].split("<").first()
    }

}


data class myLottoData(
    val roundNum: String,
    val publicDate: String,
    var prizeMoney: String?,
    var winnerNum: MutableList<Int>,
    val myNum: ArrayList<MutableList<Int>>,
    val myStatusList: List<String>,
    val noticeTop: String,
    val noticeBottom: String?
)