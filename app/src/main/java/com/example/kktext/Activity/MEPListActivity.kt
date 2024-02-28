package com.example.kktext.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kktext.Adapter.*
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.Model.CityBean
import com.example.kktext.Model.MEPBean
import com.example.kktext.Model.UpdateClientBean
import com.example.kktext.R
import com.example.kktext.Utills.*
import com.example.kktext.databinding.ActivityClientListBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.util.*

class MEPListActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityClientListBinding
    private lateinit var mAllAdapter: MEPListAdapter
    private  var stateselector: AutoCompleteTextView?=null
    private  var cityselector: AutoCompleteTextView?=null
    private  var editName: TextInputEditText?=null
    private  var editMobNo: TextInputEditText?=null
    private  var editAddress: TextInputEditText?=null
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_list)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "All MEP"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.ivLogout.visibility = View.GONE
        binding.igToolbar.switchDayStart.visibility = View.GONE

        apiClientList()

        //dob and doa(aniversy)

       // cat subcat
        // get-lead-product-list

    }

    fun apiClientList() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getMEP, params)
    }

    fun apiUpdateClient(id: Int, doa: String, dob: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params.put("id",id.toString())
        params.put("name",editName?.text.toString())
        params.put("number",editMobNo?.text.toString())
        params.put("state",stateselector?.text.toString())
        params.put("city",cityselector?.text.toString())
        params.put("address",editAddress?.text.toString())
        params.put("doa",doa.toString())
        params.put("dob",dob.toString())
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.updateMep, params)
    }
    fun handleClientList(data: List<MEPBean.Data>) {
        binding.rcAllLead.layoutManager = LinearLayoutManager(this)
        mAllAdapter = MEPListAdapter(this, data, object :
            RvPlumberListner {
            override fun clickPos(
                name: String?,
                mobileNo: String?,
                state: String?,
                city: String?,
                address: String?,
                id: Int, doa: String?,dob: String?
            ) {
                openUpdateDetailDialog(name,mobileNo,state,city,address,id,doa,dob)
            }


        })
        binding.rcAllLead.adapter = mAllAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
        binding.rcAllLead.isNestedScrollingEnabled = false
        mAllAdapter.notifyDataSetChanged()

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (data != null) {
                    mAllAdapter.filter.filter(s)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                mAllAdapter.filter.filter(s)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mAllAdapter.filter.filter(s)
               /* if (s.toString().trim { it <= ' ' }.length < 1) {
                    ivClear.visibility = View.GONE
                } else {
                    ivClear.visibility = View.GONE
                }*/
            }
        })

    }

    @SuppressLint("SuspiciousIndentation")
    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            apiClient.progressView.hideLoader()

            if (tag == ApiContants.getMEP) {
                val mepBean = apiClient.getConvertIntoModel<MEPBean>(
                    jsonElement.toString(),
                    MEPBean::class.java
                )

                if (mepBean.error==false) {
                    handleClientList(mepBean.data)
                }
            }
            if (tag == ApiContants.getCity) {
                val cityBean = apiClient.getConvertIntoModel<CityBean>(
                    jsonElement.toString(),
                    CityBean::class.java
                )
                if (cityBean.error == false) {
                    val state = arrayOfNulls<String>(cityBean.data.size)
                    for (i in cityBean.data.indices) {
                        //Storing names to string array
                        state[i] = cityBean.data.get(i).city
                    }
                    val adapte1: ArrayAdapter<String?>
                    adapte1 = ArrayAdapter(
                        this@MEPListActivity,
                        android.R.layout.simple_list_item_1,
                        state
                    )
                    cityselector?.setAdapter(adapte1)
                    cityselector?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

                        cityselector?.setText(parent.getItemAtPosition(position).toString())
                        Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
                        Toast.makeText(
                            applicationContext,
                            cityselector?.getText().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        apiCity(stateselector?.text.toString())
                    })
                }


            }

            if (tag == ApiContants.updateMep) {
                val updateClientBean = apiClient.getConvertIntoModel<UpdateClientBean>(
                    jsonElement.toString(),
                    UpdateClientBean::class.java
                )

                if (updateClientBean.error==false) {
                   Toast.makeText(this,updateClientBean.msg,Toast.LENGTH_SHORT).show()
                    apiClientList()
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

    fun openUpdateDetailDialog(
        name: String?,
        mobileNo: String?,
        state: String?,
        city: String?,
        address: String?,
        id: Int, doa: String?,
        dob: String?,
    ) {
        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val dialog = layoutInflater.inflate(R.layout.dialog_update_client,null)

        builder.setView(dialog)

        builder.setCanceledOnTouchOutside(false)
        builder.show()
        /*    val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
                R.layout.dialog_update_client, R.style.AppBottomSheetDialogTheme,
                this
            )*/
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        editName = dialog.findViewById<TextInputEditText>(R.id.editName) as TextInputEditText
        editMobNo = dialog.findViewById<TextInputEditText>(R.id.editMobNo) as TextInputEditText
        editAddress = dialog.findViewById<TextInputEditText>(R.id.editAddress) as TextInputEditText
        stateselector = dialog.findViewById<AutoCompleteTextView>(R.id.stateselector) as AutoCompleteTextView
        cityselector = dialog.findViewById<AutoCompleteTextView>(R.id.cityselector) as AutoCompleteTextView
        val editDOB = dialog.findViewById<EditText>(R.id.editDOB) as EditText
        val editDOA = dialog.findViewById<EditText>(R.id.editDOA) as EditText
        val btnSubmit = dialog.findViewById<TextView>(R.id.btnSubmit) as TextView
        setState()
        ivClose.setOnClickListener {  builder.dismiss() }
        btnSubmit.setOnClickListener { builder.dismiss()
            apiUpdateClient(id,editDOA.text.toString(),editDOB.text.toString())
        }
        editName!!.setText(name)
        editMobNo!!.setText(mobileNo)
        editAddress!!.setText(address)
        stateselector!!.setText(state)
        cityselector!!.setText(city)
        editDOB!!.setText(doa)
        editDOA!!.setText(dob)

        editDOB.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                this@MEPListActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews = "${ year.toString()+ "-"+(monthOfYear + 1).toString()  + "-" + dayOfMonth.toString() }"

                    //   val dateofnews = (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    editDOB.setText(dateofnews)
                },
                year, month, day
            )
            datePickerDialog.show()
        })

        editDOA.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(
                this@MEPListActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    //  dob.setText(dateofnews);
                    val dateofnews = "${ year.toString()+ "-"+(monthOfYear + 1).toString()  + "-" + dayOfMonth.toString() }"

                    //   val dateofnews = (monthOfYear + 1).toString() + "/" + dayOfMonth + "/" + year
                    editDOA.setText(dateofnews)
                },
                year, month, day
            )
            datePickerDialog.show()
        })
    }
    fun setState() {
        val state = arrayOfNulls<String>(SalesApp.stateList.size)
        for (i in SalesApp.stateList.indices) {
            state[i] = SalesApp.stateList.get(i).state
        }

        stateselector?.setAdapter(
            ArrayAdapter(
                this@MEPListActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        stateselector?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            stateselector?.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                stateselector?.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setState()
            apiCity(stateselector?.text.toString())
        })
    }
    fun apiCity(stateName: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["state"] = stateName
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCity, params)

    }

    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
      //  startService(Intent(this, LocationService::class.java))
    }
}
