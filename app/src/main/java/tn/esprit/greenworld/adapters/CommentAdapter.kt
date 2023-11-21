
package tn.esprit.greenworld.adapters
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.greenworld.databinding.CommentairesItemBinding
import tn.esprit.greenworld.models.Comment

class CommentAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentairesItemBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        Log.d("Comments", "Binding comment at position $position: ${comment._id}")
        holder.bind(comment)
    }


    override fun getItemCount(): Int = comments.size


    class CommentViewHolder(private val binding: CommentairesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            val editableComment = Editable.Factory.getInstance().newEditable(comment.Contenu)

            binding.comment.text = editableComment
        }
    }
}
