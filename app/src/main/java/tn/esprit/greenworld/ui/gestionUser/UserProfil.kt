package tn.esprit.greenworld.ui.gestionUser

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUserProfiBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class UserProfileFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityUserProfiBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityUserProfiBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        getUser()
        onResume()
        binding.btnSetting.setOnClickListener { showBottomSheet(it) }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        getUser() // Fetch user data every time the fragment becomes visible
    }
    private fun getUser() {
        val userId = sharedPreferences.getString("userId", "")?.toString() ?: 0L
        val userInterface = RetrofitImp.buildRetrofit().create(UserInterface::class.java)
        userInterface.getUserById(userId.toString()).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user -> updateUI(user) }
                } else {
                    showSnackbar("Error fetching user data")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                showSnackbar("Network error: ${t.message}")
            }
        })
    }

    private fun updateUI(user: User) {
        binding.tvName.text = user.userName
        binding.txtEmail.text = user.email
        binding.txtNum.text = user.numTel
        binding.txtLn.text = user.loginCount.toString()
        binding.txtTime.text = user.totalTimeSpent

        Glide.with(this)
            .load(user.imageRes)
            .apply(RequestOptions().placeholder(R.drawable.avatar).error(R.drawable.ic_apple))
            .into(binding.profileImage)
    }

    private fun showBottomSheet(view: View) {
        val bottomSheetFragment = OptionsBottomSheetFragment()
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_options_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        view.findViewById<TextView>(R.id.tvUser).setOnClickListener {
            startActivity(Intent(requireContext(), UpdateUserFiled::class.java))
            dismiss()
        }
    }
}
