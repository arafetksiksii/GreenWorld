package tn.esprit.greenworld.adapters

import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.CommentairesItemBinding
import tn.esprit.greenworld.models.Comment

interface CommentActionListener {
    fun onDeleteComment(commentId: String)
    fun onUpdateComment(commentId: String)
}

class CommentAdapter(
    private var comments: MutableList<Comment>,
    private val listener: CommentActionListener,
    private val currentUserID: String? // Add this property
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    var selectedCommentId: String? = null

    fun updateComments(updatedComments: List<Comment>) {
        comments.clear()
        comments.addAll(updatedComments)
        notifyDataSetChanged()
    }

    fun getCommentById(commentId: String): Comment? {
        return comments.find { it._id == commentId }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentairesItemBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding, listener, currentUserID)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        Log.d("Comments", "Binding comment at position $position: ${comment._id}")
        holder.bind(comment)
    }

    override fun getItemCount(): Int = comments.size

    inner class CommentViewHolder(
        private val binding: CommentairesItemBinding,
        private val listener: CommentActionListener,
        private val currentUserID: String? // Add this property
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteIcon.setOnClickListener {
                val commentId = comments[adapterPosition]._id
                selectedCommentId = commentId
                listener.onDeleteComment(commentId)
            }

            binding.updateIcon.setOnClickListener {
                val commentId = comments[adapterPosition]._id
                selectedCommentId = commentId
                val selectedComment = comments.find { it._id == commentId }
                selectedComment?.let {
                    showUpdateDialog(selectedComment)
                }
            }
        }

        private fun showUpdateDialog(comment: Comment) {
            val alertDialogBuilder = AlertDialog.Builder(binding.root.context)
            alertDialogBuilder.setTitle("Mise à jour du commentaire")

            val editText = EditText(binding.root.context)
            editText.setText(comment.Contenu)
            alertDialogBuilder.setView(editText)

            alertDialogBuilder.setPositiveButton("Mettre à jour") { _, _ ->
                val updatedContent = editText.text.toString()
                comment.Contenu = updatedContent
                notifyDataSetChanged()
                listener.onUpdateComment(comment._id)
            }

            alertDialogBuilder.setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        fun bind(comment: Comment) {
            Log.d("CommentAdapter", "comment.userID: ${comment.userID}, currentUserID: $currentUserID")

            val editableComment = Editable.Factory.getInstance().newEditable(comment.Contenu)
            binding.comment.text = editableComment
            binding.userName.text = comment.userName
            if (!comment.userImage.isNullOrEmpty()) {
                Glide.with(binding.profileImage.context)
                    .load(comment.userImage)
                    .placeholder(R.drawable.circle_background)
                    .error(com.cloudinary.android.R.drawable.mtrl_ic_error)
                    .into(binding.profileImage)
            } else {
                binding.profileImage.setImageResource(R.drawable.profil
                )
            }
            if (comment.userID == currentUserID) {
                binding.updateIcon.visibility = View.VISIBLE
                binding.deleteIcon.visibility = View.VISIBLE
            } else {
                binding.updateIcon.visibility = View.GONE
                binding.deleteIcon.visibility = View.GONE
            }
        }
    }
}