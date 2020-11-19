package com.panicdev.fakelotto.main.view.activity

import android.app.Activity
import android.os.Handler
import com.panicdev.fakelotto.R
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.*
import com.panicdev.fakelotto.databinding.ActivityScanBinding
import com.panicdev.fakelotto.main.viewmodel.ScanViewModel
import com.panicdev.panic.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.Field

/**
 * 카드 현금 QR 환급 영수증조회 스캐너
 */
class ScanActivity : BaseActivity<ActivityScanBinding, ScanViewModel>(), BarcodeCallback {
    override val layoutResourceId: Int = R.layout.activity_scan
    override val baseViewModel: ScanViewModel by viewModel()
    var capture: CaptureManagerEx? = null


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
        }
    }

    override fun initAfterBinding() {
        baseViewModel.run {

        }
    }

    override fun barcodeResult(result: BarcodeResult?) {
        result?.let {
            val code = it.text
//            mPresenter.decode(result)
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