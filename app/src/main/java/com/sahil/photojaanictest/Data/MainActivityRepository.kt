package com.sahil.photojaanictest.Data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.sahil.photojaanictest.BuildConfig
import com.sahil.photojaanictest.Model.TaskModel
import com.sahil.photojaanictest.Retrofit.RestApi
import io.realm.Realm
import io.realm.Sort
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivityRepository: PageKeyedDataSource<String, List<TaskModel>>() {

    val TAG = MainActivityRepository::class.java.simpleName
    var page=1
    private lateinit var realm: Realm
    private lateinit var userDetailsList: MutableLiveData<List<TaskModel>>
    val interceptor = HttpLoggingInterceptor()
    val client = OkHttpClient.Builder().addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    val gson = Gson()
    val retrofit =  Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BuildConfig.BASEURL)
        .build()

    val restApi = retrofit.create(RestApi::class.java)

    val x= loadTask()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, List<TaskModel>>) {

        restApi.getAllCountries(1).enqueue(object : Callback<List<TaskModel>> {

            override fun onFailure(call: Call<List<TaskModel>>?, t: Throwable?) {
                Log.e(TAG, "OOPS!! something went wrong.."+ t!!.message)
            }

            override fun onResponse(
                call: Call<List<TaskModel>>?,
                response: Response<List<TaskModel>>?
            ) {

                Log.e(TAG, response!!.body().toString())
                when (response.code()) {
                    200 -> {
                        Thread {
                            page = page++
                            for (items in response.body()!!){
                                addDetails(items)
                            }
                        }.start()
                    }
                }

            }
        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, List<TaskModel>>) {
        Log.e(TAG, "OOPS!! $params")
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, List<TaskModel>>) {

        restApi.getAllCountries(2).enqueue(object : Callback<List<TaskModel>> {

            override fun onFailure(call: Call<List<TaskModel>>?, t: Throwable?) {
                Log.e(TAG, "OOPS!! something went wrong.."+ t!!.message)
            }

            override fun onResponse(
                    call: Call<List<TaskModel>>?,
                    response: Response<List<TaskModel>>?
            ) {

                Log.e(TAG, response!!.body().toString())
                when (response.code()) {
                    200 -> {
                        Thread {
                            for (items in response.body()!!){
                                addDetails(items)
                            }

                        }.start()
                    }
                }

            }
        })
    }

    private fun loadTask(): String {
        var xs=""
        restApi.getAllCountries(1).enqueue(object : Callback<List<TaskModel>> {

            override fun onFailure(call: Call<List<TaskModel>>?, t: Throwable?) {
                Log.e(TAG, "OOPS!! something went wrong.."+ t!!.message)
                xs= t.message.toString()
            }

            override fun onResponse(
                call: Call<List<TaskModel>>?,
                response: Response<List<TaskModel>>?
            ) {

                Log.e(TAG, response!!.body().toString())
                when (response.code()) {
                    200 -> {
                        Thread {
                            page = page++
                            for (items in response.body()!!){
                                addDetails(items)
                            }
                        }.start()
                    }
                }
                xs= response!!.body().toString()
            }
        })
        return xs
    }

    fun getTasks() : MutableLiveData<List<TaskModel>> {

        userDetailsList=MutableLiveData()
        realm = Realm.getDefaultInstance()
        val realmUserList = realm.where(TaskModel::class.java).sort("index", Sort.ASCENDING).findAll()
        for (s in 0 until realmUserList.size) {
            userDetailsList.value=realmUserList
        }
        return userDetailsList

    }

    fun addDetails(items: TaskModel) {
        realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
        } catch (e: Exception) {
            Log.d("TAG","transaction failed")
        }
        realm.insertOrUpdate(items)
        realm.commitTransaction()
    }


}