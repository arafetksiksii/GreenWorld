
package tn.esprit.greenworld.ui.gestionUser
import android.content.SharedPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import tn.esprit.greenworld.R

class UserProfileFragment : Fragment() {

    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var imageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences // Déplacer ici

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_user_profi, container, false)

        // Initialiser sharedPreferences ici
        sharedPreferences = requireContext().getSharedPreferences("user_pref", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")

        // Retrieve user details from SharedPreferences
        val userName = sharedPreferences.getString("userName", "")
        val userEmail = sharedPreferences.getString("userEmail", "")
        val userImageRes = sharedPreferences.getString("userImageRes", "")
        Log.d("ddddddddddddddd", userImageRes.toString())

        // Initialize TextViews and ImageView
        textViewName = view.findViewById(R.id.textViewName)
        textViewEmail = view.findViewById(R.id.textViewEmail)
        imageView = view.findViewById(R.id.imageViewProfile)

        // Set user details to UI
        textViewName.text = userName
        textViewEmail.text = userEmail

        // Load user image using Glide
        Glide.with(this)
            .load(userImageRes)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_apple)
                    .error(R.drawable.ellipse_background)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Caching strategy
            )
            .into(imageView)

        return view
    }
}
