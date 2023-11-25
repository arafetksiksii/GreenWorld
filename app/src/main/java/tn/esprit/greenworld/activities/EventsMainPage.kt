package tn.esprit.greenworld.activities

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.share.ShareApi
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import tn.esprit.greenworld.activities.ReservationActivity
import tn.esprit.greenworld.adapters.CommentAdapter
import tn.esprit.greenworld.databinding.ActivityEventsMainPageBinding
import tn.esprit.greenworld.fragments.CommentBottomSheet
import tn.esprit.greenworld.repositories.CommentRepository
import tn.esprit.greenworld.viewModel.EventDetailsViewModel
import kotlin.properties.Delegates


class EventsMainPage : AppCompatActivity() {
    private lateinit var binding: ActivityEventsMainPageBinding
    private lateinit var eventId: String
    private lateinit var eventTitle: String
    private lateinit var eventImage: String
    private lateinit var eventLocation: String
    private lateinit var eventDescription: String
    private var eventnbParticipant by Delegates.notNull<Int>()
    private lateinit var eventMvvm: EventDetailsViewModel
    private var callbackManager: CallbackManager? = null
    val userID = "6557fea6fb5c6a93876129f1"
    val commentRepository = CommentRepository()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerViewCom // Replace 'recyclerView' with the actual ID of your RecyclerView in the layout
        recyclerView.layoutManager = LinearLayoutManager(this)

        window.statusBarColor = Color.GRAY
        eventMvvm = EventDetailsViewModel()
        getEventInformationFromIntent()
        setInformationInViews()
        fetchEventDetails()
        observerEventDetailLiveData()
        binding.btnEvent.setOnClickListener {
            onBackPressed()
        }
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        binding.facebook.setOnClickListener {
            val message: String = binding.titreEvent.text.toString()
            val imageUri = Uri.parse(eventImage)
            val facebookShareUrl = "https://www.facebook.com/sharer/sharer.php?u=$imageUri&quote=$message"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookShareUrl))
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Aucune application de navigation n'est installée sur cet appareil", Toast.LENGTH_SHORT).show()
            }
        }


        binding.commenter.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("event_id", eventId)
            editor.putString("user_id", userID)
            editor.apply()

            val bottomSheetFragment = CommentBottomSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }



        binding.participer.setOnClickListener { event ->
            val intent = Intent(this, ReservationActivity::class.java)
            intent.putExtra("event_id", eventId)
            intent.putExtra("event_titre", eventTitle)
            intent.putExtra("event_image", eventImage)
            intent.putExtra("event_description", eventDescription)
            intent.putExtra("event_location", eventLocation)
            intent.putExtra("event_nbParticpant", eventnbParticipant.toString())
            val userId = "654f70fb5cb452f9138dbf46"
            intent.putExtra("user_id", userId)

            startActivity(intent)
        }
        commentRepository.getCommentsByEvent(eventId) { comments ->
            if (comments != null) {
                // Log the number of comments
                Log.d("Comments", "Number of comments: ${comments.size}")

                // Log comments
                for (comment in comments) {
                    Log.d("Comments", "Comment ID: ${comment._id}, Content: ${comment.Contenu}")


                    val commentAdapter = CommentAdapter(comments)
                    recyclerView.adapter = commentAdapter
                    commentAdapter.notifyDataSetChanged()
                }

            } else {
                // Handle the case where fetching comments failed
                Log.e("Comments", "Failed to fetch comments")
            }
        }
    }

    private fun observerEventDetailLiveData() {
        eventMvvm.obersverEventDetailLiveData().observe(this, Observer { event ->
            event?.let {
                Log.d("EventActivity", "event Data Received: ${event.titre}")
                binding.titreEvent.text = "Name of Event: ${event.titre.toString()}"
                binding.description.text = "Description: ${event.description}"
                binding.location.text = "Location: ${event.lieu}"
                binding.nbParticipant.text = "nombre de participants : ${event.nbparticipant}"

                Log.d("EventActivity", "Received eventDescription: $eventDescription")
                Log.d("EventActivity", "Received eventLocation: $eventLocation")
            } ?: run {
                // Handle the case when the API response is null
                Toast.makeText(this, "Failed to retrieve event details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getEventInformationFromIntent() {
        val intent = intent
        eventId = intent.getStringExtra("event_id") ?: ""
        eventTitle = intent.getStringExtra("event_titre") ?: ""
        eventImage = intent.getStringExtra("event_image") ?: ""
        eventDescription = intent.getStringExtra("event_description") ?: ""
        eventLocation = intent.getStringExtra("event_location") ?: ""
        eventnbParticipant = intent.getStringExtra("event_nbParticpant")?.toIntOrNull() ?: 0

        Log.d("EventActivity", "Received eventId: $eventId")
        Log.d("EventActivity", "Received eventTitle: $eventTitle")
        Log.d("EventActivity", "Received eventImage: $eventImage")
        Log.d("EventActivity", "Received eventDescription: $eventDescription")
        Log.d("EventActivity", "Received eventLocation: $eventLocation")
        Log.d("EventActivity", "Received eventParticipant: $eventnbParticipant")
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(eventImage)
            .into(binding.imageEvent)
        binding.titreEvent.text = eventTitle
    }

    private fun fetchEventDetails() {
        if (eventId.isNotEmpty()) {
            eventMvvm.getEventDetail(eventId)
        }
    }

    /*fun partagerContenuSurFacebook() {
        shareLinkContent()
    }
*/
    /*  private fun shareLinkContent() {
          val shareDialog = ShareDialog(this)
          val shareLinkContent: ShareLinkContent = ShareLinkContent.Builder()
              .setContentUrl(Uri.parse("https://www.facebook.com/profile.php?id=61553646785355&is_tour_dismissed=true"))
              .build()
          shareDialog.show(shareLinkContent)
      }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Assurez-vous que le requestCode correspond au code de demande de callbackManager
        if (requestCode == FacebookSdk.getCallbackRequestCodeOffset()) {
            // Continuez avec le code de gestion du résultat si nécessaire
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }
}


