package tn.esprit.green_world.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.green_world.interfaces.ProduitApi

object RetrofitInstance {
    val api:ProduitApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.166:9090/produit/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProduitApi::class.java)

    }
}