package com.example.kktext.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kktext.Model.AllRequestedQuoteBean
import com.example.kktext.Model.DashboardBean
import com.example.kktext.R
import com.example.kktext.Utills.RvStatusClickListner


class NotificationAdapter(var context: Activity, var list: List<DashboardBean.Data.Followup>,var rvClickListner: RvStatusClickListner) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setIsRecyclable(false)

   /*     holder.tvAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
        holder.tvQtyAdd.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
        holder.tvQtyMinus.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(100f))
        holder.tvQty.background = RoundView(Color.TRANSPARENT, RoundView.getRadius(20f), true, R.color.orange)
        holder.tvOff.background = RoundView(context.resources.getColor(R.color.orange), RoundView.getRadius(20f))
        holder.tvAdd.visibility = View.VISIBLE*/

        holder.tvID.text= list[position].id.toString()
        holder.tvName.text= list[position].name
      //  holder.tvPlumber.text= "Plumber : "+list[position].plumber
        holder.tvNumber.text=list[position].number.toString()
        holder.tvStatus.text= list[position].status.toString()
        holder.tvRemainedTime.text= list[position].remind_time.toString()
        holder.tvRemainedDate.text= list[position].remind_date.toString()



       // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

      /*  if ("Retailer"=="Retailer"){
      //      holder.itemView.visibility=View.GONE
        }*/

        holder.itemView.setOnClickListener {
            rvClickListner.clickPos(list[position].status,list[position].id)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvID: TextView = itemview.findViewById(R.id.tvID)
       val tvName: TextView = itemview.findViewById(R.id.tvName)
       val tvNumber: TextView = itemview.findViewById(R.id.tvNumber)
       val tvStatus: TextView = itemview.findViewById(R.id.tvStatus)
       val tvRemainedDate: TextView = itemview.findViewById(R.id.tvRemainedDate)
       val tvRemainedTime: TextView = itemview.findViewById(R.id.tvRemainedTime)

    }

}