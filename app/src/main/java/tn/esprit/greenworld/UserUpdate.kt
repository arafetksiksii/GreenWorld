package tn.esprit.greenworld

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
    private lateinit var sharedPreferences: SharedPreferences // Déplacer ici


    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


// Initialiser sharedPreferences ici
        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)




        // Check if MediaManager is already initialized

            // Initialize Cloudinary with configuration
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
            // Check if an image is selected before attempting to upload
            if (selectedImageUri != null) {
                uploadImageToCloudinary(selectedImageUri!!)

            } else {
                Toast.makeText(this, "Select an image first", Toast.LENGTH_SHORT).show()
            }
            updateUserp()
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
            // The image has been successfully selected
            selectedImageUri = data?.data

            // Display the selected image in the ImageView
            selectedImageView.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToCloudinary(imageUri: Uri) {

        // Use MediaManager configuration
        MediaManager.get().upload(imageUri).callback(object : UploadCallback {
            override fun onStart(requestId: String?) {
                // Handle the start of the upload
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                // Handle the progress of the upload
            }

            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                // Handle the success of the upload
                val publicId = resultData?.get("public_id")
                val url = MediaManager.get().url().generate(publicId.toString())

                Toast.makeText(
                    this@UserUpdate,
                    "Image uploaded successfully. URL: $url",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                // Handle errors during the upload
                Toast.makeText(
                    this@UserUpdate,
                    "Error uploading the image: ${error?.description}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                // Handle the rescheduling of the upload
            }
        }).dispatch()
    }
    private fun updateUserp() {
        // Get user data from SharedPreferences
       // val sharedPreferences: SharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "")
        val imageUrl=sharedPreferences.getString("imageRes","")
        Log.d("hhhhhhhhhhh","jjjjjjjjjjjjjjjj")
        val user5 = User5(
            id = userId?.toString() ?: "", // Assurez-vous que userId n'est pas null
            email = binding.tiEmail.text.toString(),
            nom = binding.tiEmail.text.toString(), // Utilisez le champ correct pour le nom
            prenom = binding.tiEmail.text.toString(), // Utilisez le champ correct pour le prénom
            userName = binding.tiFullName.text.toString(), // Assurez-vous que tiFullName est le champ correct
            adress = binding.tiAdress.text.toString(),
            cin = binding.tiCin.text.toString(),
            imageRes = imageUrl?.toString() ?: "" // Assurez-vous que imageUrl n'est pas null
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
                // Handle the failure here
                // You can log the error or show a message to the user
                handleUpdateFailure(t.message ?: "Unknown error")
            }
        })
    }
    private fun handleSuccessfulupdate(user: User?) {
        // Handle successful registration
        // Optionally, you can navigate to the login screen
        startActivity(Intent(this@UserUpdate, LoginActivity::class.java))
        finish()
    }

    private fun handleUpdateFailure(errorMessage: String) {
        // Handle registration failure
        showSnackbar(errorMessage)
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
