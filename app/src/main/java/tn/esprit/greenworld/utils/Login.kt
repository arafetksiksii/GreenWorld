package tn.esprit.greenworld.utils

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.models.User2
import tn.esprit.greenworld.models.User3
import tn.esprit.greenworld.models.UserData

interface Login {


    @POST("auth/login") // Assurez-vous d'utiliser le bon endpoint sur votre serveur

    fun login(@Body user: User1): Call<User>

    @POST("/user") // Assurez-vous d'utiliser le bon endpoint sur votre serveur
    fun register(@Body user: User2): Call<User>

    @POST("user/sendResetCode")
    fun sendResetCode(@Body user: User3): Call<User>


    @POST("/auth/loginfb") // Replace with your API endpoint
    fun loginWithFacebook(@Body userData: UserData): Call<User>


    @POST("auth/logout/{id}")
    fun logout(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): Call<Void>


}