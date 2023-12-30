package tn.esprit.greenworld.utils


import retrofit2.Call
import retrofit2.http.*
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User1
import tn.esprit.greenworld.models.User4
import tn.esprit.greenworld.models.User5
import tn.esprit.greenworld.models.UserData

interface UserInterface {
    // Créer un nouvel utilisateur
    @POST("user")
    fun createUser(@Body user: User): Call<User>

    // Obtenir un utilisateur par ID
    @GET("user/{id}")
    fun getUserById(@Path("id") userId: Long): Call<User>

    // Mettre à jour les informations d'un utilisateur
    @PUT("user/")
    fun updateUser( @Body updatedUser: User5): Call<User>
    @PUT("user/updateR")
    fun updateUser2( @Body updatedUser: User1): Call<User>
    // Supprimer un utilisateur
    @DELETE("user/{id}")
    fun deleteUser(@Path("id") userId: Long): Call<Void>

    // Obtenir la liste de tous les utilisateurs
    @GET("user")
    fun getAllUsers(): Call<List<User>>

    // Rechercher un utilisateur par nom
    @GET("user/search")
    fun searchUserByName(@Query("name") name: String): Call<List<User>>

    @PUT("user/updateProfilById")
    fun updateScoreById(@Body updatedUser: User4): Call<User>

    @PUT("admin/newPassword")
    fun updatePassword(@Body updatedUser: User1): Call<User>




}
