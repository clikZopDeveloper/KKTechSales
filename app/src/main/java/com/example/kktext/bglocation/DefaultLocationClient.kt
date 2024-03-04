package com.example.kktext


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.Model.ProductDeleteBean
import com.example.kktext.Utills.SalesApp
import com.example.kktext.Utills.Utility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient): LocationClient , ApiResponseListner {
    private lateinit var apiClient: ApiController
    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if(!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Missing location permission")
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("GPS is disabled")
            }
            val request = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    result.locations.lastOrNull()?.let { location ->
                        apiUpdateLoction(location)

                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
    fun apiUpdateLoction(location: Location) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(context, this)
        val params = Utility.getParmMap()
        params["last_location"] = "${location.latitude},${location.longitude}"
        //    apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getLocationUpdate, params)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun success(tag: String?, jsonElement: JsonElement?) {
        try {
            //    apiClient.progressView.hideLoader()
            if (tag == ApiContants.getLocationUpdate) {
                val requestQuoteBean = apiClient.getConvertIntoModel<ProductDeleteBean>(
                    jsonElement.toString(),
                    ProductDeleteBean::class.java
                )

                //  Toast.makeText(this, requestQuoteBean.msg, Toast.LENGTH_SHORT).show()
            }

        }catch (e:Exception){
            Log.d("error>>",e.localizedMessage)
        }
    }

    override fun failure(tag: String?, errorMessage: String) {
        //    apiClient.progressView.hideLoader()
        Utility.showSnackBar(context = Activity(), errorMessage)
    }

}