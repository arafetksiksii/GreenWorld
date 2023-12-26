package tn.esprit.greenworld


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.login.LoginFragment

import com.mindinventory.midrawer.MIDrawerView
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_DOOR_IN
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_DOOR_OUT
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_SLIDE
import com.mindinventory.midrawer.MIDrawerView.Companion.MI_TYPE_SLIDE_WITH_CONTENT
import tn.esprit.green_world.fragments.HomeFragment
import tn.esprit.greenworld.activities.MapActivity

import tn.esprit.greenworld.databinding.ActivityMainnavbaraBinding
import tn.esprit.greenworld.databinding.FavproduitFragmentBinding
import tn.esprit.greenworld.fragments.EventFragment
import tn.esprit.greenworld.fragments.ProduitFragment
import tn.esprit.greenworld.fragments.typedechets
import tn.esprit.greenworld.ui.gestionUser.LoginActivity
import tn.esprit.greenworld.ui.gestionUser.UserProfileFragment

import tn.esprit.greenworld.ui.gestionUser.User_ForgetPassword

import tn.esprit.greenworld.ui.gestionUser.User_Register
import tn.esprit.greenworld.ui.quiz_activity.QuizListActivity

open class MIDrawerActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = "MIDrawerActivity"
    private var slideType = 0
    private lateinit var activityMainBinding: ActivityMainnavbaraBinding
    private lateinit var imageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences // Déplacer ici
    // Define a shared preference name
    private val PREF_NAME = "user_pref"
    private val USER_ID_KEY = "userId"
    private val USER_NAME_KEY = "userName"
    private val USER_EMAIL_KEY = "userEmail"
    private val USER_IMAGE_KEY = "userImageRes"
    private val USER_TOKEN_KEY = "tokenLogin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainnavbaraBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        // Set color for the container's content as transparent
        activityMainBinding.drawerLayout.setScrimColor(Color.TRANSPARENT)


        activityMainBinding.navScroll.setOnClickListener(this)
        activityMainBinding.navSlide.setOnClickListener(this)
        activityMainBinding.navDoorIn.setOnClickListener(this)
        activityMainBinding.navDoorOut.setOnClickListener(this)

        setSupportActionBar(activityMainBinding.includeToolbar.toolbar)

        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.home)

        sharedPreferences = this.getSharedPreferences("user_pref", AppCompatActivity.MODE_PRIVATE)
        val userImageRes = sharedPreferences.getString("userImageRes", "")
        // Load user image using Glide
        val circularImageView = findViewById<ImageView>(R.id.circularImageView)
        Glide.with(this)
            .load(userImageRes) // Replace with the actual resource or URL
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_apple)
                    .error(R.drawable.ellipse_background)
                    .circleCrop() // Apply circular cropping
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Caching strategy
            )
            .into(circularImageView)



        activityMainBinding.logout.setOnClickListener {
            logout()
        }

        // Implement the drawer listener
        activityMainBinding.drawerLayout.setMIDrawerListener(object : MIDrawerView.MIDrawerEvents {
            override fun onDrawerOpened(drawerView: View) {

                super.onDrawerOpened(drawerView)
                Log.d(TAG, "Drawer Opened")
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                Log.d(TAG, "Drawer closed")
            }
        })
        //dark mode

        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                // Night mode is active
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                // Night mode is not active, use the default light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                // Use the default light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


    }

    override fun onBackPressed() {
        if (activityMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nav_scroll -> {
                avoidDoubleClicks(activityMainBinding.navScroll)
                slideType = MI_TYPE_SLIDE_WITH_CONTENT
                updateSliderTypeEvents()
            }
            R.id.nav_slide -> {
                avoidDoubleClicks(activityMainBinding.navSlide)
                slideType = MI_TYPE_SLIDE
                updateSliderTypeEvents()
            }

            R.id.nav_doorIn -> {
                avoidDoubleClicks(activityMainBinding.navDoorIn)
                slideType = MI_TYPE_DOOR_IN
                updateSliderTypeEvents()
            }
            R.id.nav_doorOut -> {
                avoidDoubleClicks(activityMainBinding.navDoorIn)
                slideType = MI_TYPE_DOOR_OUT
                updateSliderTypeEvents()
            }

        }
    }

    private fun updateSliderTypeEvents() {
        if (handler == null) {
            handler = Handler()
            activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            handler?.postDelayed(runnable, 500)
        }
    }

    var handler: Handler? = null
    var runnable: Runnable = Runnable {

        when (slideType) {
            MI_TYPE_SLIDE_WITH_CONTENT -> {
                activityMainBinding.includeToolbar.toolbar.title = this@MIDrawerActivity.resources.getString(R.string.scroll)

                replaceFragment(UserProfileFragment())
            }

            MI_TYPE_SLIDE -> {
                activityMainBinding.includeToolbar.toolbar.title = this@MIDrawerActivity.resources.getString(R.string.slide)
                replaceFragment(ProduitFragment())  }
            MI_TYPE_DOOR_IN -> {
                activityMainBinding.includeToolbar.toolbar.title = this@MIDrawerActivity.resources.getString(R.string.doorIn)
                replaceFragment(EventFragment())

            }
            MI_TYPE_DOOR_OUT -> {
                activityMainBinding.includeToolbar.toolbar.title = this@MIDrawerActivity.resources.getString(R.string.doorOut)
                replaceFragment(typedechets())
            }
            MI_TYPE_DOOR_OUT -> {
                activityMainBinding.includeToolbar.toolbar.title = this@MIDrawerActivity.resources.getString(R.string.dechet)
                replaceFragment(typedechets())
            }



        }
        activityMainBinding.drawerLayout.setSliderType(slideType)
        handler = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activityMainBinding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Avoid double click.
     */
    fun avoidDoubleClicks(view: View) {
        val DELAY_IN_MS: Long = 900
        if (!view.isClickable) {
            return
        }
        view.isClickable = false
        view.postDelayed({ view.isClickable = true }, DELAY_IN_MS)
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    // New method for logout
    private fun logout() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Clear all saved user data from shared preferences
        editor.remove(USER_ID_KEY)
        editor.remove(USER_NAME_KEY)
        editor.remove(USER_EMAIL_KEY)
        editor.remove(USER_IMAGE_KEY)
        editor.remove(USER_TOKEN_KEY)

        // Apply changes
        editor.apply()

        // Redirect to the login screen or any other appropriate action

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
