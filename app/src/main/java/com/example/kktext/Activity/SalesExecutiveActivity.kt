package com.example.kktext.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kktext.Adapter.*
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.Model.*
import com.example.kktext.R
import com.example.kktext.Utills.*
import com.example.kktext.databinding.ActivitySalesExecutiveBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants

class SalesExecutiveActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivitySalesExecutiveBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    var clientID=0
    var CompleteLead = ""
    var PartialConverted = ""
    var Converted = ""
    var newLeads = ""
    var pending_leads = ""
    var processing_leads = ""
    var allocated = ""
    var monthlySale = ""
    var yearSale = ""
    var call_scheduled = ""
    var visit_scheduled = ""
    var visit_done = ""
    var parital = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sales_executive)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Sales Executive"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.ivLogout.visibility = View.GONE
        binding.igToolbar.switchDayStart.visibility = View.GONE
        apiExecutiveList()
    }

    fun apiExecutiveList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getExecutiveList, params)
    }

    fun apiExecutiveDashboard() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params.put("id",clientID.toString())
      //  apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getExecutiveDashboard, params)
    }


    fun handleRcDashboard() {
        binding.rcDashboard.layoutManager = GridLayoutManager(this, 2)
        var mAdapter = DashboardAdapter(this, getMenus(), object :
            RvStatusClickListner {
            override fun clickPos(status:String,pos: Int) {

            }
        })
        binding.rcDashboard.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    private fun getMenus(): ArrayList<MenuModelBean> {
        var menuList = ArrayList<MenuModelBean>()

      //  menuList.add(MenuModelBean(1, "Total Lead", "", R.drawable.ic_dashbord))
        menuList.add(MenuModelBean(2, "New Lead", newLeads, R.drawable.ic_dashbord))
        menuList.add(
            MenuModelBean(
                3,
                "Pending Lead",
                pending_leads,
                R.drawable.ic_dashbord
            )
        )
        menuList.add(
            MenuModelBean(
                4,
                "Allocated",
                allocated,
                R.drawable.ic_dashbord
            )
        )

        menuList.add(
            MenuModelBean(
                5,
                "Processing",
                processing_leads,
                R.drawable.ic_dashbord
            )
        )

        menuList.add(
            MenuModelBean(
                6,
                "Call Scheduled",
                call_scheduled,
                R.drawable.ic_dashbord
            )
        )

        menuList.add(
            MenuModelBean(
                7,
                "Visit Scheduled",
                visit_scheduled,
                R.drawable.ic_dashbord
            )
        )
        menuList.add(
            MenuModelBean(
                8,
                "Visit Done",
                visit_done,
                R.drawable.ic_dashbord
            )
        )
        menuList.add(
            MenuModelBean(
                9,
                "Converted",
                Converted,
                R.drawable.ic_dashbord
            )
        )
       /* menuList.add(
            MenuModelBean(
                10,
                "Partial Converted",
                PartialConverted,
                R.drawable.ic_dashbord
            )
        )
        menuList.add(
            MenuModelBean(
                11,
                "Complete Lead",
                CompleteLead,
                R.drawable.ic_dashbord
            )
        )*/
        menuList.add(
            MenuModelBean(
                11,
                "Month sale",
                monthlySale,
                R.drawable.ic_dashbord
            )
        )
        menuList.add(
            MenuModelBean(
                11,
                "Year sale",
                yearSale,
                R.drawable.ic_dashbord
            )
        )
        return menuList
    }






    @SuppressLint("SuspiciousIndentation")
    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()

            if (tag == ApiContants.getExecutiveList) {
                val executiveListBean = apiClient.getConvertIntoModel<ExecutiveListBean>(
                    jsonElement.toString(),
                    ExecutiveListBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (executiveListBean.error==false) {
                    setClient(executiveListBean.data)
                    clientID=executiveListBean.data.get(0).id
                //    apiExecutiveDashboard()
                }
            }
            if (tag == ApiContants.getExecutiveDashboard) {
                val dashboardBean = apiClient.getConvertIntoModel<ExecutiveDashboardBean>(
                    jsonElement.toString(),
                    ExecutiveDashboardBean::class.java
                )
                //   Toast.makeText(this, allStatusBean.msg, Toast.LENGTH_SHORT).show()
                if (dashboardBean.error == false) {
                //    binding.tvMonthlyAmount.text = dashboardBean.data.monthlySale.toString()
               //     binding.tvAmount.text = dashboardBean.data.yearlySale.toString()


                    if (dashboardBean.data.newLeads == null) {
                        newLeads = "0"

                    } else {
                        newLeads = dashboardBean.data.newLeads
                    }
                    if (dashboardBean.data.pendingLeads == null) {
                        pending_leads = "0"

                    } else {
                        pending_leads = dashboardBean.data.pendingLeads
                    }
                    if (dashboardBean.data.processingLeads == null) {
                        processing_leads = "0"

                    } else {
                        processing_leads = dashboardBean.data.processingLeads
                    }
                    if (dashboardBean.data.smNewLeads == null) {
                        allocated = "0"

                    } else {
                        allocated = dashboardBean.data.smNewLeads
                    }
                    if (dashboardBean.data.monthlySale == null) {
                        monthlySale = "0"

                    } else {
                        monthlySale = dashboardBean.data.monthlySale
                    }
                    if (dashboardBean.data.yearlySale == null) {
                        yearSale = "0"

                    } else {
                        yearSale = dashboardBean.data.yearlySale
                    }

                    if (dashboardBean.data.callScheduled == null) {
                        call_scheduled = "0"

                    } else {
                        call_scheduled = dashboardBean.data.callScheduled
                    }
                    if (dashboardBean.data.visitScheduled == null) {
                        visit_scheduled = "0"

                    } else {
                        visit_scheduled = dashboardBean.data.visitScheduled
                    }
                    if (dashboardBean.data.visitDone == null) {
                        visit_done = "0"

                    } else {
                        visit_done = dashboardBean.data.visitDone
                    }


                /*    if (dashboardBean.data.convertedLeads == null) {
                        CompleteLead = "0"

                    } else {
                        CompleteLead = dashboardBean.data.convertedLeads.toString()
                    }
*/
                    if (dashboardBean.data.convertedLeads == null) {
                        Converted = "0"

                    } else {
                        Converted = dashboardBean.data.convertedLeads.toString()
                    }

                    handleRcDashboard()
                }

            }

        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }
    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(this, errorMessage)
    }



    fun setClient(data: List<ExecutiveListBean.Data>) {
        //  binding.stateSource.setThreshold(1);//will start working from first character
        val state = arrayOfNulls<String>(data.size)
        for (i in data.indices) {
            state[i] = data.get(i).name
        }

        binding.stateClient.setAdapter(
            ArrayAdapter(
                this@SalesExecutiveActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateClient.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name

            binding.stateClient.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            for (clientBean in data) {
                if (clientBean.name.equals(parent.getItemAtPosition(position).toString())) {
                    clientID = clientBean.id
                    Log.d("StateID", "" + clientBean.id)

                    apiExecutiveDashboard()
                }
            }
            Toast.makeText(
                applicationContext,
                binding.stateClient.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            apiExecutiveList()

        })
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
    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
        startService(Intent(this, LocationService::class.java))
    }
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

}
