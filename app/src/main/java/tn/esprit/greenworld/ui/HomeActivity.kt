package tn.esprit.greenworld.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import tn.esprit.greenworld.R
import tn.esprit.greenworld.ui.fragments.HomeFragment
import tn.esprit.greenworld.ui.fragments.ProduitFragment


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fl_title = findViewById<TextView>(R.id.fl_title)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val homeFragment = HomeFragment()
                    setCurrentFragment(homeFragment)
                    fl_title.text = "Home"
                    true
                }
                R.id.profile -> {
                    val magasinFragment = ProduitFragment()
                    setCurrentFragment(magasinFragment)
                    fl_title.text = "Magasin"
                    true
                }
                // Add cases for other menu items if needed
                else -> false
            }
        }


    }
    private fun setCurrentFragment(fragment : Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flHome, fragment)
            commit()
        }
}
