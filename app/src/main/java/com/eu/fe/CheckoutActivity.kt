package com.eu.fe

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.stripe.android.view.CardInputWidget
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import java.lang.ref.WeakReference

class CheckoutActivity : AppCompatActivity() {

    private val backendUrl = "http://10.0.2.2:4242/"
    private val httpClient = OkHttpClient()
    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)


        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = Stripe(applicationContext, "pk_test_51IMUQNCnvgNH86OU8XvRQQu5ceF62M1H4R2aQEyD5itQP2sh2Lj1i9ARnXMT65Ry9dRFOQdqqBQs53fKt5vXcTV100hEEKEy01")
        startCheckout()
    }

    private fun startCheckout() {
        val weakActivity = WeakReference<Activity>(this)
        // Create a PaymentIntent by calling your server's endpoint.
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestJson = """
      {
          "currency":"myr",
          "items": [
              {"id":"xl-tshirt"}
          ]
      }
      """
        val body = requestJson.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(backendUrl + "create-payment-intent")
            .post(body)
            .build()
        httpClient.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    weakActivity.get()?.let { activity ->
                        displayAlert(activity, "Failed to load page", "Error: $e")
                    }
                }
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        weakActivity.get()?.let { activity ->
                            displayAlert(
                                activity,
                                "Failed to load page",
                                "Error: $response"
                            )
                        }
                    } else {
                        val responseData = response.body?.string()
                        val responseJson =
                            responseData?.let { JSONObject(it) } ?: JSONObject()
                        // For added security, our sample app gets the publishable key
                        // from the server.
                        paymentIntentClientSecret = responseJson.getString("clientSecret")
                    }
                }
            })

        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener {
            val cardInputWidget =
                findViewById<CardInputWidget>(R.id.cardInputWidget)
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }

    private fun displayAlert(activity: Activity, title: String, message: String, restartDemo: Boolean = false) {
        runOnUiThread {
            val builder = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
            builder.setPositiveButton("Ok", null)
            builder.create().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this)
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    weakActivity.get()?.let { activity ->
                        displayAlert(
                            activity,
                            "Payment succeeded",
                            gson.toJson(paymentIntent)
                        )
                    }
                } else if (status == StripeIntent.Status.RequiresPaymentMethod) {
                    weakActivity.get()?.let { activity ->
                        displayAlert(
                            activity,
                            "Payment failed",
                            paymentIntent.lastPaymentError?.message.orEmpty()
                        )
                    }
                }
            }
            override fun onError(e: Exception) {
                weakActivity.get()?.let { activity ->
                    displayAlert(
                        activity,
                        "Payment failed",
                        e.toString()
                    )
                }
            }
        })
    }
}


