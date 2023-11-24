package tn.esprit.greenworld.fragments
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import tn.esprit.greenworld.databinding.FragmentCommentBottomSheetBinding
import tn.esprit.greenworld.models.Comment
import tn.esprit.greenworld.repositories.CommentRepository
import com.google.android.material.snackbar.Snackbar
import io.github.muddz.styleabletoast.StyleableToast
import tn.esprit.greenworld.R
import java.util.Date

class CommentBottomSheet : DialogFragment() {
    private lateinit var binding: FragmentCommentBottomSheetBinding
    private lateinit var userId: String
    private lateinit var eventId: String
    private lateinit var commentRepository: CommentRepository
    private lateinit var sharedPreferences: SharedPreferences // Déplacer ici


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentBottomSheetBinding.inflate(inflater, container, false)
        commentRepository = CommentRepository()
        // Récupérez les ID de l'événement et de l'utilisateur depuis SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        eventId = sharedPreferences.getString("event_id", "") ?: ""
        userId = sharedPreferences.getString("user_id", "") ?: ""

        val userIDEditText = binding.userID
        userIDEditText.text = userId

        val eventIDEditText = binding.eventID
        eventIDEditText.text = eventId

        // Set up your UI elements and actions here

        binding.envoyer.setOnClickListener {
            postComment()
        }

        return binding.root
    }
    private fun postComment() {
        val eventId = binding.eventID.text.toString()
        val commentContent = binding.commentText.text.toString()
        val userId = sharedPreferences.getString("userId", "")
        val userName = sharedPreferences.getString("user_pref", "")

        if (commentContent.isNotEmpty()) {
            val newComment = Comment(
                _id = "dummy_id",
                Contenu = commentContent,
                date = Date(),
                eventID = eventId,
                userID = userId.toString(),
                __v = 0
            )

            commentRepository.addComment(eventId, newComment) { isSuccess ->
                if (isSuccess) {
                    context?.let { StyleableToast.makeText(it, "envoyé", Toast.LENGTH_LONG, R.style.mytoast).show() };

                    dismiss() // Fermer le Bottom Sheet après avoir ajouté le commentaire avec succès
                } else {
                    Snackbar.make(
                        binding.root,
                        "Échec de l'ajout du commentaire",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Snackbar.make(
                binding.root,
                "Le commentaire ne peut pas être vide",
                Snackbar.LENGTH_SHORT
            ).show()

        }}}