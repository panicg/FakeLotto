package com.panicdev.fakelotto.main.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.zxing.BarcodeFormat
import com.panicdev.fakelotto.R
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.*
import com.panicdev.fakelotto.databinding.ActivityScanBinding
import com.panicdev.fakelotto.main.viewmodel.ScanViewModel
import com.panicdev.panic.base.BaseActivity
import com.panicdev.panic.common.L
import com.panicdev.panic.common.PreferenceController
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Field
class ScanActivity : BaseActivity<ActivityScanBinding, ScanViewModel>(), BarcodeCallback {
    override val layoutResourceId: Int = R.layout.activity_scan
    override val baseViewModel: ScanViewModel by viewModel()
    var capture: CaptureManagerEx? = null

    var isTouching = false

    val preferenceController = PreferenceController.instance

    override fun initStartView() {
        viewDataBinding.run {
            view = this@ScanActivity

            vDecorateBarcode.post {
                val size = Size(
                    vDecorateBarcode.measuredWidth,
                    vDecorateBarcode.measuredHeight
                )
                vDecorateBarcode.barcodeView.framingRectSize = size

                capture = CaptureManagerEx(this@ScanActivity, vDecorateBarcode)
                capture?.initializeFromIntent(intent, null)
                capture?.decode(this@ScanActivity)
                capture?.onResume()
                disableLaser()
            }

            btnGuide.setOnClickListener {
                GuideView(this@ScanActivity).show(parentView = viewDataBinding.root as ViewGroup)
            }

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

            val isFirst = preferenceController.getBoolean("isFirst", true)
            if (isFirst){
                GuideView(this@ScanActivity).show(parentView = viewDataBinding.root as ViewGroup)
                preferenceController.putBoolean("isFirst", false)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouching = true
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                isTouching = false
                return false
            }
        }
        return false
    }


    override fun initAfterBinding() {
        baseViewModel.run {

        }
    }

    override fun barcodeResult(result: BarcodeResult?) {
        result?.let {
            if (it.barcodeFormat == BarcodeFormat.QR_CODE && it.toString().contains("dhlottery.co.kr")) {
                val code = it.text
//                Toast.makeText(this@ScanActivity, code.toString(), Toast.LENGTH_SHORT).show()

//                throw RuntimeException("Test Crash") // Force a crash
                //터치 안함
                val intent = Intent(this, ResultActivity::class.java).apply {
                    putExtra("isFake", isTouching)
                    putExtra("url", result.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "로또종이 상단에 위치한 QR코드를 인식해주세요.", Toast.LENGTH_SHORT).show()
                capture?.decode(this@ScanActivity)
                capture?.onResume()
            }
        }
    }

    override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {

    }

    private fun disableLaser() {
        val viewFinder: ViewfinderView = viewDataBinding.vDecorateBarcode.viewFinder
        var scannerAlphaField: Field? = null
        try {
            scannerAlphaField = viewFinder.javaClass.getDeclaredField("SCANNER_ALPHA")
            scannerAlphaField.isAccessible = true
            scannerAlphaField.set(viewFinder, IntArray(1))
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    override fun onRestart() {
        super.onRestart()
        isTouching = false
        capture?.decode(this@ScanActivity)
        capture?.onResume()
    }

    override fun onResume() {
        super.onResume()
        capture?.decode(this)
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }

    inner class CaptureManagerEx(
        activity: Activity,
        private val barcodeView: DecoratedBarcodeView
    ) :
        CaptureManager(activity, barcodeView) {

        private lateinit var callbackWrapper: BarcodeCallback
        private val handlerInternal = Handler()
        private val callbackInternal: BarcodeCallback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                barcodeView.pause()
                handlerInternal.post {
                    callbackWrapper.barcodeResult(result)
                }

            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                callbackWrapper.possibleResultPoints(resultPoints)
            }
        }

        fun decode(callback: BarcodeCallback) {
            this.callbackWrapper = callback
            decode()
        }

        fun reStart() {
            barcodeView.resume()
            decode()
        }

        override fun decode() {
            barcodeView.decodeSingle(callbackInternal)
        }
    }
}