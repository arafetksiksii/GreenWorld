package tn.esprit.greenworld.models

data class Question(
    val _id: String, // L'ID MongoDB de la question
    val id_quiz: String, // L'ID du quiz associé à cette question
    val question: String, // Le texte de la question
    val choix: List<String>, // La liste des choix
    val reponse_correcte: Int // L'index du choix correct
)