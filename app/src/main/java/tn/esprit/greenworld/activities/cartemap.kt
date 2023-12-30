package tn.esprit.greenworld.activities

import retrofit2.Response
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import tn.esprit.greenworld.API.pointApi
import tn.esprit.greenworld.R
import tn.esprit.greenworld.models.pointCollecte

class cartemap : FragmentActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var apiService: pointApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

       // apiService = RetrofitClient.getRetrofitInstance().create(ApiService::class.java)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



        override fun onMapReady(map: GoogleMap) {
            googleMap = map

            // Déplacez la caméra vers une position par défaut (par exemple, le centre de Paris)
            val defaultLocation = LatLng(48.8566, 2.3522)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

            // Récupérez les points de collecte depuis votre API et ajoutez des marqueurs sur la carte
            val getAllPointsCall: Call<List<pointCollecte>> = apiService.getAllPoints()
            getAllPointsCall.enqueue(object : Callback<List<pointCollecte>> {
                override fun onResponse(call: Call<List<pointCollecte>>, response: Response<List<pointCollecte>>) {
                    if (response.isSuccessful) {
                        val points: List<pointCollecte>? = response.body()

                        points?.forEach { point ->
                            val pointLocation = LatLng(point.latitude, point.longitude)
                            val markerOptions = MarkerOptions()
                                .position(pointLocation)
                                .title(point.nom)
                                .snippet("Capacité: ${point.capacite}")

                            val marker = googleMap.addMarker(markerOptions)
                            // Vous pouvez ajouter un gestionnaire de clics sur le marqueur si nécessaire
                            // marker.tag = point
                        }
                    } else {
                        // Handle error
                    }
                }

                override fun onFailure(call: Call<List<pointCollecte>>, t: Throwable) {
                    // Handle failure
                }
            })
        }


}