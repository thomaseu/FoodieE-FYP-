package com.eu.fe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_pm.*
import org.json.JSONObject


class pm : AppCompatActivity(),PaymentResultListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pm)

        paymethod()
    }

    private fun paymethod() {
        cButton.setOnClickListener{

            Toast.makeText(this, "Welcome to Stripe", Toast.LENGTH_SHORT).show()
            //val i =Intent(this@pm, CheckoutActivity::class.java )
            val i =Intent(this@pm, stripepayment::class.java )
            startActivity(i)
            finish()

        }

        eButton.setOnClickListener {
            Toast.makeText(this, "Welcome to RazorPay", Toast.LENGTH_SHORT).show()
            startrazorpay()


        }
    }

    private fun startrazorpay() {
        var checkout =Checkout()
        try {
            val options = JSONObject()
            options.put("name", "FoodieE")
            options.put("description", "Reference No. #123456")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", "100")
            options.put("prefill.email", "thomaseu@example.com")
            options.put("prefill.contact", "01127094877")
            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Payment Failed", Toast.LENGTH_LONG).show()
        }



    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(applicationContext, "Payment Success", Toast.LENGTH_LONG).show()
        val i = Intent( this@pm, UserActivity::class.java)
        startActivity(i)
        finish()

    }

    override fun onPaymentError(p0: Int, p1: String?) {

    }
}