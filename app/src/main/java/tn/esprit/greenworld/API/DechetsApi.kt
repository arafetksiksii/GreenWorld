package tn.esprit.greenworld.API


import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import tn.esprit.greenworld.models.Dechets
import tn.esprit.greenworld.models.DechetsItem


interface DechetsApi {
    @POST("/dechets")
     fun addDechets(@Body dechets: DechetsItem): Call<Dechets>

    @Multipart
    @POST("dechets/")
    fun createDechetsWithImage(
        @Part("Type_dechets") Type_dechets: String,
        @Part("date_depot") date_depot: String,
        @Part("nombre_capacite") nombre_capacite: Int,
        @Part("adresse") adresse: String,
        @Part file: MultipartBody.Part
    ): Call<Dechets>
}