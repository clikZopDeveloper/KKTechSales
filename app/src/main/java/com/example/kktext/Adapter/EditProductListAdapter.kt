package com.example.kktext.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kktext.Model.LeadProductListBean
import com.example.kktext.R
import com.example.kktext.Utills.RvStatusClickListner


class EditProductListAdapter(var context: Activity,  var list: List<LeadProductListBean.Data>, var rvClickListner: RvStatusClickListner) : RecyclerView.Adapter<EditProductListAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_prod_list, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)

   /*     holder.tvAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
        holder.tvQtyAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
        holder.tvQtyMinus.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
        holder.tvQty.background = RoundView(Color.TRANSPARENT, RoundView.getRadius(20f), true, R.color.orange)
        holder.tvOff.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
      */


        holder.tvCatName.text="Category Name : "+ list[position].productName
        holder.tvQty.text= "Qty : "+list[position].qty.toString()

       // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

      /*  if ("Retailer"=="Retailer"){
      //      holder.itemView.visibility=View.GONE
        }*/

        holder.ivDelete.setOnClickListener {
            rvClickListner.clickPos("",list[position].id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvCatName: TextView = itemview.findViewById(R.id.tvCatName)
       val tvQty: TextView = itemview.findViewById(R.id.tvQty)
       val ivImage: ImageView = itemview.findViewById(R.id.ivImage)
       val ivDelete: ImageView = itemview.findViewById(R.id.ivDelete)
    }

}