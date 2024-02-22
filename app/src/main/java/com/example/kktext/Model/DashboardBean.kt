package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class DashboardBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String, // Data found
    @SerializedName("0")
    val x0: String // leads
) {
    data class Data(
        @SerializedName("booked")
        val booked: String, // 0
        @SerializedName("call_scheduled")
        val callScheduled: String, // 0
        @SerializedName("cancelled")
        val cancelled: String, // 0
        @SerializedName("completed")
        val completed: String, // 4
        @SerializedName("converted_leads")
        val convertedLeads: String, // 0
        @SerializedName("executive_count")
        val executiveCount: Int, // 0
        @SerializedName("followups")
        val followups: List<Followup>,
        @SerializedName("interested_leads")
        val interestedLeads: String, // 0
        @SerializedName("lost_leads")
        val lostLeads: String, // 0
        @SerializedName("monthly_sale")
        val monthlySale: Int, // 1326725
        @SerializedName("new_leads")
        val newLeads: String, // 3
        @SerializedName("Partial")
        val partial: String, // 0
        @SerializedName("pending_leads")
        val pendingLeads: String, // 0
        @SerializedName("processing_leads")
        val processingLeads: String, // 0
        @SerializedName("sm_newLeads")
        val smNewLeads: String, // 0
        @SerializedName("total_leads")
        val totalLeads: Int, // 8
        @SerializedName("visit_done")
        val visitDone: String, // 1
        @SerializedName("visit_scheduled")
        val visitScheduled: String, // 1
        @SerializedName("yearly_sale")
        val yearlySale: Int // 0
    ) {
        data class Followup(
            @SerializedName("id")
            val id: Int, // 2595
            @SerializedName("name")
            val name: String, // adeshManocha clikzop
            @SerializedName("number")
            val number: String, // 8145581455
            @SerializedName("status")
            val status: String ,// VISIT SCHEDULED
             @SerializedName("remind_date")
            val remind_date: String ,// VISIT SCHEDULED
             @SerializedName("remind_time")
            val remind_time: String ,// VISIT SCHEDULED
        )
    }
}