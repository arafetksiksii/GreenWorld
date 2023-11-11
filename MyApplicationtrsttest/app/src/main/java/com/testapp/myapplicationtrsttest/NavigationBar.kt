package com.testapp.myapplicationtrsttest

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.testapp.myapplicationtrsttest.R

class NavigationBar : AppCompatActivity() {


    var selectedTab: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar);

        val  homeLayout = findViewById<LinearLayout>(R.id.home_id)
        val  cameraLayout = findViewById<LinearLayout>(R.id.camera_id)
        val  favoritLayout = findViewById<LinearLayout>(R.id.favorite_id)
        val  shopLayout = findViewById<LinearLayout>(R.id.shop_id)

        val homeImage =  findViewById<ImageView>(R.id.homeimage)
        val cameraImage =  findViewById<ImageView>(R.id.cameraimage)
        val favoritImage =  findViewById<ImageView>(R.id.favoriteimage)
        val shopImage =  findViewById<ImageView>(R.id.shopimage)
        val hometxt =findViewById<TextView>(R.id.hometext)
        val cameratxt =findViewById<TextView>(R.id.cameratext)
        val favorittxt =findViewById<TextView>(R.id.favoritetext)
        val shoptxt =findViewById<TextView>(R.id.shoptext)
        favorittxt.visibility = View.GONE
        cameratxt.visibility = View.GONE
        shoptxt.visibility = View.GONE
        homeLayout.setOnClickListener {
            // Ajoutez votre code ici pour gérer l'événement de clic
            if (selectedTab != 1) {
                // Cacher les textes des autres onglets
                favorittxt.visibility = View.GONE
                cameratxt.visibility = View.GONE
                shoptxt.visibility = View.GONE
                // Réinitialiser les images des autres onglets
                favoritImage.setImageResource(R.drawable.like_selected_icon)
                cameraImage.setImageResource(R.drawable.camera_selected)
                shopImage.setImageResource(R.drawable.shop_selected)

                // Réinitialiser les arrière-plans des autres onglets
                favoritLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                // Mettre en surbrillance l'onglet "Home"
                homeLayout.setBackgroundResource(R.drawable.round_back_home_100)
                homeImage.setImageResource(R.drawable.home)
                hometxt.visibility = View.VISIBLE

                // Appliquer une animation de mise à l'échelle à l'onglet "Home"
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                homeLayout.startAnimation(anim)

                selectedTab = 1
            }
        }

        cameraLayout.setOnClickListener {
            // Ajoutez votre code ici pour gérer l'événement de clic
            if (selectedTab != 2) {
                // Cacher les textes des autres onglets
                hometxt.visibility = View.GONE
                favorittxt.visibility = View.GONE
                shoptxt.visibility = View.GONE

                // Réinitialiser les images des autres onglets
                homeImage.setImageResource(R.drawable.home_selected)
                favoritImage.setImageResource(R.drawable.like_selected_icon)
                shopImage.setImageResource(R.drawable.shop_selected)

                // Réinitialiser les arrière-plans des autres onglets
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoritLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                // Mettre en surbrillance l'onglet "Favorites"
                cameraLayout.setBackgroundResource(R.drawable.round_back_home_100)
                cameraImage.setImageResource(R.drawable.camera)
                cameratxt.visibility = View.VISIBLE

                // Appliquer une animation de mise à l'échelle à l'onglet "Favorites"
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                cameraLayout.startAnimation(anim)

                selectedTab = 2
            }
        }

        favoritLayout.setOnClickListener {
            // Ajoutez votre code ici pour gérer l'événement de clic
            if (selectedTab != 3) {
                // Cacher les textes des autres onglets
                hometxt.visibility = View.GONE
                cameratxt.visibility = View.GONE
                shoptxt.visibility = View.GONE

                // Réinitialiser les images des autres onglets
                homeImage.setImageResource(R.drawable.home_selected)
                cameraImage.setImageResource(R.drawable.camera_selected)
                shopImage.setImageResource(R.drawable.shop_selected)

                // Réinitialiser les arrière-plans des autres onglets
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                // Mettre en surbrillance l'onglet "Camera"
                favoritLayout.setBackgroundResource(R.drawable.round_back_home_100)
                favoritImage.setImageResource(R.drawable.like_icon)
                favorittxt.visibility = View.VISIBLE

                // Appliquer une animation de mise à l'échelle à l'onglet "Camera"
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 0.8f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                favoritLayout.startAnimation(anim)

                selectedTab = 3
            }
        }



        shopLayout.setOnClickListener {
            // Ajoutez votre code ici pour gérer l'événement de clic
            if (selectedTab != 4) {
                // Cacher les textes des autres onglets
                hometxt.visibility = View.GONE
                cameratxt.visibility = View.GONE
                favorittxt.visibility = View.GONE

                // Réinitialiser les images des autres onglets
                homeImage.setImageResource(R.drawable.home_selected)
                cameraImage.setImageResource(R.drawable.camera_selected)
                favoritImage.setImageResource(R.drawable.like_selected_icon)

                // Réinitialiser les arrière-plans des autres onglets
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoritLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))

                // Mettre en surbrillance l'onglet "Shop"
                shopLayout.setBackgroundResource(R.drawable.round_back_home_100)
                shopImage.setImageResource(R.drawable.shop)
                shoptxt.visibility = View.VISIBLE

                // Appliquer une animation de mise à l'échelle à l'onglet "Shop"
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 0.8f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                shopLayout.startAnimation(anim)

                selectedTab = 4
            }
        }

    }

}