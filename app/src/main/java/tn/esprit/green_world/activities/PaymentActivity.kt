    package tn.esprit.green_world.activities

    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
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
    import tn.esprit.green_world.utils.RetrofitInstance

    class PaymentActivity : AppCompatActivity(), CoroutineScope by MainScope() {

        private lateinit var paymentIntentClientSecret: String
        private lateinit var paymentLauncher: PaymentLauncher
        private lateinit var cardInputWidget: CardInputWidget

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_payment)

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

            // Add additional log to check if this function is being called
            Log.d("PaymentActivity", "onPaymentResult called")
        }


        override fun onDestroy() {
            super.onDestroy()
            cancel() // Cancel the coroutine scope when the activity is destroyed
        }
    }
