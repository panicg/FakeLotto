package com.panicdev.panic.base
import com.panicdev.fakelotto.main.viewmodel.RealViewModel
import com.panicdev.fakelotto.main.viewmodel.ScanViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * okHttp Client
 */
val client: OkHttpClient = OkHttpClient.Builder().apply {
    val log = HttpLoggingInterceptor()
    log.level = HttpLoggingInterceptor.Level.BODY
    addInterceptor(log)
}.build()

var m = module {

//    factory<HotelDataModel> { HotelDataModelImpl(get()) }

    viewModel { ScanViewModel() }
    viewModel { RealViewModel() }



}

var restAPIModule = module {


    /**
     * Airplane ticket
     */
//    single<AirplaneAPIInterface> {
//        Retrofit.Builder().apply {
//            baseUrl(NetworkConstants.BASE_URL)
//            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            addConverterFactory(GsonConverterFactory.create())
//            client(client)
//        }.build().create(AirplaneAPIInterface::class.java)
//    }
}


var trModules = listOf(m, restAPIModule)

