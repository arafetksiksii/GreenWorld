package tn.esprit.green_world.activities

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.view.CardInputWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import tn.esprit.green_world.R
import tn.esprit.green_world.interfaces.CommandeApi
import tn.esprit.green_world.models.Commande
import tn.esprit.green_world.models.Produit
import tn.esprit.green_world.utils.RetrofitInstance
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PaymentActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    }

    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher
    private lateinit var cardInputWidget: CardInputWidget
    private lateinit var alertDialog: AlertDialog
    private val CREATE_FILE_REQUEST_CODE = 1
    private var isReceiptGenerated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Storage Permission Required")
                    .setMessage("This app needs access to the storage to download the receipt.")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }
                    .setCancelable(false)
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        } else {
            // Permission has already been granted
            // ...
        }

        cardInputWidget = findViewById(R.id.cardInputWidget)
        val btnSubmitPayment: Button = findViewById(R.id.btnSubmitPayment)

        val tvPaymentDetails: TextView = findViewById(R.id.tvPaymentDetails)

        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.create(
            this,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )

        // Start the checkout process immediately
        startCheckout()

        btnSubmitPayment.setOnClickListener {
            if (::paymentIntentClientSecret.isInitialized) {
                confirmPayment()
            } else {
                Log.d("PaymentActivity", "Failed to confirm payment")
            }
        }
    }

    private fun startCheckout() {
        // Call the createPaymentIntent endpoint without any parameters
        RetrofitInstance.Commandeapi.createPaymentIntent()
            .enqueue(object : retrofit2.Callback<CommandeApi.PaymentIntentResponse> {
                override fun onResponse(
                    call: retrofit2.Call<CommandeApi.PaymentIntentResponse>,
                    response: retrofit2.Response<CommandeApi.PaymentIntentResponse>
                ) {
                    if (response.isSuccessful) {
                        paymentIntentClientSecret = response.body()?.clientSecret.orEmpty()
                    } else {
                        Log.d("PaymentActivity", "Failed to create payment intent")
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<CommandeApi.PaymentIntentResponse>,
                    t: Throwable
                ) {
                    Log.e("PaymentActivity", "Error creating payment intent", t)
                }
            })
    }

    private fun confirmPayment() {
        cardInputWidget.paymentMethodCreateParams?.let { params ->
            val confirmParams =
                ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                    params,
                    paymentIntentClientSecret
                )
            launch {
                paymentLauncher.confirm(confirmParams)
            }
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {

        val message = when (paymentResult) {
            is PaymentResult.Completed -> {
                if (!isReceiptGenerated) {
                    downloadReceipt()
                }
                "Completed!"
            }
            is PaymentResult.Canceled -> {
                "Canceled!"
            }
            is PaymentResult.Failed -> {
                // This string comes from the PaymentIntent's error message.
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                "Failed: " + paymentResult.throwable?.message
            }
        }
        Log.d("PaymentActivity", message)
        showPaymentResultDialog(message)
        Log.d("PaymentActivity", "onPaymentResult called")
        // Add additional log to check if this function is being called
        Log.d("PaymentActivity", "onPaymentResult called")
    }

    private fun showPaymentResultDialog(message: String) {
        // Create an AlertDialog.Builder
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setCancelable(false)

        // Add "Return Home" button
        builder.setPositiveButton("Return Home") { _, _ ->
            // Add the logic to return to the main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }



        // Create the AlertDialog
        alertDialog = builder.create()

        // Show the AlertDialog
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel() // Cancel the coroutine scope when the activity is destroyed
    }

    private fun downloadReceipt() {
        // Make a network call to get the cart details
        RetrofitInstance.Commandeapi.getCommande()
            .enqueue(object : retrofit2.Callback<Commande> {
                override fun onResponse(
                    call: retrofit2.Call<Commande>,
                    response: retrofit2.Response<Commande>
                ) {
                    if (response.isSuccessful) {
                        val commande = response.body()
                        commande?.let {
                            // Extract selected products and total price from the Commande model
                            val selectedProducts = it.selectedProducts
                            val totalPrice = it.totalPrice

                            // Generate and download the receipt
                            generateAndDownloadReceipt(selectedProducts, totalPrice)

                            // Set the flag to indicate that the receipt is generated
                            isReceiptGenerated = true
                        }
                    } else {
                        Log.d("PaymentActivity", "Failed to get cart details")
                    }
                }

                override fun onFailure(call: retrofit2.Call<Commande>, t: Throwable) {
                    Log.e("PaymentActivity", "Error getting cart details", t)
                }
            })
    }

    private fun generateAndDownloadReceipt(selectedProducts: List<Produit>, totalPrice: String) {
        // Create a new PDF document
        val document = PdfDocument()

        // Create a page info for a simple page
        val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create()

        // Start a new page
        val page = document.startPage(pageInfo)

        // Draw content on the page
        val canvas = page.canvas
        drawReceiptContent(canvas, selectedProducts, totalPrice)

        // Finish the page
        document.finishPage(page)

        // Create a content resolver
        val resolver = applicationContext.contentResolver

        // Define the metadata for the new file
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "receipt.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS) // Change this to your desired directory
        }

        // Get the URI for the new file
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        // Write the document to an output stream
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                document.writeTo(outputStream)
                Toast.makeText(this, "Receipt downloaded!", Toast.LENGTH_SHORT).show()
                Log.d("PaymentActivity", "Document Page Count: ${document.pages.size}")
                Log.d("PaymentActivity", "File Path: $uri")
                // Now you can implement logic to open the PDF file using an external PDF viewer or share it
                // ...

                // Close the document
                document.close()
            }
        }
    }

    private fun drawReceiptContent(canvas: Canvas, selectedProducts: List<Produit>, totalPrice: String) {
        // Customize the receipt content based on your needs
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 12f

        var yOffset = 40f

        canvas.drawText("Receipt Details:", 10f, yOffset, paint)
        yOffset += 20f

        for (product in selectedProducts) {
            canvas.drawText("product :${product.title} price : ${product.price}TND quantity: ${product.quantity} " , 10f, yOffset, paint)
            yOffset += 20f
        }

        canvas.drawText("Total Price: $totalPrice", 10f, yOffset, paint)
    }
}
