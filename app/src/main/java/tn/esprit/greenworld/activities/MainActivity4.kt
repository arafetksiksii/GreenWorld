package tn.esprit.greenworld.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import tn.esprit.greenworld.R
import tn.esprit.greenworld.databinding.ActivityMain4Binding
<<<<<<< Updated upstream
import tn.esprit.greenworld.databinding.ActivityMainBinding
import tn.esprit.greenworld.fragments.EventFragment
=======
import tn.esprit.greenworld.fragments.typedechets
>>>>>>> Stashed changes

class MainActivity4 : AppCompatActivity() {
    private lateinit var binding: ActivityMain4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btntype.setOnClickListener {
            // replaceFragment(typedechets())
        }

        binding.btndechets.setOnClickListener {
            replaceFragment(EventFragment())
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
}