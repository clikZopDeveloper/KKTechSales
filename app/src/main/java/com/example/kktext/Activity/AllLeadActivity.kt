package com.example.kktext.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kktext.Adapter.*
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.Model.AllLeadDataBean
import com.example.kktext.Model.LeadDetailBean
import com.example.kktext.Model.ProductDeleteBean
import com.example.kktext.Model.UpdateLeadBean
import com.example.kktext.R
import com.example.kktext.Utills.*
import com.example.kktext.databinding.ActivityAllLeadBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class AllLeadActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityAllLeadBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    var leadID=0
    var conversionType="Partial"
    var conversion=" "
    private var quoteListID: MutableList<Any?>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_lead)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "All Lead"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.ivLogout.visibility = View.GONE
        binding.igToolbar.switchDayStart.visibility = View.GONE
        binding.igToolbar.tvTitle.text = intent.getStringExtra("leadStatus")
if (intent.hasExtra("conversion")){
   conversion= intent.getStringExtra("conversion")!!
    if (conversion.equals("Partial")){//Partial Converted
        binding.igToolbar.tvTitle.text ="Partial Converted"
    }else if (conversion.equals("Completed")){//Complete Lead
        binding.igToolbar.tvTitle.text = "Complete Lead"
    }else{

    }
}
        intent.getStringExtra("leadStatus")?.let { apiAllLead(it) }


    }

    fun apiAllLead(status: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["status"] = status
        params["conversion"] = conversion
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.AllLeadData, params)
    }


    fun handleAllLead(data: List<AllLeadDataBean.Data>) {
        binding.rcAllLead.layoutManager = LinearLayoutManager(this)
        var mAdapter = AllLeadAdapter(this, data, intent.getStringExtra("leadStatus"), object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {
                leadID=pos
                apiLeadDetail(pos)
            }
        })
        binding.rcAllLead.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
    }

    @SuppressLint("SuspiciousIndentation")
    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.UpdateLead) {
                val updateLeadBean = apiClient.getConvertIntoModel<UpdateLeadBean>(
                    jsonElement.toString(),
                    UpdateLeadBean::class.java
                )
                Toast.makeText(this, updateLeadBean.msg, Toast.LENGTH_SHORT).show()

            }
            if (tag == ApiContants.AllLeadData) {
                val allLeadDataBean = apiClient.getConvertIntoModel<AllLeadDataBean>(
                    jsonElement.toString(),
                    AllLeadDataBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (allLeadDataBean.error==false) {
                    handleAllLead(allLeadDataBean.data)
                }
            }

            if (tag == ApiContants.LeadDetail) {
                val leadDeatilBean = apiClient.getConvertIntoModel<LeadDetailBean>(
                    jsonElement.toString(),
                    LeadDetailBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (leadDeatilBean.error==false) {
                    setLeadDetailDialog(leadDeatilBean.data)
                }
            }

            if (tag == ApiContants.DeleteProductData) {
                val productDeleteBean = apiClient.getConvertIntoModel<ProductDeleteBean>(jsonElement.toString(),ProductDeleteBean::class.java)
                   Toast.makeText(this, productDeleteBean.msg, Toast.LENGTH_SHORT).show()
                apiLeadDetail(leadID)


            }

            if (tag == ApiContants.RequestQuote) {
                val requestQuoteBean = apiClient.getConvertIntoModel<ProductDeleteBean>(
                    jsonElement.toString(),
                    ProductDeleteBean::class.java
                )

                    Toast.makeText(this, requestQuoteBean.msg, Toast.LENGTH_SHORT).show()
            }

        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }
    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(this, errorMessage)
    }

    fun apiLeadDetail(leadID: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["lead_id"] = leadID.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.LeadDetail, params)
    }

    fun apiRequestQuote(leadID: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["lead_id"] = leadID.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.RequestQuote, params)
    }

    fun apiProductDelete(leadID: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["lead_product_id"] = leadID.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.DeleteProductData, params)
    }

    fun setLeadDetailDialog(data: LeadDetailBean.Data) {
        val builder = AlertDialog.Builder(this,R.style.DialogFullscreen).create()
        val dialog = layoutInflater.inflate(R.layout.dailog_leaddetail,null)
        val  ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val tvStatus = dialog.findViewById<TextView>(R.id.tvStatus)
        val tvType = dialog.findViewById<TextView>(R.id.tvType)
        val tvCategory = dialog.findViewById<TextView>(R.id.tvCategory)
        val tvSubCategory = dialog.findViewById<TextView>(R.id.tvSubCategory)
        val tvPlumber = dialog.findViewById<TextView>(R.id.tvPlumber)
        val tvArchitect = dialog.findViewById<TextView>(R.id.tvArchitect)
        val tvPropertyStage = dialog.findViewById<TextView>(R.id.tvPropertyStage)
        val tvSource = dialog.findViewById<TextView>(R.id.tvSource)
        val tvState = dialog.findViewById<TextView>(R.id.tvState)
        val tvCity = dialog.findViewById<TextView>(R.id.tvCity)
        val tvMEP = dialog.findViewById<TextView>(R.id.tvMEP)
        val tvGST = dialog.findViewById<TextView>(R.id.tvGST)
        val tvAddress = dialog.findViewById<TextView>(R.id.tvAddress)
        val tvDealer = dialog.findViewById<TextView>(R.id.tvDealer)
        val tvSubmit = dialog.findViewById<TextView>(R.id.tvSubmit)
        val tvClient = dialog.findViewById<TextView>(R.id.tvClient)
        val tvEmail = dialog.findViewById<TextView>(R.id.tvEmail)
        val tvWhatsappNo = dialog.findViewById<TextView>(R.id.tvWhatsappNo)
        val tvName = dialog.findViewById<TextView>(R.id.tvName)
        val tvConst = dialog.findViewById<TextView>(R.id.tvConst)
        val tvRequestQuote = dialog.findViewById<TextView>(R.id.tvRequestQuote)
        val tvClientNumber = dialog.findViewById<TextView>(R.id.tvClientNumber)
        val tvClientAddress = dialog.findViewById<TextView>(R.id.tvClientAddress)
        val rcLeadProdtList = dialog.findViewById<RecyclerView>(R.id.rcLeadProdtList)
        val rcQuotation = dialog.findViewById<RecyclerView>(R.id.rcQuotation)
        val rcCommentList = dialog.findViewById<RecyclerView>(R.id.rcCommentList)
        val rbPartial = dialog.findViewById<RadioButton>(R.id.rbPartial)
        val rbCompleted = dialog.findViewById<RadioButton>(R.id.rbCompleted)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radioGroup)
        builder.setView(dialog)
        builder.show()
        ivClose.setOnClickListener {
            builder.dismiss()
        }
        tvRequestQuote.setOnClickListener {
            apiRequestQuote(leadID);
        }
        tvSubmit.setOnClickListener {
            apiUpdateLead()
        }
        if (data.leadData.quotesStatus==1){
            tvRequestQuote.setText("Requested")
        }
        /*data.leadData.quotesStatus*/


        if (!data.leadProducts.isNullOrEmpty()) handleLeadProductsList(rcLeadProdtList, data.leadProducts)
        if (!data.leadComments.isNullOrEmpty()) handleLeadCommentList(rcCommentList, data.leadComments)
        if (!data.quoteProduct.isNullOrEmpty())  handlercQuotationProdList(rcQuotation, data.quoteProduct)
        //   if (!data.customProduct.isNullOrEmpty())  handleDocumentList(rcDocumentList, data.customProduct)
        typeMode(radioGroup)
        if (!data.leadData.status.isNullOrEmpty()) tvStatus.setText(data.leadData.status)
        if (!data.leadData.type.isNullOrEmpty()) tvType.setText(data.leadData.type)
        if (!data.leadData.category.isNullOrEmpty())  tvCategory.setText(data.leadData.category.toString())
        if (!data.leadData.subCategory.isNullOrEmpty()) tvSubCategory.setText(data.leadData.subCategory.toString())
        if (!data.leadData.plumber.isNullOrEmpty())tvPlumber.setText(data.leadData.plumber.toString())
        if (!data.leadData.architect.isNullOrEmpty()) tvArchitect.setText(data.leadData.architect)
        if (!data.leadData.propertyStage.isNullOrEmpty())tvPropertyStage.setText(data.leadData.propertyStage)
        if (!data.leadData.source.isNullOrEmpty()) tvSource.setText(data.leadData.source)
        if (!data.leadData.state.isNullOrEmpty())  tvState.setText(data.leadData.state)
        if (!data.leadData.city.isNullOrEmpty()) tvCity.setText(data.leadData.city)
        if (!data.leadData.clientName.isNullOrEmpty())  tvClient.setText(data.leadData.clientName)
        if (!data.leadData.clientNumber.isNullOrEmpty())   tvClientNumber.setText(data.leadData.clientNumber)
        if (!data.leadData.name.isNullOrEmpty())   tvName.setText(data.leadData.name)
        if (!data.leadData.mep.isNullOrEmpty())   tvMEP.setText(data.leadData.mep)

        if (data.leadData.conversionType.equals("Completed")){
            radioGroup.visibility=View.GONE
            tvConst.visibility=View.GONE
        }else{
            radioGroup.visibility=View.VISIBLE
            tvConst.visibility=View.VISIBLE
        }


        if (!data.leadData.whatsappNo.isNullOrEmpty()) tvWhatsappNo.setText(data.leadData.whatsappNo)
        if (!data.leadData.email.isNullOrEmpty()) tvEmail.setText(data.leadData.email)

        if (!data.leadData.clientAddress.isNullOrEmpty()) tvClientAddress.setText(data.leadData.clientAddress)
        if (!data.leadData.gst.isNullOrEmpty()) tvGST.setText(data.leadData.gst)

        //   tvAddress.setText(data.address.toString())
        //  tvDealer.setText(data.dealer.toString())

       }

    fun apiUpdateLead() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["lead_id"] = leadID.toString()
        params["status"] = intent.getStringExtra("leadStatus").toString()
        params["remarks"] = ""
        params["call_date"] =""
        params["call_time"] =""
        params["conversion"] = conversionType
        params["gst"] = ""
        params["ids"] = Gson().toJson(quoteListID)

        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.UpdateLead, params)

    }

    fun typeMode(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.rbPartial) {
                    conversionType = "Partial"
                //    apiCatory(projectType)
                    Toast.makeText(this@AllLeadActivity,conversionType,Toast.LENGTH_SHORT).show()
                } else if (checkedId == R.id.rbCompleted) {
                    conversionType = "Completed"
                    Toast.makeText(this@AllLeadActivity,conversionType,Toast.LENGTH_SHORT).show()
                 //   apiCatory(projectType)
                }
            }
        })
    }

    fun handleLeadProductsList(
        rcLeadProdtList: RecyclerView,
        leadProduct: List<LeadDetailBean.Data.LeadProduct>
    ) {
        rcLeadProdtList.layoutManager = LinearLayoutManager(this)
        var mAdapter = LeadProdctListAdapter(this, leadProduct, object :
            RvStatusClickListner {
            override fun clickPos(status: String, leadID: Int) {
                apiProductDelete(leadID)
            }
        })
        rcLeadProdtList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
    }


    fun handleLeadCommentList(
        rcCommentList: RecyclerView,
        leadProduct: List<LeadDetailBean.Data.LeadComment>
    ) {
        rcCommentList.layoutManager = LinearLayoutManager(this)
        var mAdapter = LeadCommentListAdapter(this, leadProduct, object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {

            }
        })
        rcCommentList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun handlercQuotationProdList(
        rcCustmProdtList: RecyclerView,
        leadProduct: List<LeadDetailBean.Data.QuoteProduct>
    ) {
        rcCustmProdtList.layoutManager = LinearLayoutManager(this)
        var mAdapter = CustomProdListAdapter(this, leadProduct, object :
            RvListClickListner {
            override fun clickPos(status: MutableList<Any?>?, id: Int) {
                Log.d("zxzxc",Gson().toJson(status))
                quoteListID=status
            }

        })
        rcCustmProdtList.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }


    private fun openFullScreenDialog(imgUrl: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.fullscreen_dailog)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.show()

        val ivFullImage = dialog.findViewById<ImageView>(R.id.ivFullImage)
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val ivDownload = dialog.findViewById<ImageView>(R.id.ivDownload)
        //     ivClose.background = RoundView(Color.BLACK, RoundView.getRadius(100f))
        ivClose.setOnClickListener {
            dialog.dismiss() }
        ivDownload.setOnClickListener {
            GeneralUtilities.downloadUrl(activity,ApiContants.downloadUrl+imgUrl)
        }

        Glide.with(this)
            .load(ApiContants.BaseUrl+imgUrl)
            .into(ivFullImage)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK && requestCode==101){
            val leadStatus: String = data?.getStringExtra("leadStatus")!!
            Log.d("zxczc",leadStatus)
            apiAllLead(leadStatus)
        }

    }
    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        GeneralUtilities.unregisterBroadCastReceiver(this, myReceiver)
    }

    override fun onResume() {
        GeneralUtilities.registerBroadCastReceiver(this, myReceiver)
        SalesApp.setConnectivityListener(this)
        super.onResume()
    }

    override fun onNetworkConnectionChange(isconnected: Boolean) {
        ApiContants.isconnectedtonetwork = isconnected
        GeneralUtilities.internetConnectivityAction(this, isconnected)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
        startService(Intent(this, LocationService::class.java))
    }
}
