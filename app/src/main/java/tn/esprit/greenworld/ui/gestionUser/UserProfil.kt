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
import android.widget.TextView

import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
                    .transform(RoundedCorners(16))  // 16 is the radius in pixels, adjust as needed
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(binding.profileImage)

        binding.btnSetting.setOnClickListener {showBottomSheet(it) }



        return binding.root
    }

    private fun showBottomSheet(view: View) {
        val bottomSheetFragment = OptionsBottomSheetFragment()
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }



}

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_options_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle item clicks here
        view.findViewById<TextView>(R.id.tvUpdatePassword).setOnClickListener {
            startActivity(Intent(requireContext(), Update_Password::class.java))
            dismiss()
        }

        view.findViewById<TextView>(R.id.tvUpdateEmail).setOnClickListener {
            startActivity(Intent(requireContext(), Update_Email::class.java))
            dismiss()
        }

        view.findViewById<TextView>(R.id.tvUserUpdate).setOnClickListener {
            startActivity(Intent(requireContext(), UserUpdate_Image::class.java))
            dismiss()
        }
    }
}

