package com.example.kktext.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kktext.Activity.EditLeadActivity
import com.example.kktext.Activity.UpdateLeadActivity
import com.example.kktext.Model.AllLeadDataBean
import com.example.kktext.Model.AllRequestedQuoteBean
import com.example.kktext.R
import com.example.kktext.Utills.RvStatusClickListner


class AllRequestQuoteAdapter(var context: Activity, var list: List<AllRequestedQuoteBean.Data>, var leadStatus:String?, var rvClickListner: RvStatusClickListner) : RecyclerView.Adapter<AllRequestQuoteAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_allleaddata, parent, false)
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
        holder.tvSource.text= list[position].source
      //  holder.tvPlumber.text= "Plumber : "+list[position].plumber
        holder.tvArchitect.text= list[position].architect
        holder.tvClient.text= list[position].clientName
        holder.tvLeadDate.text= list[position].leadDate
        holder.ivUpdate.setOnClickListener {
          context.startActivity(Intent(context,UpdateLeadActivity::class.java)
               .putExtra("leadID",list[position].id.toString())
               .putExtra("leadStatus",leadStatus.toString()))
        }
        holder.ivEditLead.setOnClickListener {
            context.startActivityForResult(Intent(context, EditLeadActivity::class.java)
                .putExtra("leadID",list[position].leadId.toString())
                .putExtra("leadStatus",leadStatus.toString())
                .putExtra("source", list[position].source)
                .putExtra("architect",list[position].architect)
                .putExtra("client",list[position].clientName)
                .putExtra("date",list[position].leadDate)
                .putExtra("propertyStage",list[position].propertyStage)
                .putExtra("gst",list[position].gst)
                .putExtra("state",list[position].state)
                .putExtra("city",list[position].city)
                .putExtra("way","AllRequestQuote")
            ,101)
        }
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
       val tvSource: TextView = itemview.findViewById(R.id.tvSource)
       val tvPlumber: TextView = itemview.findViewById(R.id.tvPlumber)
       val tvArchitect: TextView = itemview.findViewById(R.id.tvArchitect)
       val ivEditLead: TextView = itemview.findViewById(R.id.ivEditLead)
       val tvClient: TextView = itemview.findViewById(R.id.tvClient)
       val tvLeadDate: TextView = itemview.findViewById(R.id.tvLeadDate)
       val ivUpdate: ImageView = itemview.findViewById(R.id.ivUpdate)
    }

}