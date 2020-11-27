package com.panicdev.fakelotto.main.view.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.panicdev.fakelotto.R
import com.panicdev.fakelotto.databinding.ActivityRealBinding
import com.panicdev.fakelotto.databinding.HolderBoardBinding
import com.panicdev.fakelotto.main.viewmodel.RealViewModel
import com.panicdev.panic.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class RealActivity : BaseActivity<ActivityRealBinding, RealViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_real
    override val baseViewModel: RealViewModel by viewModel()

    var isWin = false

    val scoreList = listOf<List<Int>>(
        listOf(1, 2, 3, 4, 5, 6),
        listOf(1, 2, 3, 4, 5, 6),
        listOf(1, 2, 3, 4, 5, 6),
        listOf(1, 2, 3, 4, 5, 6),
        listOf(1, 2, 3, 4, 5, 6)
    )

    lateinit var boardAdapter : BoardAdapter


    override fun initStartView() {
        viewDataBinding.run {


            tvNo.text = "제868회"
            tvDate.text = "2020-1-20 추첨"

            tvWin1.text = "1"
            tvWin2.text = "2"
            tvWin3.text = "3"
            tvWin4.text = "4"
            tvWin5.text = "5"
            tvWin6.text = "6"
            tvWinBonus.text = "7"

            tvMent1.text = "${if(isWin) "축하합니다!" else "아쉽게도,"}"
            if (isWin){
                llWin.visibility = View.VISIBLE
                llLose.visibility = View.INVISIBLE
                tvPrize.text = "2,249,466,563"
            } else {
                llWin.visibility = View.INVISIBLE
                llLose.visibility = View.VISIBLE
            }

            boardAdapter = BoardAdapter()
            rvBoard.adapter = boardAdapter
       }
    }

    override fun initAfterBinding() {
        baseViewModel.run {

            startParsing(intent.getStringExtra("url")!!)
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
            holder.init(scoreList[position])
        }

    }


    inner class BoardHolder(view : View) : RecyclerView.ViewHolder(view){
        var mBinding = HolderBoardBinding.bind(view)

        fun init(item : List<Int>){
            mBinding.run {
                tvLine.text = "A"
                tvResult.text = "낙첨"
                tvWin1.text = item[0].toString()
                tvWin2.text = item[1].toString()
                tvWin3.text = item[2].toString()
                tvWin4.text = item[3].toString()
                tvWin5.text = item[4].toString()
                tvWin6.text = item[5].toString()
            }
        }
    }
}