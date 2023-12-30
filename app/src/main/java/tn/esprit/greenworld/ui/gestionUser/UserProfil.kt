package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
import android.content.Intent

import android.content.SharedPreferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.PopupMenu

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

import tn.esprit.greenworld.R

import tn.esprit.greenworld.databinding.ActivityUserProfiBinding

class UserProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityUserProfiBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityUserProfiBinding.inflate(inflater, container, false)



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



        binding.tvName.text = userName
        binding.txtEmail.text = userEmail
        binding.txtNum.text = userNumTel
        binding.txtLn.text = userNBLogin
        binding.txtTime.text = userTimePass

        Glide.with(this)
            .load(userImageRes)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_apple)
                    .error(R.drawable.avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(binding.profileImage)
        binding.btnSetting.setOnClickListener { showPopupMenu(it) }
        // Handle Return button click

        return binding.root
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.setting_options_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuUpdatePassword -> {

                    startActivity(Intent(this.context, Update_Password::class.java))
                    true
                }

                R.id.menuUpdateEmail -> {

                    startActivity(Intent(this.context, Update_Email::class.java))
                    true
                }

                R.id.menuUserUpdate -> {
                    startActivity(Intent(this.context, UserUpdate_Image::class.java))
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

}

