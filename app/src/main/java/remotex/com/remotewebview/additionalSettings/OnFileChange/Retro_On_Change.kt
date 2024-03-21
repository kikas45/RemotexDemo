package remotex.com.remotewebview.additionalSettings.OnFileChange

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import remotex.com.remotewebview.additionalSettings.utils.Constants.Companion.BASE_URL_ON_CHANGE

object Retro_On_Change {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ON_CHANGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiOnChnage by lazy {
        retrofit.create(ApiOnChnage::class.java)
    }
}
