package tn.esprit.greenworld.API

import tn.esprit.greenworld.models.Comment
import tn.esprit.greenworld.models.Reservation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApi {
    @POST("comment")
    fun postCommentForEvent(
        @Body comment: Comment
    ): Call<Comment>
    @GET("comment/event/{eventId}")
    fun getCommentsByEvent(@Path("eventId") eventId: String): Call<List<Comment>>
    @DELETE("comment/{commentId}")
    fun deleteComment(@Path("commentId") commentId: String): Call<Unit>
    @PATCH("comment/{commentId}")
    fun updateComment(
        @Path("commentId") commentId: String,
        @Body updatedComment: Comment
    ): Call<Comment>

}