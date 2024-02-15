package com.example.kktext.Activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kktext.Adapter.EditProductListAdapter
import com.example.kktext.Adapter.ProductListAdapter
import com.example.kktext.ApiHelper.ApiController
import com.example.kktext.ApiHelper.ApiResponseListner
import com.example.kktext.Model.*
import com.example.kktext.R
import com.example.kktext.Utills.*
import com.example.kktext.Utills.GeneralUtilities.getPath
import com.example.kktext.databinding.ActivityEditLeadBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.stpl.antimatter.Utils.ApiContants
import okhttp3.MultipartBody
import java.io.File


class EditLeadActivity : AppCompatActivity(), ApiResponseListner,
    GoogleApiClient.OnConnectionFailedListener,
    ConnectivityListener.ConnectivityReceiverListener {
    private lateinit var binding: ActivityEditLeadBinding
    private lateinit var apiClient: ApiController
    var myReceiver: ConnectivityListener? = null
    var activity: Activity = this
    var projectType = "Commercial"

    var customProdCatName = ""
    var customProductCatID = 0
    var customSubCatName = ""


    var projectCatID = 0
    var productCategoryID = 0
    var subProdCatID = 0
    var projectSubCatID = 0
    var clientID = 0
    var productID = 0


    val PERMISSION_CODE = 12345
    val CAMERA_PERMISSION_CODE1 = 123
    var SELECT_PICTURES1 = 1
    var file2: File? = null
    var type = arrayOf("Residential", "Commercial")
    val list: MutableList<LeadProductListBean.Data> = ArrayList()
    val multipleProdlist: MutableList<MultipleProListBean> = ArrayList()
    val cutomProdList: MutableList<MultipleProductBean> = ArrayList()
    val cutomProdImgList: MutableList<CustProdImgBean> = ArrayList()
    val cutomFinalList: MutableList<Any> = ArrayList()
    var leadID = ""
    var leadStatus = ""
    var source = ""
    var architect = ""
    var client = ""
    var date = ""
    var propertyStage = ""
    var gst = ""
    var state = ""
    var city = ""
    var mep = ""
    var catID = ""
    var subCatID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_lead)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        myReceiver = ConnectivityListener()

        binding.igToolbar.tvTitle.text = "Edit Lead"
        binding.igToolbar.ivMenu.setImageDrawable(resources.getDrawable(R.drawable.ic_back_black))
        binding.igToolbar.ivMenu.setOnClickListener { finish() }
        binding.igToolbar.ivLogout.visibility = View.GONE
        binding.igToolbar.switchDayStart.visibility = View.GONE

