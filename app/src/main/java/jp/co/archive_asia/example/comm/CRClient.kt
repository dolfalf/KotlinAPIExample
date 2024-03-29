package jp.co.archive_asia.kotlinapiexample.comm

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CRClient {

    //Clientを作成
    val httpBuilder: OkHttpClient.Builder get() {

        //httpClinetのBuilderを作る
        val httpClient = OkHttpClient.Builder()
        //create http client　headerの追加
        httpClient.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Accept", "application/json")
                .method(original.method(), original.body())
                .build()
            //proceedメソッドは再びパーミッション許可ダイアログを表示してその結果を返します
            var response = chain.proceed(request)

            return@Interceptor response
        })
            .readTimeout(30, TimeUnit.SECONDS)

        //log
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(loggingInterceptor)

        return httpClient
    }

    fun createService(): CRApiService {
        var client = httpBuilder.build()
        var retrofit = Retrofit.Builder()
            .baseUrl("https://qiita.com/")//基本のurl設定
            .addConverterFactory(GsonConverterFactory.create())//Gsonの使用
            .client(client)//カスタマイズしたokhttpのクライアントの設定
            .build()
        //Interfaceから実装を取得
        var API = retrofit.create(CRApiService::class.java)

        return API
    }

}
