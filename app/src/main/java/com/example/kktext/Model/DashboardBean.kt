package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class DashboardBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String, // Datafound
    @SerializedName("0")
    val x0: String // leads
) {
    data class Data(
        @SerializedName("booked")
        val booked: Int, // 1
        @SerializedName("call_scheduled")
        val callScheduled: String, // 1
        @SerializedName("cancelled")
        val cancelled: Int, // 1
        @SerializedName("completed")
        val completed: Int, // 1
        @SerializedName("converted_leads")
        val convertedLeads: String, // 2
        @SerializedName("executive_count")
        val executiveCount: Int, // 0
        @SerializedName("followups")
        val followups: List<Any>,
        @SerializedName("interested_leads")
        val interestedLeads: String, // 0
        @SerializedName("lost_leads")
        val lostLeads: String, // 0
        @SerializedName("monthly_sale")
        val monthlySale: Double, // 19052.65
        @SerializedName("new_leads")
        val newLeads: String, // 10
        @SerializedName("Partial")
        val partial: Int, // 1
        @SerializedName("pending_leads")
        val pendingLeads: String, // 0
        @SerializedName("processing_leads")
        val processingLeads: String, // 0
        @SerializedName("sm_newLeads")
        val smNewLeads: String, // 0
        @SerializedName("total_leads")
        val totalLeads: Int, // 13
        @SerializedName("visit_done")
        val visitDone: String, // 0
        @SerializedName("visit_scheduled")
        val visitScheduled: String, // 0
        @SerializedName("yearly_sale")
        val yearlySale: Int // 0
    )
}