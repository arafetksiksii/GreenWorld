package tn.esprit.greenworld.activities
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import tn.esprit.greenworld.R

class CustomDialog(context: Context) : AlertDialog(context, R.style.RoundedDialog) {
    private lateinit var cancelButton: ImageButton
    private lateinit var okButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customdialog)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        cancelButton = findViewById(R.id.cancelID)
        okButton = findViewById(R.id.ok_btn_id)

        cancelButton.setOnClickListener {
            dismiss()
        }

        okButton.setOnClickListener {
            dismiss()
            Toast.makeText(context, "Thanks", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, EventsMainPage::class.java)

            context.startActivity(intent)

        }
    }
}
