package com.panicdev.fakelotto.main.view.activity

import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.panicdev.fakelotto.R
import com.panicdev.fakelotto.databinding.ActivityRealBinding
import com.panicdev.fakelotto.main.viewmodel.RealViewModel
import com.panicdev.panic.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class RealActivity : BaseActivity<ActivityRealBinding, RealViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_real
    override val baseViewModel: RealViewModel by viewModel()


    override fun initStartView() {
        viewDataBinding.run {

            wv.setWebViewClient(WebViewClient()) // 클릭시 새창 안뜨게

            wv.webViewClient = WebClient()
            wv.settings.apply {
                javaScriptEnabled = true // 웹페이지 자바스클비트 허용 여부
                setSupportMultipleWindows(false) // 새창 띄우기 허용 여부
                javaScriptCanOpenWindowsAutomatically = false // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
                loadWithOverviewMode = true // 메타태그 허용 여부
                useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
                setSupportZoom(false) // 화면 줌 허용 여부
                builtInZoomControls = false // 화면 확대 축소 허용 여부
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 컨텐츠 사이즈 맞추기
                cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
                domStorageEnabled = true // 로컬저장소 허용 여부
            }

            wv.loadUrl(intent.getStringExtra("url") ?: "")
        }
    }

    override fun initAfterBinding() {
        baseViewModel.run {

        }
    }

    class WebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView,
            url: String
        ): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // Obvious next step is: document.forms[0].submit()
//            view.loadUrl("javascript:document.forms[0].q.value='[android]'")
            view.loadUrl("javascript:window.document.getElementsByClassName('clr')[0].style.display='none';")
        }
    }
}