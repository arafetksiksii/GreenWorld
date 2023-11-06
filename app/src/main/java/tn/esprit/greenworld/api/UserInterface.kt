package tn.esprit.greenworld.api

import retrofit2.Call
import retrofit2.http.*
import tn.esprit.greenworld.models.User

interface UserInterface {
    // Créer un nouvel utilisateur
    @POST("users")
    fun createUser(@Body user: User): Call<User>

    // Obtenir un utilisateur par ID
    @GET("users/{id}")
    fun getUserById(@Path("id") userId: Long): Call<User>

    // Mettre à jour les informations d'un utilisateur
    @PUT("users/{id}")
    fun updateUser(@Path("id") userId: Long, @Body updatedUser: User): Call<User>

    // Supprimer un utilisateur
    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: Long): Call<Void>

    // Obtenir la liste de tous les utilisateurs
    @GET("users")
    fun getAllUsers(): Call<List<User>>

    // Rechercher un utilisateur par nom
    @GET("users/search")
    fun searchUserByName(@Query("name") name: String): Call<List<User>>
}
