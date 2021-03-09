package com.eu.fe;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*


class CustomAdapter(var items: ArrayList<Food>, var clickListner: OnFoodItemClickListner) :
    RecyclerView.Adapter<CustomViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        lateinit var customViewHolder: CustomViewHolder
        customViewHolder = CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
        return customViewHolder

    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.initialize(items.get(position), clickListner)
    }
}


class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    var foodName = itemView.foodname
    var foodDescription = itemView.fooddescription
    var foodLogo = itemView.foodlogo


    fun initialize(item: Food, action: OnFoodItemClickListner) {
        foodName.text = item.name
        foodDescription.text = item.description
        foodLogo.setImageResource(item.logo)

        itemView.setOnClickListener {
            action.onItemClick(item, adapterPosition)
        }

    }
}


interface OnFoodItemClickListner {
    fun onItemClick(item: Food, position: Int)

}








