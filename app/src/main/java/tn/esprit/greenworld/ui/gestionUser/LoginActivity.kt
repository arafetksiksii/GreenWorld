package tn.esprit.greenworld.ui.gestionUser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.greenworld.databinding.ActivityUserLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val rootView = window.decorView.rootView

        // Initialisation des TextWatchers, si nécessaire
        // initTextWatchers()

        // Gestion du clic sur le bouton "S'inscrire"
        binding.btnLogin.setOnClickListener {
            // Validation du formulaire (décommentez si nécessaire)
            // if (validateForm()) {
            // Si la validation est réussie, naviguez vers l'activité d'inscription (User_Register).
            startActivity(Intent(this, User_Register::class.java))
            // } else {
            // Si la validation échoue, affichez un Snackbar avec un message d'erreur.
            // Snackbar.make(rootView, getString(R.string.msg_error_inputs), Snackbar.LENGTH_SHORT).show()
            // }
        }
    }

    // Méthode de validation du formulaire, si nécessaire
    // private fun validateForm(): Boolean {
    //    // Ajoutez votre logique de validation ici
    //    // Retournez true si la validation réussit, sinon false.
    // }
}
