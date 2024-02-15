package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class ExecutiveDashboardBean(
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
        val booked: String, // null
        @SerializedName("call_scheduled")
        val callScheduled: String, // null
        @SerializedName("cancelled")
        val cancelled: String, // null
        @SerializedName("completed")
        val completed: String, // null
        @SerializedName("converted_leads")
        val convertedLeads: String, // null
        @SerializedName("interested_leads")
        val interestedLeads: String, // null
        @SerializedName("monthly_sale")
        val monthlySale: String, // 0
        @SerializedName("new_leads")
        val newLeads: String, // null
        @SerializedName("pending_leads")
        val pendingLeads: String, // null
        @SerializedName("processing_leads")
        val processingLeads: String, // null
        @SerializedName("sm_newLeads")
        val smNewLeads: String, // null
        @SerializedName("total_leads")
        val totalLeads: String, // 0
        @SerializedName("visit_done")
        val visitDone: String, // null
        @SerializedName("visit_scheduled")
        val visitScheduled: String, // null
        @SerializedName("yearly_sale")
        val yearlySale: String // 0
    )
}