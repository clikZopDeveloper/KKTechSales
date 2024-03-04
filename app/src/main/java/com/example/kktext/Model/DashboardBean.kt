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
        val callScheduled: String, // 56
        @SerializedName("cancelled")
        val cancelled: String, // 0
        @SerializedName("completed")
        val completed: String, // 2
        @SerializedName("converted_leads")
        val convertedLeads: String, // 4
        @SerializedName("executive_count")
        val executiveCount: Int, // 0
        @SerializedName("followups")
        val followups: List<Followup>,
        @SerializedName("interested_leads")
        val interestedLeads: String, // 0
        @SerializedName("lost_leads")
        val lostLeads: String, // 0
        @SerializedName("monthly_sale")
        val monthlySale: Double, // 83340.75
        @SerializedName("new_leads")
        val newLeads: String, // 2
        @SerializedName("Partial")
        val partial: String, // 1
        @SerializedName("pending_leads")
        val pendingLeads: String, // 0
        @SerializedName("processing_leads")
        val processingLeads: String, // 6
        @SerializedName("sm_newLeads")
        val smNewLeads: String, // 0
        @SerializedName("total_leads")
        val totalLeads: Int, // 213
        @SerializedName("visit_done")
        val visitDone: String, // 52
        @SerializedName("visit_scheduled")
        val visitScheduled: String, // 83
        @SerializedName("yearly_sale")
        val yearlySale: Int // 83341
    ) {
        data class Followup(
            @SerializedName("id")
            val id: Int, // 2764
            @SerializedName("name")
            val name: String, // MR. Rommy Bansal Advocate
            @SerializedName("number")
            val number: String, // 8896510001
            @SerializedName("remind_date")
            val remindDate: String, // 2024-2-29
            @SerializedName("remind_time")
            val remindTime: String,
            @SerializedName("status")
            val status: String // VISIT SCHEDULED
        )
    }
}