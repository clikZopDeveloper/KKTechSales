package com.example.kktext.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.Model.LoginBean
import com.example.kktext.R
import com.example.kktext.Utills.GeneralUtilities
import com.example.kktext.Utills.PrefManager
import com.example.kktext.Utills.Utility
import com.example.kktext.databinding.ActivityLoginBinding
import com.example.kktext.viewmodelactivity.LoginViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import java.util.*

class LoginActivity : AppCompatActivity() , ApiResponseListner {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiClient: ApiController
    lateinit var  loginViewModel:LoginViewModel
    var activity: Activity = this
    private var currentLoc: String?=null
    private val permissionId = 2
    var location: Location? = null
    private var isPermissionGranted = false
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
      //  loginViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(LoginViewModel::class.java )

//binding.loginViewModel=loginViewModel
 //       binding.lifecycleOwner=this
     //   getLocation()

        binding.btnLogin.setOnClickListener {
    doLogin()
}

    }

    private fun doLogin() {
        if (TextUtils.isEmpty(binding.editMobNo.text.toString())) {
            Utility.showSnackBar(this, "Please enter mobile number")
        } else if (binding.editMobNo.text.toString().length < 10) {
            Utility.showSnackBar(this, "Please enter valid mobile number")
        } else if (TextUtils.isEmpty(binding.editPassword.text.toString())) {
            Utility.showSnackBar(this, "Please enter password")
        } else {
        //    loginViewModel.apiCallLogin()
         apiCallLogin()
        }
    }

    fun apiCallLogin() {
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["mobile"] = binding.editMobNo.text.toString()
        params["password"] = binding.editPassword.text.toString()

        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.login, params)

    }

    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()
            if (tag == ApiContants.login) {
                val loginModel = apiClient.getConvertIntoModel<LoginBean>(jsonElement.toString(), LoginBean::class.java)
                if (loginModel.error==false) {
                    PrefManager.putString(ApiContants.AccessToken, loginModel.data.token )
                    PrefManager.putString(ApiContants.mobileNumber, binding.editMobNo.text.toString())
                    PrefManager.putString(ApiContants.password,  binding.editPassword.text.toString() )
                    Toast.makeText(activity, loginModel.msg, Toast.LENGTH_SHORT).show()
                    GeneralUtilities.launchActivity(this, DashboardActivity::class.java)
                    finishAffinity()
                }

            }
        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }

    }

    override fun failure(tag: String?, errorMessage: String) {

        apiClient.progressView.hideLoader()

        Utility.showSnackBar(activity, errorMessage)
    }


    class ViewModelFactory(val context:LoginActivity):
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                LoginViewModel(context) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) { isPermissionGranted = (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            return
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        // Start the LocationService when the app is closed
        startService(Intent(this, LocationService::class.java))
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                this@LoginActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            isPermissionGranted = false
            //   Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
            getPermission()
        } else {
            //   Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
            isPermissionGranted = true
        }
    }


    private fun getPermission() {
        if (ContextCompat.checkSelfPermission(
                this@LoginActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            //   Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@LoginActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                //      Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
              requestPermission(this@LoginActivity)
            } else {
                //      Toast.makeText(getActivity(), "5", Toast.LENGTH_SHORT).show();
             requestPermission(this@LoginActivity)
            }
        } else {
            // Permission has already been granted
            //   Toast.makeText(getActivity(), "6", Toast.LENGTH_SHORT).show();
            isPermissionGranted = true
        }
    }


    fun requestPermission(activity: Activity?) {
        ActivityCompat.requestPermissions( activity!!,  arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),   1001
        )
    }
}
