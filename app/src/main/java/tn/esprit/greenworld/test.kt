package tn.esprit.greenworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation_view)

        toolbar.setNavigationOnClickListener {

            drawerLayout.openDrawer(GravityCompat.START)

        }

        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            drawerLayout.closeDrawer(GravityCompat.START)
            when (id) {
                R.id.nav_home -> Toast.makeText(
                    this@test,
                    "Home is Clicked",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.Store -> Toast.makeText(
                    this@test,
                    "Message is Clicked",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.dechets -> Toast.makeText(
                    this@test,
                    "Synch is Clicked",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.Event -> Toast.makeText(
                    this@test,
                    "Trash is Clicked",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.settings -> Toast.makeText(
                    this@test,
                    "Settings is Clicked",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_login -> Toast.makeText(
                    this@test,
                    "Login is Clicked",
                    Toast.LENGTH_SHORT
                ).show()



                else -> return@OnNavigationItemSelectedListener true
            }
            true
        })
    }
}