package com.sahil.photojaanictest.Retrofit

import retrofit2.Call
import retrofit2.http.GET
import com.sahil.photojaanictest.Model.TaskModel
import retrofit2.http.Path

interface RestApi {

    @GET("test/{page}")
    fun getAllCountries(@Path("page") path:Int) : Call<List<TaskModel>>
}