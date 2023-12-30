package tn.esprit.greenworld.ui.gestionUser

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.MIDrawerActivity
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityUserUpdateBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User5
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class UserUpdate_Image : AppCompatActivity() {
    var config: HashMap<String, String> = HashMap()
    private lateinit var binding: ActivityUserUpdateBinding
    private lateinit var selectedImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private val PASSWORD_KEY = "password"


    // Define a shared preference name
    private val PREF_NAME = "user_pref"
    private val USER_ID_KEY = "userId"
    private val USER_NAME_KEY = "userName"
    private val USER_EMAIL_KEY = "userEmail"
    private val USER_IMAGE_KEY = "userImageRes"
    private val USER_TOKEN_KEY = "tokenLogin"
    private val USER_TIMEPass_KEY = "userTimePass"
    private val USER_NBLOGIN_KEY = "userNBLogin"
    private val USER_SCORE_KEY = "userScore"
    private val PASSWORD_KEY_NEW = "new_password"
    private val USER_EMAIL_KEY_NEW = "new_email"

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReturn.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Check if MediaManager is already initialized

            // Initialization code for MediaManager
            config["cloud_name"] = "dznvwntjn"
            config["api_key"] = "972319243848173"
            config["api_secret"] = "xp2G8BXbjvjec0dbFIaQbUJ3Mj8"
            MediaManager.init(this, config)




        selectedImageView = findViewById(R.id.selectedImageView)
        val userImageRes = sharedPreferences.getString("USER_IMAGE_KEY", "")

        // Check if userImageRes is not empty and then set the image to selectedImageView
        if (userImageRes?.isNotEmpty() == true) {
            // Use your preferred image loading library or method to set the image
            // For example, if using Glide:
            Glide.with(this).load(userImageRes).into(selectedImageView)
        }

        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            checkCameraPermission()
        }

        val uploadImageButton: Button = findViewById(R.id.uploadImageButton)
        uploadImageButton.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageToCloudinary(selectedImageUri!!)
            } else {
                Toast.makeText(this, "Select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            // Permission already granted, proceed with camera launch
            launchImageSelection()
        }
    }

    private fun launchImageSelection() {
        val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST)
    }

    private var selectedImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageView.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToCloudinary(imageUri: Uri) {
        MediaManager.get().upload(imageUri).callback(object : UploadCallback {
            override fun onStart(requestId: String?) {}

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                val publicId = resultData?.get("public_id")
                val url = MediaManager.get().url().generate(publicId.toString())
                updateUserp(url)
                Toast.makeText(
                    this@UserUpdate_Image,
                    "Image uploaded successfully. URL: $url",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Toast.makeText(
                    this@UserUpdate_Image,
                    "Error uploading the image: ${error?.description}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
        }).dispatch()
    }

    private fun updateUserp(imageUrl: String) {
        val userId = sharedPreferences.getString("userId", "")

        val user5 = User5(
            id = userId?.toString() ?: "",
            email = "",
            nom = "",
            prenom = "",
            userName = "",
            adress = "",
            cin = "",
            imageRes = imageUrl
        )

        val imageService = RetrofitImp.buildRetrofit().create(UserInterface::class.java)
        imageService.updateUser(user5).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    handleSuccessfulupdate(response.body())
                } else {
                    handleUpdateFailure(response.errorBody()?.string().toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                handleUpdateFailure(t.message ?: "Unknown error")
            }
        })
    }

    private fun handleSuccessfulupdate(user: User?) {
        if (user != null) {
            saveUserToPreferences(user)
            startActivity(Intent(this@UserUpdate_Image, MIDrawerActivity::class.java))
            finish()
        } else {
            // Handle the case where user is null, e.g., show an error message
            Log.e("UserUpdate_Image", "Received null user in handleSuccessfulupdate")
            showSnackbar("Error updating user information")
        }
    }


    private fun handleUpdateFailure(errorMessage: String) {
        showSnackbar(errorMessage)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    private fun saveUserToPreferences(user: User) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(PASSWORD_KEY, user.password)
        editor.putString(USER_ID_KEY, user._id)
        editor.putString(USER_NAME_KEY, user.userName)
        editor.putString(USER_EMAIL_KEY, user.email)
        editor.putString(USER_IMAGE_KEY, user.imageRes)
        editor.putString(USER_TOKEN_KEY, user.token)
        editor.putString(USER_NBLOGIN_KEY, user.loginCount)
        editor.putString(USER_TIMEPass_KEY, user.totalTimeSpent)
        editor.putString(USER_SCORE_KEY, user.score.toString())




        editor.apply()

        // Add logs to check values after saving
        Log.d(
            "UserProfileFragment",
            "Saved UserID: ${user._id}, UserName: ${user.userName}, UserEmail: ${user.email}, UserImageRes: ${user.imageRes}, UserToken: ${user.token}"
        )
    }
}
