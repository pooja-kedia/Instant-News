package com.myapplication.newspaper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.headlines.MainActivity

class NewspaperRecyclerAdapter(
    private val listNewspaper: ArrayList<NewspaperModel>,
    private val context: Context

): RecyclerView.Adapter<NewspaperRecyclerAdapter.NewspaperHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewspaperHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_newspaper_list, parent, false)
        return NewspaperHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return listNewspaper.size
    }

    inner class NewspaperHolder(view: View):RecyclerView.ViewHolder(view){
        val newspaper : TextView = view.findViewById(R.id.times_of_india)
        val viewName : ConstraintLayout= view.findViewById(R.id.view_name)

    }
    override fun onBindViewHolder(holder: NewspaperHolder, position: Int) {
        val item = listNewspaper[position]
        holder.newspaper.text = item.newspaper
        holder.viewName.setOnClickListener {
            //Toast.makeText(context,item.id,Toast.LENGTH_SHORT).show()
            val intent = Intent(context,MainActivity::class.java)
            //ek Activity se dusri Activity me Data send krne ke liye
            intent.putExtra("clickedId",item.id)
            context.startActivity(intent)

        }
    }

}