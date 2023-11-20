package tn.esprit.green_world.activities

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.green_world.R

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

        homeLayout.setOnClickListener {
            // Add your code here to handle the click event
            if (selectedTab != 1) {
                favorittxt.visibility = View.GONE
                cameratxt.visibility = View.GONE
                shoptxt.visibility = View.GONE
                favoritImage.setImageResource(R.drawable.like_selected_icon)
           cameraImage.setImageResource(R.drawable.camera_selected)
                shopImage.setImageResource(R.drawable.shop_selected)
                favoritLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                homeLayout.setBackgroundColor(R.drawable.round_back_home_100)
                homeImage.setBackgroundColor(R.drawable.home)
                hometxt.visibility=View.VISIBLE
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
        favoritLayout.setOnClickListener {
            // Add your code here to handle the click event
            if (selectedTab != 2) {
                hometxt.visibility = View.GONE
                cameratxt.visibility = View.GONE
                shoptxt.visibility = View.GONE
                homeImage.setImageResource(R.drawable.home_selected)
                cameraImage.setImageResource(R.drawable.camera_selected)
                shopImage.setImageResource(R.drawable.shop_selected)
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoritLayout.setBackgroundColor(R.drawable.round_back_home_100)
                favoritImage.setBackgroundColor(R.drawable.like_icon)
                favorittxt.visibility=View.VISIBLE
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                favoritLayout.startAnimation(anim)
                selectedTab = 1


            }

        }
        cameraLayout.setOnClickListener {
            // Add your code here to handle the click event
            if (selectedTab != 3) {
                hometxt.visibility = View.GONE
                favorittxt.visibility = View.GONE
                shoptxt.visibility = View.GONE
                homeImage.setImageResource(R.drawable.home_selected)
                favoritImage.setImageResource(R.drawable.like_selected_icon)
                shopImage.setImageResource(R.drawable.shop_selected)
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoritLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(R.drawable.round_back_home_100)
                cameraImage.setBackgroundColor(R.drawable.camera)
                favorittxt.visibility=View.VISIBLE
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                favoritLayout.startAnimation(anim)
                selectedTab = 1


            }

        }


        shopLayout.setOnClickListener {
            // Add your code here to handle the click event
            if (selectedTab != 4) {
                hometxt.visibility = View.GONE
                favorittxt.visibility = View.GONE
                cameratxt.visibility = View.GONE
                homeImage.setImageResource(R.drawable.home_selected)
                favoritImage.setImageResource(R.drawable.like_selected_icon)
                cameraImage.setImageResource(R.drawable.camera_selected)
                homeLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                cameraLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                favoritLayout.setBackgroundColor(resources.getColor(android.R.color.transparent))
                shopLayout.setBackgroundColor(R.drawable.round_back_home_100)
                shopImage.setBackgroundColor(R.drawable.shop)
                favorittxt.visibility=View.VISIBLE
                val anim = ScaleAnimation(
                    0.8f, 1.0f, 1f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                )
                anim.duration = 200
                anim.fillAfter = true
                favoritLayout.startAnimation(anim)
                selectedTab = 1


            }

        }
    }

}