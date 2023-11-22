package com.example.testtt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.testtt.databinding.ActivityMainBinding
import com.example.testtt.fragments.EventFragment
import com.example.testtt.fragments.dechets
import com.example.testtt.fragments.typedechets

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btntype.setOnClickListener {
            replaceFragment(typedechets())
        }

        binding.btndechets.setOnClickListener {
            replaceFragment(dechets())
        }
    }
}



