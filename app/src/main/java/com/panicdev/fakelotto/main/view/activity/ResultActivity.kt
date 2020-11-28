package com.panicdev.fakelotto.main.view.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.panicdev.fakelotto.databinding.HolderBoardBinding
import com.panicdev.fakelotto.main.viewmodel.RealViewModel
import com.panicdev.panic.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.panicdev.fakelotto.R
import com.panicdev.fakelotto.databinding.ActivityResultBinding
import com.panicdev.panic.common.alert
import kotlin.collections.ArrayList
import kotlin.random.Random


class ResultActivity : BaseActivity<ActivityResultBinding, RealViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_result
    override val baseViewModel: RealViewModel by viewModel()

    var isWin = false

    lateinit var scoreList : ArrayList<MutableList<Int>>
    lateinit var statusList : List<String>
    lateinit var boardAdapter : BoardAdapter
    lateinit var winnerNum : List<Int>
    var winIndex = 0

    var isFake = false


    override fun initStartView() {
        viewDataBinding.run {

            isFake = intent.getBooleanExtra("isFake", false)

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
       }
    }

    override fun initAfterBinding() {
        baseViewModel.run {
            error.observe(this@ResultActivity, Observer {
                alert(message = it){
                    finish()
                }
            })

            myLottoData.observe(this@ResultActivity, Observer { result ->
                viewDataBinding.run {
                    statusList = result.myStatusList
                    tvNo.text = result.roundNum
                    tvDate.text = result.publicDate

                    if (isFake){
                        winIndex = result.roundNum.replace("제", "").replace("회", "").toInt() % result.myNum.count()
                        result.winnerNum = result.myNum[winIndex]
                        result.winnerNum[6] = Random.nextInt(0, 46)
                    }
                    winnerNum = result.winnerNum


                    result.prizeMoney?.let { prize ->
                        //당첨됬음
                        llWin.visibility = View.VISIBLE
                        tvMent1.text = if (isFake) "축하합니다!" else result.noticeTop
                        tvPrize.text = prize
                        result.noticeBottom?.let {
                            //지급기한이 지남
                            tvMent2.visibility = View.VISIBLE
                            tvMent2.text = it
                        } ?: run {
                            tvMent2.visibility = View.GONE
                        }
                    } ?: run {
                        //전부 낙첨
                        tvMent1.text = result.noticeTop
                        llLose.visibility = View.VISIBLE
                    }

                    val numViewList = listOf(tvWin1, tvWin2, tvWin3, tvWin4, tvWin5, tvWin6, tvWinBonus)

                    numViewList.forEachIndexed { index, textView ->
                        textView.text = "${result.winnerNum[index]}"
                        textView.backgroundTintList = when (result.winnerNum[index]){
                            in 1 .. 10 -> getColorStateList(R.color.clr_1)
                            in 11 .. 20 -> getColorStateList(R.color.clr_2)
                            in 21 .. 30 -> getColorStateList(R.color.clr_3)
                            in 31 .. 40 -> getColorStateList(R.color.clr_4)
                            in 41 .. 50 -> getColorStateList(R.color.clr_5)
                            else -> getColorStateList(R.color.clr_1)
                        }
                    }

                    scoreList = result.myNum

                    boardAdapter = BoardAdapter()
                    rvBoard.adapter = boardAdapter
                }
            })

            startParsing(intent.getStringExtra("url")!!, isFake)
        }
    }

    inner class BoardAdapter : RecyclerView.Adapter<BoardHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.holder_board, parent, false)
            return BoardHolder(view)
        }

        override fun getItemCount(): Int {
            return scoreList.count()
        }

        override fun onBindViewHolder(holder: BoardHolder, position: Int) {
            holder.init(scoreList[position], position)
        }

    }


    inner class BoardHolder(view : View) : RecyclerView.ViewHolder(view){
        var mBinding = HolderBoardBinding.bind(view)

        fun init(item : List<Int>, position : Int){
            mBinding.run {
                tvLine.text = when(position){
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    3 -> "D"
                    else -> "E"
                }

                tvResult.text = if (isFake && position == winIndex) "1등당첨" else statusList[position]
                val numViewList = listOf(tvWin1, tvWin2, tvWin3, tvWin4, tvWin5, tvWin6)

                numViewList.forEachIndexed { index, textView ->
                    textView.run {
                        text = item[index].toString()
                        if (winnerNum.contains(item[index])){
                            textView.backgroundTintList =when (item[index]){
                                in 1 .. 10 -> getColorStateList(R.color.clr_1)
                                in 11 .. 20 -> getColorStateList(R.color.clr_2)
                                in 21 .. 30 -> getColorStateList(R.color.clr_3)
                                in 31 .. 40 -> getColorStateList(R.color.clr_4)
                                in 41 .. 50 -> getColorStateList(R.color.clr_5)
                                else -> getColorStateList(R.color.clr_1)
                            }
                        } else {
                            textView.setTextColor(getColor(R.color.black_text2))
                            textView.backgroundTintList = getColorStateList(R.color.clr_0)
                        }
                    }
                }
            }
        }
    }
}