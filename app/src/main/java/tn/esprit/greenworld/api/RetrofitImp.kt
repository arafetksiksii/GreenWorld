package tn.esprit.greenworld.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImp {


    companion object {

        var BASE_URL = "http://172.16.8.176:5000/"

        fun buildRetrofit() : Retrofit {

            return  Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

        }
    }
}