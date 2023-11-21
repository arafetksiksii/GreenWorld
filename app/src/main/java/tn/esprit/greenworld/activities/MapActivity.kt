package tn.esprit.greenworld.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import tn.esprit.greenworld.R
import tn.esprit.greenworld.API.EventApi
import tn.esprit.greenworld.models.Event
import tn.esprit.greenworld.models.EventList
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.greenworld.utils.RetrofitImp

class MapActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private var mapController: IMapController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Check for and request location permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                // Permission is already granted, proceed with location operations
                setupMap()
            }
        } else {
            // For devices below Android M, no runtime permission check is needed
            setupMap()
        }
    }

    private fun setupMap() {
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)

        mapController = map.controller

        // Centrez la carte sur la Tunisie
        val tunisiaCenter = GeoPoint(33.8869, 9.5375)
        mapController?.setZoom(7.0)  // Ajustez le niveau de zoom selon vos besoins
        mapController?.setCenter(tunisiaCenter)

        // Récupérez une instance de l'interface EventApi à partir du singleton RetrofitImp
        val eventApi = RetrofitImp.buildRetrofit().create(EventApi::class.java)

        // Faites l'appel réseau pour récupérer la liste des événements
        val call: Call<EventList> = eventApi.getEvent()
        call.enqueue(object : Callback<EventList> {
            override fun onResponse(call: Call<EventList>, response: Response<EventList>) {
                if (response.isSuccessful) {
                    val events: List<Event> = response.body() ?: emptyList()

                    // Vérifiez si la liste d'événements n'est pas nulle et n'est pas vide
                    if (events.isNotEmpty()) {
                        for (event in events) {
                            // Créez un marqueur pour chaque événement
                            val eventLocation = GeoPoint(event.latitude, event.longitude)
                            val eventMarker =
                                OverlayItem(event.lieu, "Description de l'événement", eventLocation)

                            // Ajoutez le marqueur à la carte
                            val eventOverlay = ItemizedIconOverlay(
                                applicationContext,
                                listOf(eventMarker),
                                null
                            )
                            map.overlays.add(eventOverlay)
                        }

                        // Rafraîchir la carte pour afficher les nouveaux marqueurs
                        map.invalidate()

                        // Ajouter les marqueurs avec titres
                        addEventMarkers(events)
                    }
                } else {
                    // Gérer la réponse d'erreur
                }
            }

            override fun onFailure(call: Call<EventList>, t: Throwable) {
                // Gérer l'échec de l'appel réseau
            }
        })
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with location operations
                    setupMap()
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                    // You might want to inform the user why the permission is necessary
                    setupMap()  // Initialize map even if permission is denied
                }
            }
        }
    }

    private fun addEventMarkers(events: List<Event>) {
        for (event in events) {
            // Créer un marqueur pour chaque événement
            val eventLocation = GeoPoint(event.latitude, event.longitude)
            val eventMarker = Marker(map)

            // Concaténer le titre (nom de l'événement) et le lieu pour le snippet
            val snippetText = "à :${event.lieu ?: "Lieu non spécifié"}"

            eventMarker.position = eventLocation
            eventMarker.title = event.titre
            eventMarker.snippet = snippetText

            // Ajouter le marqueur à la carte
            map.overlays.add(eventMarker)
        }
    }


    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 1
    }
}
