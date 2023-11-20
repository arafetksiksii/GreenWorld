package tn.esprit.green_world.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.green_world.interfaces.CommandeApi
import tn.esprit.green_world.interfaces.ProduitApi
import tn.esprit.green_world.interfaces.ProduitfavApi

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

