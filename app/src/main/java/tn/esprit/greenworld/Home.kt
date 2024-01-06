package tn.esprit.greenworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
<<<<<<< Updated upstream
import tn.esprit.green_world.fragments.HomeFragment
=======
>>>>>>> Stashed changes
import tn.esprit.greenworld.adapters.Adapter
import tn.esprit.greenworld.fragments.EventFragment
import tn.esprit.greenworld.fragments.FavProduitFragment
import tn.esprit.greenworld.fragments.ProduitFragment
import tn.esprit.greenworld.fragments.typedechets

class Home : AppCompatActivity() {
    private lateinit var pageMain: ViewPager2
    private lateinit var bottomnav: BottomNavigationView
    private val fragmentArrayList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pageMain = findViewById(R.id.pagerMain)
        bottomnav = findViewById(R.id.bottomNav)

        fragmentArrayList.add(HomeFragment())
        fragmentArrayList.add(FavProduitFragment())
        fragmentArrayList.add(EventFragment())

        fragmentArrayList.add(typedechets())
        fragmentArrayList.add(ProduitFragment())


        val adapterViewPager = Adapter(this, fragmentArrayList)
        pageMain.adapter = adapterViewPager

        pageMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomnav.setSelectedItemId(R.id.homeFragment)
                    1 -> bottomnav.setSelectedItemId(R.id.favoriteFragment)
                    2 -> bottomnav.setSelectedItemId(R.id.EventFragment)
                    3 -> bottomnav.setSelectedItemId(R.id.typeFragment)
                    4 -> bottomnav.setSelectedItemId(R.id.produitFragment)
                }
                super.onPageSelected(position)
            }
        })

        bottomnav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> pageMain.setCurrentItem(0, true)
                R.id.favoriteFragment -> pageMain.setCurrentItem(1, true)
                R.id.EventFragment -> pageMain.setCurrentItem(2, true)
                R.id.typeFragment -> pageMain.setCurrentItem(3, true)
                R.id.produitFragment -> pageMain.setCurrentItem(4, true)
            }
            true
        }
    }
}