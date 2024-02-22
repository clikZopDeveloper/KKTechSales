package com.example.kktext.Model


import com.google.gson.annotations.SerializedName

data class LeadDetailBean(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean, // false
    @SerializedName("msg")
    val msg: String
) {
    data class Data(
        @SerializedName("lead_comments")
        val leadComments: List<LeadComment>,
        @SerializedName("lead_data")
        val leadData: LeadData,
        @SerializedName("lead_products")
        val leadProducts: List<LeadProduct>,
        @SerializedName("quote_product")
        val quoteProduct: List<QuoteProduct>
    ) {
        data class LeadComment(
            @SerializedName("comment")
            val comment: String,
            @SerializedName("created_date")
            val createdDate: String, // 2023-12-04 14:40:14
            @SerializedName("id")
            val id: Int, // 470
            @SerializedName("lead_id")
            val leadId: Int, // 499
            @SerializedName("remind_date")
            val remindDate: String,
            @SerializedName("remind_time")
            val remindTime: String,
            @SerializedName("status")
            val status: String, // VISIT DONE
            @SerializedName("user_id")
            val userId: Int // 44
        )

        data class LeadData(
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
            val architect: String, // Ar. Unknown/Harsimran
            @SerializedName("budget")
            val budget: Any, // null
            @SerializedName("campaign")
            val campaign: Any, // null
            @SerializedName("category")
            val category: String, // Villa
             @SerializedName("mep")
            val mep: String, // update test mep
            @SerializedName("catg_id")
            val catgId: Int, // 1
            @SerializedName("city")
            val city: String, // Ambala
            @SerializedName("classification")
            val classification: Any, // null
            @SerializedName("client_address")
            val clientAddress: String, // E11/6 Vatika Ambala City
            @SerializedName("client_city")
            val clientCity: String, // Ambala
            @SerializedName("client_id")
            val clientId: Int, // 200
            @SerializedName("client_name")
            val clientName: String, // Mrs.Anu Khosla
            @SerializedName("client_number")
            val clientNumber: String, // 9810872764
            @SerializedName("conversion_type")
            val conversionType: Any, // null
            @SerializedName("email")
            val email: String, // null
            @SerializedName("field3")
            val field3: Any, // null
            @SerializedName("field4")
            val field4: Any, // null
            @SerializedName("final_price")
            val finalPrice: Any, // null
            @SerializedName("gst")
            val gst: String, // null
            @SerializedName("id")
            val id: Int, // 499
            @SerializedName("is_allocated")
            val isAllocated: Int, // 0
            @SerializedName("is_interested_allocated")
            val isInterestedAllocated: Int, // 0
            @SerializedName("last_comment")
            val lastComment: String,
            @SerializedName("lead_date")
            val leadDate: String, // 2023-12-04 14:29:29
            @SerializedName("name")
            val name: String, // null
            @SerializedName("notes")
            val notes: Any, // null
            @SerializedName("phone")
            val phone: Any, // null
            @SerializedName("plumber")
            val plumber: String, // Mr. Ram Sewak/Ambala
            @SerializedName("project_id")
            val projectId: Any, // null
            @SerializedName("projects")
            val projects: Any, // null
            @SerializedName("property_stage")
            val propertyStage: String, // During Flooring Before Gate
            @SerializedName("quotes_status")
            val quotesStatus: Int, // 0
            @SerializedName("remind_date")
            val remindDate: String, // 2024-01-25
            @SerializedName("remind_time")
            val remindTime: String,
            @SerializedName("size")
            val size: Any, // null
            @SerializedName("source")
            val source: String, // Cold Call
            @SerializedName("state")
            val state: String, // Haryana
            @SerializedName("status")
            val status: String, // CALL SCHEDULED
            @SerializedName("sub_category")
            val subCategory: String, // 500 Sq yard
            @SerializedName("sub_catg_id")
            val subCatgId: Int, // 2
            @SerializedName("type")
            val type: String, // Residential
            @SerializedName("updated_date")
            val updatedDate: String, // 2024-01-17 17:06:09
            @SerializedName("user_id")
            val userId: Int, // 44
            @SerializedName("whatsapp_no")
            val whatsappNo: String // null
        )

        data class LeadProduct(
            @SerializedName("created_at")
            val createdAt: String, // 2023-12-06 18:21:52
            @SerializedName("id")
            val id: Int, // 830
            @SerializedName("is_delivered")
            val isDelivered: Int, // 0
            @SerializedName("lead_id")
            val leadId: Int, // 499
            @SerializedName("product_id")
            val productId: Int, // 25
            @SerializedName("product_name")
            val productName: String, // ILIOS AUTOMATION PANEL-HOT WATER RECIRCULATION
            @SerializedName("product_price")
            val productPrice: String, // 8611.00
            @SerializedName("qty")
            val qty: String, // 1.00
            @SerializedName("updated_at")
            val updatedAt: Any // null
        )

        data class QuoteProduct(
            @SerializedName("created_at")
            val createdAt: String, // 2023-12-04 14:51:24
            @SerializedName("id")
            val id: Int, // 142
            @SerializedName("is_delivered")
            val isDelivered: Int, // 0
            @SerializedName("name")
            val name: String, // BRADFORD WHITE ELECTRICAL WATER HEATER - M-II-80R6DS
            @SerializedName("qty")
            val qty: String, // 1.00
            @SerializedName("quote_id")
            val quoteId: Int // 25
        )
    }
}