package com.eu.fe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.eu.fe.databinding.ActivityFoodDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_food_details.*


class FoodDetailsActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding : ActivityFoodDetailsBinding
    lateinit var btn_buy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_food_details)

        food_name.text = getIntent().getStringExtra("FOODNAME")
        food_description.text = getIntent().getStringExtra("FOODDESC")
        getIntent().getStringExtra("FOODLOGO")?.let { image_food.setImageResource(it.toInt()) }
        btn_buy=findViewById(R.id.btn_buy)

        btn_buy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        savedata()
        val I = Intent(this@FoodDetailsActivity,pm::class.java)
        startActivity(I)
        finish()
    }




    private fun savedata(){
        val foodname = food_name.text.toString().trim()
        val fooddescription = food_description.text.toString().trim()
        val foodlogo = image_food.drawable.toString().trim()


        val sfdb = FirebaseDatabase.getInstance().getReference("Selected Food")
        val fdId = sfdb.push().key

        val sf= fdId?.let { savedfood(foodlogo,foodname,fooddescription,fdId) }

        if (fdId != null) {
            sfdb.child(fdId).setValue(sf).addOnCompleteListener{
                Toast.makeText(applicationContext, "Added to cart", Toast.LENGTH_SHORT).show()

            var getdata = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var sb = StringBuilder()
                        for (i in snapshot.children){
                            var foodlogo1 = i.child("foodlogo").getValue()
                            var foodname1 = i.child("foodname").getValue()
                            var fooddesciption1 = i.child("fooddescription").getValue()

                            sb.append("${i.key} $foodlogo1 $foodname1 $fooddesciption1")
                        }
                        textView2.setText(sb)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                }

                sfdb.addValueEventListener(getdata)
            }
        }
    }


}