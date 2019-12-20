package jp.co.archive_asia.kotlinapiexample

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import jp.co.archive_asia.kotlinapiexample.comm.CRClient
import jp.co.archive_asia.kotlinapiexample.model.QiitResponse

import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import jp.co.archive_asia.kotlinapiexample.viewModel.QiitaModel as QiitaModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            //Request!
            fetchAllUserData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    //データリストに保存し、そのデータの取得
    fun fetchAllUserData(): List<QiitaModel> {

        val dataList = mutableListOf<QiitaModel>()
        //リクエストURl作成してデータとる パラメータの引数の設定も行う
        CRClient().createService().apiDemo(page = 1, perPage = 20).enqueue(object :
            Callback<List<QiitResponse>> {

            //非同期処理
            override fun onResponse(call: Call<List<QiitResponse>>, response: Response<List<QiitResponse>>) {
                Log.d("TAGres","onResponse")

                //ステータスコードが200：OKなので、ここではちゃんと通信できたよ
                if (response.isSuccessful) {
                    response.body()?.let {
                        for (item in it) {
                            val data: QiitaModel = QiitaModel().also {
                                //取得したいものをAPIから手元のリスト（Model）に
                                it.title = item.title
                                it.url = item.url
                                it.id = item.user!!.id
                            }
                            //取得したデータをModelに追加
                            dataList.add(data)
                        }
                        //今回recyclerViewを利用しているが、これを書かないと先に画面の処理が終えてしまうので表示されなくなります。
                        //recyclerView.adapter?.notifyDataSetChanged()
                    }
                } else {
                }
            }
            override fun onFailure(call: Call<List<QiitResponse>>, t: Throwable) {
                Log.d("TAGres","onFailure")
            }
        })
        return dataList
    }
}