try {
    leadID = intent.getStringExtra("leadID")!!
    leadStatus = intent.getStringExtra("leadStatus")!!
    source = intent.getStringExtra("source")!!
    architect = intent.getStringExtra("architect")!!
    client = intent.getStringExtra("client")!!
    date = intent.getStringExtra("date")!!
    propertyStage = intent.getStringExtra("propertyStage")!!
    gst = intent.getStringExtra("gst")!!
    state = intent.getStringExtra("state")!!
    city = intent.getStringExtra("city")!!
    catID = intent.getStringExtra("catID")!!
    subCatID = intent.getStringExtra("subCatID")!!

    if (intent.getStringExtra("mep")!=null){
        mep = intent.getStringExtra("mep")!!
        binding.stateMEP.setText(mep).toString()
    }

    binding.stateSource.setText(source).toString()
    binding.stateArchitect.setText(architect).toString()
    binding.stateClient.setText(client).toString()
    binding.statePropertyStage.setText(propertyStage).toString()
    binding.editGSTNo.setText(gst).toString()
    binding.stateselector.setText(state).toString()
    binding.cityselector.setText(city).toString()


}catch (e:Exception){
    Log.d("error>>>>>>>>>",e.localizedMessage)
}

        //  requestPermission()
        setSourceData()
        setProductCat()
        //   setCustomProdCat()
        setState()
        setArchitect()
        setMEP()
        //  setDealer()
        setInstaller()
        setPropertyStage()
        setClient()
        typeMode()
        apiCatory(projectType)
       // apiSubCatory(subCatID.toInt())
      //  apiProductList(leadID)
        binding.stateDealer.isEnabled = false
        apiLeadProductList("", 0,"")
        /*   if (checkBox.isChecked()){
            tvAcceptPolicy.setEnabled(true);
            tvAcceptPolicy.setAlpha(1f);
        }else {

        }*/

        binding.btnAadharFront.setOnClickListener {
            openCameraDialog(SELECT_PICTURES1, CAMERA_PERMISSION_CODE1)

        }
        binding.dealerCheck.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (binding.dealerCheck.isChecked()) {
                binding.stateDealer.isEnabled = true
            } else {
                binding.stateDealer.isEnabled = false
            }
        })

        binding.btnAddProduct.setOnClickListener {
            if (TextUtils.isEmpty(binding.stateProductCategory.text.toString())) {
                Utility.showSnackBar(this, "Please select category")
            } else if (TextUtils.isEmpty(binding.productSubCategory.text.toString())) {
                Utility.showSnackBar(this, "Please select subcategory")
            } else if (TextUtils.isEmpty(productID.toString())) {
                Utility.showSnackBar(this, "Please select product")
            } else if (TextUtils.isEmpty(binding.editQty.text.toString())) {
                Utility.showSnackBar(this, "Enter qty")
            } else {
                apiLeadProductList("add", productID,binding.editQty.text.toString())
            }
        }

        binding.btnAddCustProduct.setOnClickListener {
            if (TextUtils.isEmpty(customProdCatName)) {
                Utility.showSnackBar(this, "Please Select Custom Category")
            } else if (TextUtils.isEmpty(customSubCatName)) {
                Utility.showSnackBar(this, "Please Select Custom Subcategory")
            } else if (TextUtils.isEmpty(binding.editCustmProdName.text.toString())) {
                Utility.showSnackBar(this, "Enter Custom Product Name")
            } else if (TextUtils.isEmpty(binding.editCustmQty.text.toString())) {
                Utility.showSnackBar(this, "Enter qty")
            } else if (file2 == null) {
                Utility.showSnackBar(this, "Please Select Image")
            } else {
                addDataCustmProdtList()
            }
        }

        //  loginViewModel = ViewModelProvider(this, ViewModelFactory(this)).get(LoginViewModel::class.java )

