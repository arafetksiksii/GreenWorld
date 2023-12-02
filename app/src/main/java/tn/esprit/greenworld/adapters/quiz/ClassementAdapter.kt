package tn.esprit.greenworld.adapters.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.ClassementItem

class ClassementAdapter(private val classementList: List<ClassementItem>) : RecyclerView.Adapter<ClassementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_classement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = classementList[position]
        holder.tvUserName.text = item.userName
        holder.tvScore.text = item.score.toString()
        holder.txt_classement.text = "${position + 1}."
// Définir l'image de la médaille en fonction de la position
        when (position) {
            0 -> holder.img_medaille.setImageResource(R.drawable.img_medaille1) // médaille d'or pour le premier
            1 -> holder.img_medaille.setImageResource(R.drawable.img_medaille2) // médaille d'argent pour le deuxième
            2 -> holder.img_medaille.setImageResource(R.drawable.img_medaille3) // médaille de bronze pour le troisième
            else -> holder.img_medaille.visibility = View.GONE // pas de médaille pour les autres
        }    }

    override fun getItemCount(): Int = classementList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tvUserName)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
        val img_medaille: ImageView = view.findViewById(R.id.img_medaille)
        val txt_classement: TextView = view.findViewById(R.id.tvPosition)


        // Déclarez ici d'autres vues de l'item si nécessaire
    }
}
