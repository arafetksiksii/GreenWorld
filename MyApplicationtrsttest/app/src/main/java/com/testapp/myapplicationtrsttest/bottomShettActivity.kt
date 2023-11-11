package com.testapp.myapplicationtrsttest

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior

class bottomShettActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_shett)

        Log.d("BottomSheetActivity", "Avant l'initialisation de BottomSheetBehavior")
        val sheet = findViewById<LinearLayout>(R.id.sheet)
        Log.d("BottomSheetActivity", "Après l'initialisation de BottomSheetBehavior")

        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 200
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}