//binding.loginViewModel=loginViewModel
        //       binding.lifecycleOwner=this

        binding.btnSubmit.setOnClickListener {
            apiInsertLead()
        }
    }

    fun apiProductList(leadID: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(this, this)
        val params = Utility.getParmMap()
        params["lead_id"] = leadID
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getLeadProductList, params)
    }
   /* params["type"] = leadID
    params["lead_id"] = leadID
    params["lead_product_id"] = leadID
    params["qty"] = leadID*/

    fun typeMode() {
        binding.radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId == R.id.rbResidential) {
                    projectType = "Residential"
                    apiCatory(projectType)
                } else if (checkedId == R.id.rbCommercial) {
                    projectType = "Commercial"
                    apiCatory(projectType)
                } /*else if (checkedId == R.id.rbOthers) {
                    projectType = "Others"
                    apiCatory(projectType)
                }*/
            }
        })
    }

    fun apiInsertLead() {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        // builder.addFormDataPart("installer", "")
        //  builder.addFormDataPart("dealer", binding.stateDealer.text.toString())
        builder.addFormDataPart("lead_id", leadID.toString())
        builder.addFormDataPart("source", binding.stateSource.text.toString())
        builder.addFormDataPart("plumber", binding.stateInstaller.text.toString())
        builder.addFormDataPart("architect", binding.stateArchitect.text.toString())
        builder.addFormDataPart("project_type", projectType)
        builder.addFormDataPart("project_category_id", projectCatID.toString())
        builder.addFormDataPart("project_sub_category_id", projectSubCatID.toString())
        builder.addFormDataPart("property_stage", binding.statePropertyStage.text.toString())
        builder.addFormDataPart("state", binding.stateselector.text.toString())
        builder.addFormDataPart("city", binding.cityselector.text.toString())
        builder.addFormDataPart("client_id", clientID.toString())
        builder.addFormDataPart("remarks", binding.editRemark.text.toString())
        builder.addFormDataPart("gst", binding.editGSTNo.text.toString())
        builder.addFormDataPart("address", binding.editAddress.text.toString())
        builder.addFormDataPart("mep", binding.stateMEP.getText().toString())

        //  builder.addFormDataPart("catg_id", productCategoryID.toString())//product Id
        // builder.addFormDataPart("sub_catg_id", subProdCatID.toString())//product sub cat Id

        builder.addFormDataPart(
            "prod_list",
            Gson().toJson(multipleProdlist)
        )// multiple Product List  (product Id ,Qty)

        //  val filesToUpload = arrayOfNulls<File>(cutomProdImgList.size)
        /* for (i in 0 until cutomProdImgList.size) {
             builder.addFormDataPart("files", "img[" + i + "]"+System.currentTimeMillis(),
                 RequestBody.create("multipart/form-data".toMediaTypeOrNull(), cutomProdImgList.get(i).image))
         }*/

        Log.d("requestParms", Gson().toJson(builder))
        apiClient.progressView.showLoader()
        apiClient.makeCallMultipart(ApiContants.getLeadUpdate, builder.build())

    }

    fun apiLeadProductList(type: String, productID: Int, qty: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["type"] = type
        params["lead_id"] = leadID
        params["lead_product_id"] = productID.toString()
        params["qty"] = qty
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.DeleteProductData, params)

    }

    fun apiCity(stateName: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["state"] = stateName
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCity, params)

    }

    fun apiGetProduct(cat: String, subCat: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["category"] = cat
        params["subcategory"] = subCat
        //   apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getProduct, params)

    }

    fun apiProductSubCat(cat: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["catg_id"] = cat.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getProductSubCategory, params)

    }

    fun apiCustProdSubCat(cat: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["catg_id"] = cat.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getCustomProdSubCat, params)

    }

    fun apiCatory(type: String) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["type"] = type
        //    apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getProjectCategory, params)
    }

    fun apiSubCatory(subCatId: Int) {
        SalesApp.isAddAccessToken = true
        apiClient = ApiController(activity, this)
        val params = Utility.getParmMap()
        params["catg_id"] = subCatId.toString()
        apiClient.progressView.showLoader()
        apiClient.getApiPostCall(ApiContants.getProjectSubCategory, params)
    }

    override fun success(tag: String?, jsonElement: JsonElement) {
        try {
            apiClient.progressView.hideLoader()

            if (tag == ApiContants.getLeadUpdate) {
                val baseResponseBean = apiClient.getConvertIntoModel<BaseResponseBean>(
                    jsonElement.toString(),
                    BaseResponseBean::class.java
                )
                Toast.makeText(this, baseResponseBean.msg, Toast.LENGTH_SHORT).show()
                val returnIntent = Intent()
                returnIntent.putExtra("leadStatus", leadStatus)
                setResult(RESULT_OK, returnIntent)
                finish()

            }

            if (tag == ApiContants.getLeadProductList) {
                val leadProductListBean = apiClient.getConvertIntoModel<LeadProductListBean>(
                    jsonElement.toString(),
                    LeadProductListBean::class.java
                )

                     handleProductList(leadProductListBean.data)

            }

            if (tag == ApiContants.DeleteProductData) {
                val productDeleteBean = apiClient.getConvertIntoModel<LeadProductListBean>(jsonElement.toString(),
                    LeadProductListBean::class.java)
                Toast.makeText(this, productDeleteBean.msg, Toast.LENGTH_SHORT).show()

                    handleProductList(productDeleteBean.data)

            }
            if (tag == ApiContants.getProjectCategory) {
                val projectCategoryBean = apiClient.getConvertIntoModel<ProjectCategoryBean>(
                    jsonElement.toString(),
                    ProjectCategoryBean::class.java
                )
                if (projectCategoryBean.error == false) {
                    for (i in projectCategoryBean.data.indices) {
                        //Storing names to string array
                       val  id= projectCategoryBean.data.get(i).id
                        if (id.toString().equals(catID)){
                            Log.d("czxc",projectCategoryBean.data.get(i).name)
                            binding.stateCategory.setText(projectCategoryBean.data.get(i).name)
                        }
                    }

                    val state = arrayOfNulls<String>(projectCategoryBean.data.size)
                    for (i in projectCategoryBean.data.indices) {
                        //Storing names to string array
                        state[i] = projectCategoryBean.data.get(i).name
                    }

                    binding.stateCategory.setAdapter(
                        ArrayAdapter(
                            this@EditLeadActivity,
                            android.R.layout.simple_list_item_1, state
                        )
                    )

                    binding.stateCategory.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                        binding.stateCategory.setText(parent.getItemAtPosition(position).toString())

                        for (projectCatBean in projectCategoryBean.data) {
                            if (projectCatBean.name.equals(
                                    parent.getItemAtPosition(position).toString()
                                )
                            ) {
                                projectCatID = projectCatBean.id
                                Log.d("StateID", "" + projectCatBean.id)
                            }
                        }
                        Toast.makeText(
                            applicationContext,
                            binding.stateCategory.getText().toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        apiCatory(projectType)
                        apiSubCatory(projectCatID)
                    })
                }

            }
            if (tag == ApiContants.getProjectSubCategory) {
                val subCategoryBean = apiClient.getConvertIntoModel<SubCategoryBean>(
                    jsonElement.toString(),
                    SubCategoryBean::class.java
                )
                if (subCategoryBean.error == false) {
                    val state = arrayOfNulls<String>(subCategoryBean.data.size)
                    for (i in subCategoryBean.data.indices) {
                        //Storing names to string array
                        state[i] = subCategoryBean.data.get(i).name
                    }
                    for (i in subCategoryBean.data.indices) {
                        //Storing names to string array
                        val  id= subCategoryBean.data.get(i).id
                        if (id.toString().equals(subCatID)){
                            Log.d("czxc",subCategoryBean.data.get(i).name)
                            binding.stateSubCategory.setText(subCategoryBean.data.get(i).name)
                        }
                    }

                    binding.stateSubCategory.setAdapter(
                        ArrayAdapter(
                            this@EditLeadActivity,
                            android.R.layout.simple_list_item_1, state
                        )
                    )
                    binding.stateSubCategory.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                        binding.stateSubCategory.setText(
                            parent.getItemAtPosition(position).toString()
                        )
                        Log.d("StateID", "" + parent.getItemAtPosition(position).toString())


                        for (subCatBean in subCategoryBean.data) {
                            if (subCatBean.name.equals(
                                    parent.getItemAtPosition(position).toString()
                                )
                            ) {
                                projectSubCatID = subCatBean.id
                                Log.d("StateID", "" + subCatBean.id)
                            }
                        }

                        Toast.makeText(
                            applicationContext,
                            binding.stateSubCategory.getText().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                }


                //  apiSubCatory(projectCatID)

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
                        this@EditLeadActivity,
                        android.R.layout.simple_list_item_1,
                        state
                    )
                    binding.cityselector.setAdapter(adapte1)
                    binding.cityselector.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

                        binding.cityselector.setText(parent.getItemAtPosition(position).toString())
                        Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
                        Toast.makeText(
                            applicationContext,
                            binding.cityselector.getText().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        apiCity(binding.stateselector.text.toString())
                    })
                }

            }
            if (tag == ApiContants.getProductSubCategory) {
                val subProductCatBean = apiClient.getConvertIntoModel<SubProductCatBean>(
                    jsonElement.toString(),
                    SubProductCatBean::class.java
                )
                if (subProductCatBean.error == false) {
                    val state = arrayOfNulls<String>(subProductCatBean.data.size)
                    for (i in subProductCatBean.data.indices) {
                        //Storing names to string array
                        state[i] = subProductCatBean.data.get(i).name
                    }
                    val adapte1: ArrayAdapter<String?>
                    adapte1 = ArrayAdapter(
                        this@EditLeadActivity,
                        android.R.layout.simple_list_item_1,
                        state
                    )
                    binding.productSubCategory.setAdapter(adapte1)
                    binding.productSubCategory.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

                        binding.productSubCategory.setText(
                            parent.getItemAtPosition(position).toString()
                        )
                        Log.d("StateID", "" + parent.getItemAtPosition(position).toString())

                        for (productSubCatBean in subProductCatBean.data) {
                            if (productSubCatBean.name.equals(
                                    parent.getItemAtPosition(position).toString()
                                )
                            ) {
                                subProdCatID = productSubCatBean.id
                                Log.d("StateID", "" + productSubCatBean.id)
                            }
                        }

                        Toast.makeText(
                            applicationContext,
                            binding.productSubCategory.getText().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        apiGetProduct(
                            binding.stateProductCategory.text.toString(),
                            binding.productSubCategory.text.toString()
                        )
                        apiProductSubCat(productCategoryID)
                    })
                }

            }
            if (tag == ApiContants.getProduct) {
                val productBean = apiClient.getConvertIntoModel<GetProductBean>(
                    jsonElement.toString(),
                    GetProductBean::class.java
                )
                if (productBean.error == false) {
                    val state = arrayOfNulls<String>(productBean.data.size)
                    for (i in productBean.data.indices) {
                        //Storing names to string array
                        state[i] = productBean.data.get(i).name
                    }
                    val adapte1: ArrayAdapter<String?>
                    adapte1 = ArrayAdapter(
                        this@EditLeadActivity,
                        android.R.layout.simple_list_item_1,
                        state
                    )
                    binding.stateProduct.setAdapter(adapte1)
                    binding.stateProduct.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                        binding.stateProduct.setText(parent.getItemAtPosition(position).toString())
                        Log.d("StateID", "" + parent.getItemAtPosition(position).toString())

                        for (productBean in productBean.data) {
                            if (productBean.name.equals(
                                    parent.getItemAtPosition(position).toString()
                                )
                            ) {
                                productID = productBean.id
                                Log.d("StateID", "" + productBean.id)
                            }
                        }
                        Toast.makeText(
                            applicationContext,
                            binding.stateProduct.getText().toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        apiGetProduct(
                            binding.stateProductCategory.text.toString(),
                            binding.productSubCategory.text.toString()
                        )
                    })
                }


            }

            /*
                 if (tag == ApiContants.getCustomProdSubCat) {
                      val customSubCatBean = apiClient.getConvertIntoModel<CustomSubCatBean>(
                          jsonElement.toString(),
                          CustomSubCatBean::class.java
                      )
                      val state = arrayOfNulls<String>(customSubCatBean.data.size)
                      for (i in customSubCatBean.data.indices) {
                          //Storing names to string array
                          state[i] = customSubCatBean.data.get(i).name
                      }
                      val adapte1: ArrayAdapter<String?>
                      adapte1 = ArrayAdapter(
                          this@AddLeadActivity,
                          android.R.layout.simple_list_item_1,
                          state
                      )
                      binding.CustSubCatProduct.setAdapter(adapte1)
                      binding.CustSubCatProduct.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                          customSubCatName = customSubCatBean.data.get(position).name

                          Log.d("StateID", "" + customSubCatName)
                          binding.CustSubCatProduct.setText(customSubCatName)
                          Toast.makeText(
                              applicationContext,
                              binding.CustSubCatProduct.getText().toString(),
                              Toast.LENGTH_SHORT
                          ).show()

                          apiCustProdSubCat(customProductCatID)
                      })

                  }
                 */

        } catch (e: Exception) {
            Log.d("error>>", e.localizedMessage)
        }
    }

    override fun failure(tag: String?, errorMessage: String) {
        apiClient.progressView.hideLoader()
        Utility.showSnackBar(activity, errorMessage)
    }

    fun setSourceData() {
        //  binding.stateSource.setThreshold(1);//will start working from first character
        val state = arrayOfNulls<String>(SalesApp.sourceList.size)
        for (i in SalesApp.sourceList.indices) {
            state[i] = SalesApp.sourceList.get(i).name
        }

        binding.stateSource.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateSource.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name

            binding.stateSource.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                binding.stateSource.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setSourceData()

        })
    }

    fun setState() {
        val state = arrayOfNulls<String>(SalesApp.stateList.size)
        for (i in SalesApp.stateList.indices) {
            state[i] = SalesApp.stateList.get(i).state
        }

        binding.stateselector.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateselector.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            binding.stateselector.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                binding.stateselector.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setState()
            apiCity(binding.stateselector.text.toString())
        })
    }

    fun setInstaller() {
        val state = arrayOfNulls<String>(SalesApp.installerList.size)
        for (i in SalesApp.installerList.indices) {
            state[i] = SalesApp.installerList.get(i).name
        }

        binding.stateInstaller.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateInstaller.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            binding.stateInstaller.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())

            Toast.makeText(
                applicationContext,
                binding.stateInstaller.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            setInstaller()
        })

    }

    fun setArchitect() {
        val state = arrayOfNulls<String>(SalesApp.architectList.size)
        for (i in SalesApp.architectList.indices) {
            state[i] = SalesApp.architectList.get(i).name
        }

        binding.stateArchitect.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateArchitect.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            binding.stateArchitect.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                binding.stateArchitect.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setArchitect()


        })
    }

    fun setMEP() {
        //  binding.stateSource.setThreshold(1);//will start working from first character
        val state = arrayOfNulls<String>(SalesApp.mepList.size)
        for (i in SalesApp.mepList.indices) {
            state[i] = SalesApp.mepList.get(i).name
        }

        binding.stateMEP.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateMEP.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //  var sourceName = SalesApp.sourceList.get(position).name

            binding.stateMEP.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                binding.stateMEP.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            setMEP()

        })
    }

    fun setDealer() {
        val state = arrayOfNulls<String>(SalesApp.dealerList.size)
        for (i in SalesApp.dealerList.indices) {
            state[i] = SalesApp.dealerList.get(i).name
        }

        binding.stateDealer.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateDealer.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            binding.stateDealer.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                binding.stateDealer.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setDealer()
        })
    }

    fun setPropertyStage() {
        val state = arrayOfNulls<String>(SalesApp.propertyStageList.size)
        for (i in SalesApp.propertyStageList.indices) {
            state[i] = SalesApp.propertyStageList.get(i).title
        }

        binding.statePropertyStage.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.statePropertyStage.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            binding.statePropertyStage.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())
            Toast.makeText(
                applicationContext,
                binding.statePropertyStage.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setPropertyStage()
        })
    }

    fun setClient() {
        val state = arrayOfNulls<String>(SalesApp.clientList.size)
        for (i in SalesApp.clientList.indices) {
            state[i] = SalesApp.clientList.get(i).name + " / " + SalesApp.clientList.get(i).number
        }

        binding.stateClient.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )
        binding.stateClient.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            binding.stateClient.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())

            for (clientBean in SalesApp.clientList) {

                val name = clientBean.name + " / " + clientBean.number
                if (name.equals(parent.getItemAtPosition(position).toString())) {
                    clientID = clientBean.id
                    Log.d("StateID", "" + clientBean.id)
                }
            }

            Toast.makeText(
                applicationContext,
                binding.stateClient.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()

            setClient()
        })
    }

    fun setProductCat() {
        val state = arrayOfNulls<String>(SalesApp.productCatList.size)
        for (i in SalesApp.productCatList.indices) {
            state[i] = SalesApp.productCatList.get(i).name
        }

        binding.stateProductCategory.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateProductCategory.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->

            binding.stateProductCategory.setText(parent.getItemAtPosition(position).toString())
            Log.d("StateID", "" + parent.getItemAtPosition(position).toString())

            for (productCatBean in SalesApp.productCatList) {
                if (productCatBean.name.equals(parent.getItemAtPosition(position).toString())) {
                    productCategoryID = productCatBean.id
                    Log.d("StateID", "" + productCatBean.id)
                }
            }

            Toast.makeText(
                applicationContext,
                binding.stateProductCategory.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            apiProductSubCat(productCategoryID)
            setProductCat()
        })
    }

    fun setCustomProdCat() {
        val state = arrayOfNulls<String>(SalesApp.prodCustomCatList.size)
        for (i in SalesApp.prodCustomCatList.indices) {
            state[i] = SalesApp.prodCustomCatList.get(i).name
        }

        binding.stateCustProdCat.setAdapter(
            ArrayAdapter(
                this@EditLeadActivity,
                android.R.layout.simple_list_item_1, state
            )
        )

        binding.stateCustProdCat.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            customProdCatName = SalesApp.prodCustomCatList.get(position).name

            customProductCatID = SalesApp.prodCustomCatList.get(position).id
            Log.d("StateID", "" + customProdCatName)
            binding.stateCustProdCat.setText(customProdCatName)
            Toast.makeText(
                applicationContext,
                binding.stateCustProdCat.getText().toString(),
                Toast.LENGTH_SHORT
            ).show()
            //  apiCustProdSubCat(customProductCatID)
            setCustomProdCat()
        })
    }

    fun addDataList() {
        val params = Utility.getParmMap()
        params["catName"] = binding.stateProductCategory.text.toString()
        params["subCatName"] = binding.productSubCategory.text.toString()
        params["product_id"] = productID.toString()
        params["qty"] = binding.editQty.text.toString()
      /*  val multiple = MultipleProductBean(
            binding.stateProductCategory.text.toString(),
            binding.productSubCategory.text.toString(),
            binding.stateProduct.text.toString(),
            productID.toString(),
            binding.editQty.text.toString(), ""
        )*/
  val multiple = LeadProductListBean.Data(
            binding.stateProductCategory.text.toString(),
           0,
            0,0,
            productID,"","",binding.editQty.text.toString(),""

        )


        val multipleList = MultipleProListBean(
            productID.toString(),
            binding.editQty.text.toString()
        )
        list.add(multiple)
        multipleProdlist.add(multipleList)
        Toast.makeText(this@EditLeadActivity, "Product Added Successfully", Toast.LENGTH_SHORT)
            .show()
        binding.editQty.text?.clear()
        Log.d("xzczxcxz", Gson().toJson(list))
        Log.d("xzczxcxz", Gson().toJson(multipleProdlist))


        handleProductList(list)

    }

    fun addDataCustmProdtList() {
        val params = Utility.getParmMap()
        params["catName"] = customProdCatName
        params["subCatName"] = customSubCatName
        params["product_name"] = binding.editCustmProdName.text.toString()
        params["qty"] = binding.editCustmQty.text.toString()
        params["file"] = ""
        val multiple = MultipleProductBean(
            customProdCatName,
            customSubCatName,
            binding.editCustmProdName.text.toString(),
            0.toString(),
            binding.editCustmQty.text.toString(), file2?.absolutePath!!
        )

        cutomProdList.add(multiple)
        val custImg = CustProdImgBean(file2?.absolutePath!!)

        cutomProdImgList.add(custImg)

        Toast.makeText(
            this@EditLeadActivity,
            "Custom Product Added Successfully",
            Toast.LENGTH_SHORT
        )
            .show()
        binding.editCustmQty.text?.clear()

        binding.btnAadharFront.setImageResource(R.drawable.add_file)
        Log.d("xzczxcxz", Gson().toJson(cutomProdList))
        Log.d("xzczxcxz", Gson().toJson(cutomProdImgList))
        cutomFinalList.addAll(cutomProdList)
        cutomFinalList.addAll(cutomProdImgList)
        //  Log.d("saasdf",Gson().toJson(cutomFinalList))
        handleCustomProdList(cutomProdList)

    }

    fun handleProductList(data: List<LeadProductListBean.Data>) {
        binding.rcAllProduct.visibility = View.VISIBLE
        binding.rcAllProduct.layoutManager = LinearLayoutManager(this)
        var mAdapter = EditProductListAdapter(this,  data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, productID: Int) {
                apiLeadProductList("delete", productID, "")
            }
        })
        binding.rcAllProduct.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false

    }

    fun handleCustomProdList(
        data: MutableList<MultipleProductBean>,
    ) {
        binding.rcAllCustmProduct.layoutManager = LinearLayoutManager(this)
        var mAdapter = ProductListAdapter(this, "CustomProduct", data, object :
            RvStatusClickListner {
            override fun clickPos(status: String, pos: Int) {

            }
        })
        binding.rcAllCustmProduct.adapter = mAdapter
        // rvMyAcFiled.isNestedScrollingEnabled = false
    }

    fun ClickPicCamera(CAMERA_PERMISSION_CODE: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_PERMISSION_CODE)
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES
            ),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show()

            } else {
                requestPermission()
            }
        }
    }

    private fun uploadImage(SELECT_PICTURES: Int) {
        val selectImage = Intent()
        selectImage.type = "image/*"
        selectImage.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                selectImage,
                "Select Picture"
            ),
            SELECT_PICTURES
        )
    }

    fun openCameraDialog(SELECT_PICTURES: Int, CAMERA_PERMISSION_CODE: Int) {
        val dialog: Dialog = GeneralUtilities.openBootmSheetDailog(
            R.layout.dialog_camera, R.style.AppBottomSheetDialogTheme,
            this
        )
        val ivClose = dialog.findViewById<ImageView>(R.id.ivClose)
        val llInternalPhoto = dialog.findViewById<View>(R.id.llInternalPhoto) as LinearLayout
        val llClickPhoto = dialog.findViewById<View>(R.id.llClickPhoto) as LinearLayout

        llInternalPhoto.setOnClickListener {
            dialog.dismiss()
            requestPermission()
            uploadImage(SELECT_PICTURES)
        }

        llClickPhoto.setOnClickListener {
            dialog.dismiss()
            requestPermission()
            ClickPicCamera(CAMERA_PERMISSION_CODE)

        }
        ivClose.setOnClickListener { dialog.dismiss() }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURES1) {
                try {
                    val uri = data!!.data
                    val picturePath: String = getPath(
                        applicationContext, uri
                    )
                    Log.d("Picture Path", picturePath)
                    file2 = File(picturePath)
                    //  selectedImageUri2 = uri
                    //  attatchmentStr2 = file2!!.absolutePath
                    val myBitmap = BitmapFactory.decodeFile(file2!!.absolutePath)
                    binding.btnAadharFront.setImageBitmap(myBitmap)
                    //Toast.makeText(getContext(), ""+picturePath, Toast.LENGTH_SHORT).show();
                } catch (e: java.lang.Exception) {

                    //    /storage/emulated/0/Pictures/Title (11).jpg
                    Log.e("Path Error", e.toString())
                    Toast.makeText(applicationContext, "" + e, Toast.LENGTH_SHORT).show()
                }
            }


            if (requestCode == CAMERA_PERMISSION_CODE1) {
                try {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.btnAadharFront.setImageBitmap(imageBitmap)
                    val tempUri = GeneralUtilities.getImageUri(applicationContext, imageBitmap)
                    file2 = File(GeneralUtilities.getRealPathFromURII(this, tempUri))

                    Log.e("Path", file2.toString())

                    //Toast.makeText(getContext(), ""+picturePath, Toast.LENGTH_SHORT).show();
                } catch (e: java.lang.Exception) {
                    Log.e("Path Error", e.toString())
                    Toast.makeText(applicationContext, "" + e, Toast.LENGTH_SHORT).show()
                }
            }
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
