package tn.esprit.greenworld

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.activities.Constants.PREF_NAME
import tn.esprit.greenworld.activities.Constants.USER_ID_KEY
import tn.esprit.greenworld.models.User
import tn.esprit.greenworld.utils.RetrofitImp
import tn.esprit.greenworld.utils.UserInterface

class pointdechets : AppCompatActivity() {
    private lateinit var pointsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pointdechets)
        pointsTextView = findViewById(R.id.pointsTextView)

        // Fetch and display user points
        fetchUserPoints()
    }
    private fun getUserIdFromPreferences(): String? {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID_KEY, null)
    }
    private fun fetchUserPoints() {
        val userId = getUserIdFromPreferences()
        if (userId != null) {
            val userService = RetrofitImp.buildRetrofit().create(UserInterface::class.java)
            val call: Call<User> = userService.getUserById(userId)

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user: User? = response.body()
                        user?.let {
                            // Display user points
                            displayUserPoints(it.points)
                        }
                    } else {
                        // Handle unsuccessful response
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }

    private fun displayUserPoints(userPoints: Int) {
        // Set the user points in the TextView
        pointsTextView.text = userPoints.toString()
    }
}