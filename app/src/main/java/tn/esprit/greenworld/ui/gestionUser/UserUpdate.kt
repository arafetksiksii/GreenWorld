package tn.esprit.greenworld

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.databinding.ActivityUserUpdateBinding
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.models.User5
import tn.esprit.greenworld.ui.gestionUser.LoginActivity
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class UserUpdate : AppCompatActivity() {
    var config: HashMap<String, String> = HashMap()
    private lateinit var binding: ActivityUserUpdateBinding
    private lateinit var selectedImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PICK_IMAGE_REQUEST = 1
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

        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            launchImageSelection()
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
                    this@UserUpdate,
                    "Image uploaded successfully. URL: $url",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Toast.makeText(
                    this@UserUpdate,
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
        startActivity(Intent(this@UserUpdate, MIDrawerActivity::class.java))
        finish()
    }

    private fun handleUpdateFailure(errorMessage: String) {
        showSnackbar(errorMessage)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
