package tn.esprit.greenworld.ui.gestionUser

import android.content.Context

import android.content.SharedPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import tn.esprit.greenworld.R

class UserProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewNum: TextView
    private lateinit var textViewLoginNumber: TextView
    private lateinit var textViewTimePass: TextView
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_user_profi, container, false)

        val userName = sharedPreferences.getString("userName", "")
        val userEmail = sharedPreferences.getString("userEmail", "")
        val userImageRes = sharedPreferences.getString("userImageRes", "")
        val userNumTel = sharedPreferences.getString("userNumTel", "")
        val userTimePass = sharedPreferences.getString("userTimePass", "")
        val userNBLogin = sharedPreferences.getString("userNBLogin", "")
        Log.d(
            "UserFragment",
            "UserName: $userName, UserEmail: $userEmail, UserImageRes: $userImageRes"
        )

        textViewName = view.findViewById(R.id.tv_name)
        textViewEmail = view.findViewById(R.id.txt_email)
        textViewNum = view.findViewById(R.id.txt_num)
        textViewLoginNumber = view.findViewById(R.id.txt_Ln)
        textViewTimePass = view.findViewById(R.id.txt_time)
        imageView = view.findViewById(R.id.profile_image)

        textViewName.text = userName
        textViewEmail.text = userEmail
        textViewNum.text = userNumTel
        textViewLoginNumber.text = userNBLogin
        textViewTimePass.text = userTimePass
        Glide.with(this)
            .load(userImageRes)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_apple)
                    .error(R.drawable.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)

        return view
    }
}
