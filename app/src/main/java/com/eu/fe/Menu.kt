package com.eu.fe

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eu.fe.databinding.ActivityMenu2Binding
import kotlinx.android.synthetic.main.activity_menu2.*


class Menu : AppCompatActivity(), OnFoodItemClickListner {

    lateinit var binding: ActivityMenu2Binding
    lateinit var foodlist: ArrayList<Food>

    var isOpen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView(this, R.layout.activity_menu2)

        val fabOpen = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_open)
        val fabClose = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_close)
        val fabRClockwise = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise)
        val fabRAntiClockwise = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.rotate_anticlockwise)

        popoutbutton.setOnClickListener {
            if (isOpen) {
                favouritebutton.startAnimation(fabClose)
                popoutbutton.startAnimation(fabRClockwise)

                isOpen = true
            } else {
                favouritebutton.startAnimation(fabOpen)
                popoutbutton.startAnimation(fabRAntiClockwise)

                isOpen = false
            }

            favouritebutton.setOnClickListener {
                Toast.makeText(this, "Added to favourite", Toast.LENGTH_SHORT).show()
            }
        }


        foodlist = ArrayList()
        addFood()



        foodRecycler.layoutManager = LinearLayoutManager(this)
        foodRecycler.addItemDecoration(DividerItemDecoration(this, 1))
        foodRecycler.adapter = CustomAdapter(foodlist, this)


    }


    fun addFood() {
        foodlist.add(
                Food(
                        "Spaghetti Bolognese",
                        "Spaghetti with an Italian ragù (meat sauce) made with minced beef, bacon and tomatoes, served with Parmesan cheese",
                        R.drawable.a
                )
        )
        foodlist.add(
                Food(
                        "Fruit Rojak",
                        "Mixed vegetables, fruits, and dough fritters that is covered in a sticky black sauce and garnished with chopped peanuts and finely-cut fragrant ginger flowers for a piquant taste. The mark of a good rojak is its sauce, made up of fermented prawn paste, sugar, lime and chilli paste",
                        R.drawable.b
                )
        )
        foodlist.add(
                Food(
                        "Sandwich",
                        "consisting of vegetables, sliced cheese or meat, placed on or between slices of bread, or more generally any dish wherein bread serves as a container or wrapper",
                        R.drawable.c
                )
        )
        foodlist.add(
                Food(
                        "Hawaiian Pizza",
                        "pizza sauce, cheese, cooked ham, and pineapple. This crowd-pleasing pizza recipe starts with my homemade pizza crust and is finished with a sprinkle of crispy bacon",
                        R.drawable.d
                )
        )
        foodlist.add(
                Food(
                        "Pancake",
                        " Flat cake, often thin and round, prepared from a starch-based batter that may contain eggs, milk and butter and cooked on a hot surface",
                        R.drawable.e
                )
        )
        foodlist.add(
                Food(
                        "Yogurt Ice Cream",
                        "Sweetened frozen food typically eaten as a snack or dessert. It may be made from dairy milk or cream and is flavoured with a sweetener, either sugar or an alternative, and any spice, such as cocoa or vanilla.",
                        R.drawable.f
                )
        )

    }


    override fun onItemClick(item: Food, position: Int) {
        Toast.makeText(this, item.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, FoodDetailsActivity::class.java)
        intent.putExtra("FOODNAME", item.name)
        intent.putExtra("FOODDESC", item.description)
        intent.putExtra("FOODLOGO", item.logo.toString())
        startActivity(intent)


    }

}






