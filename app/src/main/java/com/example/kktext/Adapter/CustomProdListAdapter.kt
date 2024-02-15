package com.example.kktext.Adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kktext.Model.LeadDetailBean
import com.example.kktext.R
import com.example.kktext.Utills.GeneralUtilities
import com.example.kktext.Utills.RvListClickListner
import com.example.kktext.Utills.RvStatusClickListner
import com.google.gson.Gson
import com.stpl.antimatter.Utils.ApiContants
import java.io.OutputStream


class CustomProdListAdapter(
    var context: Activity,
    var list: List<LeadDetailBean.Data.QuoteProduct>,
    var rvClickListner: RvListClickListner
) : RecyclerView.Adapter<CustomProdListAdapter.MyViewHolder>() {
    private val data = mutableListOf<Int>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder { // infalte the item Layout
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productcustlist, parent, false)
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

        holder.tvProductName.text = list[position].name.toString()
        holder.tvQty.text = list[position].qty.toString()

        holder.tvDate.text = list[position].createdAt.toString()
        holder.tvDelivered.text = list[position].isDelivered.toString()

        if (list[position].isDelivered == 1) {
            holder.ivCheck.isChecked = true
            holder.ivCheck.isEnabled = false
            data.add(0)
            rvClickListner.clickPos(data, list[position].id)
            Log.d("zxczxc", Gson().toJson(data))
        } else {
            holder.ivCheck.isChecked = false
            holder.ivCheck.isEnabled = true
        }
        holder.ivCheck.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                data.add(list[position].id)
            } else {
                data.remove(list[position].id)
            }

            Log.d("zxczxc", Gson().toJson(data))
            rvClickListner.clickPos(data, list[position].id)
        }
        // holder.ivImage.setImageDrawable(context.resources.getDrawable(list[position].drawableId))

        /*  if ("Retailer"=="Retailer"){
        //      holder.itemView.visibility=View.GONE
          }*/


        /* holder.itemView.setOnClickListener {
               rvClickListner.clickPos(data,list[position].id)
         }*/
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tvProductName: TextView = itemview.findViewById(R.id.tvProductName)
        val tvQty: TextView = itemview.findViewById(R.id.tvQty)
        val tvDate: TextView = itemview.findViewById(R.id.tvDate)
        val tvDelivered: TextView = itemview.findViewById(R.id.tvDelivered)
        val ivCheck: CheckBox = itemview.findViewById(R.id.ivCheck)

    }

}