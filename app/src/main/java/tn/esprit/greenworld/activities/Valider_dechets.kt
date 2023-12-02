package tn.esprit.greenworld.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import tn.esprit.greenworld.databinding.ActivityValiderDechetsBinding

class valider_dechets : AppCompatActivity() {
    lateinit var binding : ActivityValiderDechetsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityValiderDechetsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Récupérer le ByteArray de l'intent
        val byteArray = intent.getByteArrayExtra("qrcode")

        // Convertir le ByteArray en Bitmap
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        // Afficher le Bitmap dans une ImageView
        val qrCodeImageView: ImageView = binding.qrImageView
        qrCodeImageView.setImageBitmap(bitmap)
        val type_de=binding.titype
        val date_de=binding.tidate
        val nombre=binding.tiweight
        val adresse =binding.tiadresse


        val  type=getIntent().getStringExtra("Type_dechets")

        val date =getIntent().getStringExtra("date_depot")
        val weight=getIntent().getStringExtra("nombre_capacite")
        val adre=getIntent().getStringExtra("adresse")

        type_de.setText(type)
        date_de.setText(date)
        nombre.setText(weight)
        adresse.setText(adre)



    }
}

