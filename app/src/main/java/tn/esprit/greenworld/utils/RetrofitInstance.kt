package tn.esprit.greenworld.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.greenworld.interfaces.ProduitApi
import tn.esprit.greenworld.interfaces.CommandeApi

object RetrofitInstance {


    val api: ProduitApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.56.1:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProduitApi::class.java)
    }

    val Commandeapi: CommandeApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.56.1:9090/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommandeApi::class.java)
    }


}

