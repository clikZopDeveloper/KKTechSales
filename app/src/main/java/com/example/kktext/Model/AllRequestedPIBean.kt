package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class AllRequestedPIBean(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("address")
        val address: Any, // null
        @SerializedName("allocated_date")
        val allocatedDate: Any, // null
        @SerializedName("app_city")
        val appCity: Any, // null
        @SerializedName("app_contact")
        val appContact: Any, // null
        @SerializedName("app_doa")
        val appDoa: Any, // null
        @SerializedName("app_dob")
        val appDob: Any, // null
        @SerializedName("app_name")
        val appName: Any, // null
        @SerializedName("architect")
        val architect: String, // Chandigarh
        @SerializedName("budget")
        val budget: Any, // null
        @SerializedName("campaign")
        val campaign: Any, // null
        @SerializedName("catg_id")
        val catgId: Int, // 4
        @SerializedName("city")
        val city: String, // South East Delhi
        @SerializedName("classification")
        val classification: Any, // null
        @SerializedName("client_id")
        val clientId: Int, // 1826
        @SerializedName("client_name")
        val clientName: String, // Mr.Deepanshu
        @SerializedName("conversion_type")
        val conversionType: Any, // null
        @SerializedName("email")
        val email: Any, // null
        @SerializedName("field3")
        val field3: Any, // null
        @SerializedName("field4")
        val field4: Any, // null
        @SerializedName("final_price")
        val finalPrice: Any, // null
        @SerializedName("gst")
        val gst: String, // null
        @SerializedName("id")
        val id: Int, // 290
        @SerializedName("is_allocated")
        val isAllocated: Int, // 0
        @SerializedName("is_interested_allocated")
        val isInterestedAllocated: Int, // 0
        @SerializedName("last_comment")
        val lastComment: String, // wrjw
        @SerializedName("lead_date")
        val leadDate: String, // 2024-01-3115:36:56
        @SerializedName("lead_id")
        val leadId: Int, // 2200
        @SerializedName("name")
        val name: Any, // null
        @SerializedName("notes")
        val notes: Any, // null
        @SerializedName("number")
        val number: String, // 9560116768
        @SerializedName("phone")
        val phone: Any, // null
        @SerializedName("plumber")
        val plumber: Any, // null
        @SerializedName("project_id")
        val projectId: Any, // null
        @SerializedName("projects")
        val projects: Any, // null
        @SerializedName("property_stage")
        val propertyStage: String, // BeforeLenter
        @SerializedName("quotes_status")
        val quotesStatus: Int, // 0
        @SerializedName("remind_date")
        val remindDate: Any, // null
        @SerializedName("remind_time")
        val remindTime: Any, // null
        @SerializedName("size")
        val size: Any, // null
        @SerializedName("source")
        val source: String, // Architect
        @SerializedName("state")
        val state: String, // AndamanNicobar
        @SerializedName("status")
        val status: String, // NEWLEAD
        @SerializedName("sub_catg_id")
        val subCatgId: Int, // 9
        @SerializedName("type")
        val type: String, // Commercial
        @SerializedName("updated_date")
        val updatedDate: String, // 2024-02-0616:08:18
        @SerializedName("user_id")
        val userId: Int, // 58
        @SerializedName("user_name")
        val userName: String, // ArvindJoshi
        @SerializedName("user_type")
        val userType: String, // Sales manager
        @SerializedName("whatsapp_no")
        val whatsappNo: Any // null
    )
}