package tn.esprit.greenworld.repositories
import tn.esprit.greenworld.API.CommentApi
import tn.esprit.greenworld.models.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.utils.RetrofitInstance

class CommentRepository {
    private val commentApi: CommentApi = RetrofitInstance.apiii

    fun addComment(eventId: String, comment: Comment, callback: (Boolean) -> Unit) {
        commentApi.postCommentForEvent(comment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                callback(false)
            }
        })
    }
    fun getCommentsByEvent(eventId: String, callback: (List<Comment>?) -> Unit) {
        commentApi.getCommentsByEvent(eventId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    val comments = response.body()
                    callback(comments)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                callback(null)
            }
        })
    }
    fun deleteComment(commentId: String, callback: (Boolean) -> Unit) {
        commentApi.deleteComment(commentId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback(false)
            }
        })
    }
    fun updateComment(commentId: String, updatedComment: Comment, callback: (Boolean) -> Unit) {
        commentApi.updateComment(commentId, updatedComment).enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                callback(false)
            }
        })
    }


}

