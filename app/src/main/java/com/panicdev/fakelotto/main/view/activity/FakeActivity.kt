package com.panicdev.fakelotto.main.view.activity

import com.panicdev.fakelotto.databinding.ActivityFakeBinding
import com.panicdev.fakelotto.main.viewmodel.FakeViewModel
import com.panicdev.panic.base.BaseActivity
import com.panicdev.fakelotto.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class FakeActivity : BaseActivity<ActivityFakeBinding, FakeViewModel>() {
    override val layoutResourceId: Int = R.layout.activity_fake
    override val baseViewModel: FakeViewModel by viewModel()

    override fun initStartView() {
        viewDataBinding.run {


        }
    }

    override fun initAfterBinding() {
        baseViewModel.run {

        }
    }
}