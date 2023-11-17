package com.example.pdm.interfaces

import Quiz
import com.example.pdm.models.Question
import retrofit2.Call
import retrofit2.http.*

interface QuizApi {
    @GET("quiz") // Remplacez "quizzes" par le chemin de votre endpoint
    fun getQuizzes(): Call<List<Quiz>>
    @GET("question/aaa/{quizId}")
    fun getQuestionsForQuiz(@Path("quizId") quizId: String): Call<List<Question>>
}
