package tn.esprit.greenworld.utils


import retrofit2.Call
import retrofit2.http.*
import tn.esprit.greenworld.models.ClassementItem
import tn.esprit.greenworld.models.Question
import tn.esprit.greenworld.models.Quiz

interface QuizApi {
    @GET("quiz") // Remplacez "quizzes" par le chemin de votre endpoint
    fun getQuizzes(): Call<List<Quiz>>
    @GET("question/aaa/{quizId}")
    fun getQuestionsForQuiz(@Path("quizId") quizId: String): Call<List<Question>>
    @GET("user")
    fun getClassement(): Call<List<ClassementItem>>
}
