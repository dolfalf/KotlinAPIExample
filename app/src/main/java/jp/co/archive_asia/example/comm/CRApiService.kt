package jp.co.archive_asia.kotlinapiexample.comm

import jp.co.archive_asia.kotlinapiexample.model.QiitResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://qiita.com/api/v2/items?page=1&per_page=20

interface CRApiService {

    //パラメータの前までをここに書く
    @GET("api/v2/items")
    //呼び出す際に必要なリクエストURLのパラメータの設定
    fun apiDemo(
        @Query("page") page: Int,
        @Query("par_page") perPage: Int
    ): Call<List<QiitResponse>>
}