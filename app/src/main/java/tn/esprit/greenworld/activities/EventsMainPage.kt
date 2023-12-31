package tn.esprit.greenworld.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import tn.esprit.greenworld.activities.Constante.PREF_NAME
import tn.esprit.greenworld.activities.Constante.USER_ID_KEY
import tn.esprit.greenworld.adapters.CommentActionListener
import tn.esprit.greenworld.adapters.CommentAdapter
import tn.esprit.greenworld.databinding.ActivityEventsMainPageBinding
import tn.esprit.greenworld.fragments.CommentBottomSheet
import tn.esprit.greenworld.models.Comment
import tn.esprit.greenworld.repositories.CommentRepository
import tn.esprit.greenworld.viewModel.EventDetailsViewModel
import kotlin.properties.Delegates

class EventsMainPage : AppCompatActivity(), CommentActionListener {
    private lateinit var userId: String
    val accessToken = "YOUR_ACCESS_TOKEN" // Replace with your actual Facebook access token
    val pageId = "161985713672651" // Replace with your actual Facebook page ID
    val message = "Check out this post!" // Replace with your actual message

    private lateinit var binding: ActivityEventsMainPageBinding
    private lateinit var eventId: String
    private lateinit var eventTitle: String
    private lateinit var eventImage: String
    private lateinit var eventDescription: String
    private lateinit var eventLocation: String
    private var eventnbParticipant by Delegates.notNull<Int>()
    private lateinit var eventMvvm: EventDetailsViewModel
    private var callbackManager: CallbackManager? = null
    private lateinit var recyclerView: RecyclerView
    private val commentRepository = CommentRepository()
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var sharedPreferences: SharedPreferences // Déplacer ici

    private fun getUserIdFromPreferences(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    private val userID by lazy { getUserIdFromPreferences() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        eventId = sharedPreferences.getString("event_id", "") ?: ""
        userId = sharedPreferences.getString(USER_ID_KEY, "") ?: ""

        // Log the retrieved user ID for debugging
        Log.d("UserId", "User ID: $userId")

        binding = ActivityEventsMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerViewCom
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
                Toast.makeText(
                    this,
                    "Aucune application de navigation n'est installée sur cet appareil",
                    Toast.LENGTH_SHORT
                ).show()
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

        binding.participer.setOnClickListener {
            val intent = Intent(this, ReservationActivity::class.java)
            intent.putExtra("event_id", eventId)
            intent.putExtra("event_titre", eventTitle)
            intent.putExtra("event_image", eventImage)
            intent.putExtra("event_description", eventDescription)
            intent.putExtra("event_location", eventLocation)
            intent.putExtra("event_nbParticpant", eventnbParticipant.toString())
            val userId = getUserIdFromPreferences()
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }

        commentAdapter = CommentAdapter(mutableListOf(), this, userID)
        recyclerView.adapter = commentAdapter

        commentRepository.getCommentsByEvent(eventId) { comments ->
            if (comments != null) {
                Log.d("Comments", "Number of comments: ${comments.size}")
                commentAdapter.updateComments(comments)
            } else {
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
            } ?: run {
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
        val imageUrl = "http://10.0.2.2:9090/img/$eventImage"
        Log.d("Glide", "Image URL: $imageUrl")
        Glide.with(applicationContext)
            .load(imageUrl)
            .into(binding.imageEvent)
        binding.titreEvent.text = eventTitle
    }

    private fun fetchEventDetails() {
        if (eventId.isNotEmpty()) {
            eventMvvm.getEventDetail(eventId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FacebookSdk.getCallbackRequestCodeOffset()) {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDeleteComment(commentId: String) {
        // Utilisez une boîte de dialogue pour demander confirmation
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer ce commentaire ?")

        alertDialogBuilder.setPositiveButton("Oui") { _, _ ->
            // Code à exécuter si l'utilisateur clique sur "Oui"
            commentRepository.deleteComment(commentId) { success ->
                if (success) {
                    Log.e("Comments", "Success to delete comments")
                    commentRepository.getCommentsByEvent(eventId) { updatedComments ->
                        if (updatedComments != null) {
                            commentAdapter.updateComments(updatedComments)
                        }
                    }
                } else {
                    Log.e("Comments", "Failed to delete comments")
                }
            }
        }

        alertDialogBuilder.setNegativeButton("Non") { dialog, _ ->
            // Code à exécuter si l'utilisateur clique sur "Non"
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onUpdateComment(commentId: String) {
        // Obtenez le commentaire à partir de votre CommentAdapter
        val commentToUpdate = commentAdapter.getCommentById(commentId)

        // Utilisez une boîte de dialogue pour demander la nouvelle valeur du commentaire
        val editText = EditText(this)
        editText.setText(commentToUpdate?.Contenu) // Pré-remplir le contenu actuel du commentaire
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Mise à jour du commentaire")
        alertDialogBuilder.setView(editText)

        alertDialogBuilder.setPositiveButton("Mettre à jour") { _, _ ->
            // Code à exécuter si l'utilisateur clique sur "Mettre à jour"
            val updatedContent = editText.text.toString()
            commentToUpdate?.let {
                // Mettre à jour le contenu du commentaire
                it.Contenu = updatedContent

                // Appeler la fonction d'updateComment de votre CommentRepository
                commentRepository.updateComment(commentId, it) { success ->
                    if (success) {
                        Log.e("Comments", "Success to update comment")
                        commentRepository.getCommentsByEvent(eventId) { updatedComments ->
                            if (updatedComments != null) {
                                commentAdapter.updateComments(updatedComments)
                            }
                        }
                    } else {
                        Log.e("Comments", "Failed to update comment")
                    }
                }
            }
        }

        alertDialogBuilder.setNegativeButton("Annuler") { dialog, _ ->
            // Code à exécuter si l'utilisateur clique sur "Annuler"
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


}
