package tn.esprit.greenworld.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.greenworld.API.CommentApi
import tn.esprit.greenworld.API.EventApi
import tn.esprit.greenworld.API.TypeApi
import tn.esprit.greenworld.interfaces.ProduitApi
import tn.esprit.greenworld.interfaces.CommandeApi

object RetrofitInstance {

   var a ="http://10.0.2.2:9090/"
    val api: ProduitApi by lazy {
        Retrofit.Builder()
            .baseUrl(a)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProduitApi::class.java)
    }

    val Commandeapi: CommandeApi by lazy {
        Retrofit.Builder()
            .baseUrl(a)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommandeApi::class.java)
    }

    val apiEvent: EventApi by lazy {
        Retrofit.Builder()
            .baseUrl(a)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApi::class.java)
    }

    val apii: TypeApi by lazy {
        Retrofit.Builder()
            .baseUrl(a)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TypeApi::class.java)
    }
    val apiii: CommentApi by lazy {
        Retrofit.Builder()
            .baseUrl(a)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CommentApi::class.java)
    }
}

