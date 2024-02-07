package sync2app.com.syncapplive.additionalSettings


import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import sync2app.com.syncapplive.MyApplication
import sync2app.com.syncapplive.SettingsActivity
import sync2app.com.syncapplive.WebActivity
import sync2app.com.syncapplive.additionalSettings.DownloadsArray.list.DownlodPagger
import sync2app.com.syncapplive.additionalSettings.myService.NotificationService
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.SavedHistoryListAdapter
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.User
import sync2app.com.syncapplive.additionalSettings.savedDownloadHistory.UserViewModel
import sync2app.com.syncapplive.additionalSettings.urlchecks.checkUrlExistence
import sync2app.com.syncapplive.additionalSettings.urlchecks.isUrlValid
import sync2app.com.syncapplive.additionalSettings.utils.Constants
import sync2app.com.syncapplive.databinding.ActivitySyncPowellBinding
import sync2app.com.syncapplive.databinding.CustomContinueDownloadLayoutBinding
import sync2app.com.syncapplive.databinding.CustomDefinedTimeIntervalsBinding
import sync2app.com.syncapplive.databinding.CustomSavedHistoryLayoutBinding
import sync2app.com.syncapplive.databinding.CustomSelectLauncOrOfflinePopLayoutBinding
import sync2app.com.syncapplive.databinding.CustomSetUpTimerOptionLayoutBinding
import sync2app.com.syncapplive.databinding.ProgressDialogLayoutBinding
import java.io.File
import java.util.Objects


class ReSyncActivity : AppCompatActivity(), SavedHistoryListAdapter.OnItemClickListener {
    private lateinit var binding: ActivitySyncPowellBinding

    private val mUserViewModel by viewModels<UserViewModel>()

    private val adapter by lazy {
        SavedHistoryListAdapter(this, this)
    }

    private lateinit var customProgressDialog: Dialog
    private lateinit var customSavedDownloadDialog: Dialog


    //  private var randNumber = "" + System.currentTimeMillis()


    private val baseUrl222 =
        "https://firebasestorage.googleapis.com/v0/b/vector-news-b5fcf.appspot.com/o/testAPKs%2FMyZip.zip?alt=media&token=5f890c03-d2d5-4f97-95c7-e39c8dc49c57"


    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.READ_MEDIA_IMAGES

        )
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }


    private var urlIndex =
        "https://cp.cloudappserver.co.uk/app_base/public/CLO/DE_MO_2021000/Zip/App.zip"
    private var urlIndexCPI =
        "https://cp.cloudappserver.co.uk/app_base/public/CLO/DE_MO_2021000/Zip/App.zip"
    private var urlIndexAPI =
        "https://firebasestorage.googleapis.com/v0/b/vector-news-b5fcf.appspot.com/o/testAPKs%2FMyZip.zip?alt=media&token=5f890c03-d2d5-4f97-95c7-e39c8dc49c57"


    private var fil_CLO = ""
    private var fil_DEMO = ""
    private var fil_baseUrl = ""

    private var getUrlBasedOnSpinnerText = ""
    private var API_Server = "API-Cloud App Server"
    private var CP_server = "CP-Cloud App Server"
    private var Saved_Download = "Saved Dwonload"


    private var Selected_time = "Selected time"

    private var rootfolder = "Downloads"


    private var getTimeDefined_Prime = ""
    private var timeMinuetesDefined = "Sync interval timer"
    private var timeMinuetes22 = "2 Minutes"
    private var timeMinuetes55 = "5 Minutes"
    private var timeMinuetes10 = "10 Minutes"
    private var timeMinuetes15 = "15 Minutes"
    private var timeMinuetes30 = "30 Minutes"
    private var timeMinuetes60 = "60 Minutes"
    private var timeMinuetes120 = "120 Minutes"
    private var timeMinuetes180 = "180 Minutes"
    private var timeMinuetes240 = "240 Minutes"


    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    private val myDownloadClass: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.MY_DOWNLOADER_CLASS,
            Context.MODE_PRIVATE
        )
    }


    var manager: DownloadManager? = null

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val myInterVals: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val myHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    var preferences: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        preferences = PreferenceManager.getDefaultSharedPreferences(this)
//        if (preferences!!.getBoolean("darktheme", false)) {
//            setTheme(R.style.DarkThemeSettings)
//        }

        binding = ActivitySyncPowellBinding.inflate(layoutInflater)
        setContentView(binding.root)


        manager = applicationContext.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?

        MyApplication.incrementRunningActivities()

        binding.apply {

            val getSavedCLOImPutFiled =
                myDownloadClass.getString(Constants.getSavedCLOImPutFiled, "")
            val getSaveSubFolderInPutFiled =
                myDownloadClass.getString(Constants.getSaveSubFolderInPutFiled, "")
            val getSavedEditTextInputSynUrlZip =
                myDownloadClass.getString(Constants.getSavedEditTextInputSynUrlZip, "")

            if (!getSavedCLOImPutFiled.isNullOrEmpty()) {
                editTextCLOpath.setText(getSavedCLOImPutFiled)
            }

            if (!getSaveSubFolderInPutFiled.isNullOrEmpty()) {
                editTextSubPathFolder.setText(getSaveSubFolderInPutFiled)
            }


            if (!getSavedEditTextInputSynUrlZip.isNullOrEmpty()) {
                editTextInputSynUrlZip.setText(getSavedEditTextInputSynUrlZip)
            }

            initViewTooggle()


            binding.textTitle.setOnClickListener {
                applicationContext.stopService(
                    Intent(
                        applicationContext,
                        NotificationService::class.java
                    )
                )
            }



            textTestConnectionAPPer.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)
                try {
                    testConnectionSetup()
                } catch (_: Exception) {
                }
            }


            textDownloadZipSyncOrApiSyncNow.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (checkMultiplePermission()) {
                    doOperation()
                }

            }

            textLauncheSaveDownload.setOnClickListener {
                showPopForLaunch_Oblin_offline()
            }

            closeBs.setOnClickListener {

                val getStateNaviagtion = sharedBiometric.getString(Constants.CALL_RE_SYNC_MANGER, "")

                val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")


                val editor = sharedBiometric.edit()
                if (getStateNaviagtion.equals(Constants.CALL_RE_SYNC_MANGER)) {
                    editor.remove(Constants.CALL_RE_SYNC_MANGER)
                    editor.apply()
                    val intent = Intent(applicationContext, WebActivity::class.java)
                    startActivity(intent)
                    finish()


                } else {

                    if (get_navigationS2222.equals(Constants.SettingsPage)) {
                        val intent = Intent(applicationContext, SettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
                        val intent =
                            Intent(applicationContext, AdditionalSettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if (get_navigationS2222.equals(Constants.WebViewPage)) {
                        val intent = Intent(applicationContext, WebActivity::class.java)
                        startActivity(intent)
                        finish()
                    }


                }


            }


        }


    }

    override fun onBackPressed() {


        val getStateNaviagtion = sharedBiometric.getString(Constants.CALL_RE_SYNC_MANGER, "")

        val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")


        val editor = sharedBiometric.edit()
        if (getStateNaviagtion.equals(Constants.CALL_RE_SYNC_MANGER)) {
            editor.remove(Constants.CALL_RE_SYNC_MANGER)
            editor.apply()
            val intent = Intent(applicationContext, WebActivity::class.java)
            startActivity(intent)
            finish()

        } else {

            if (get_navigationS2222.equals(Constants.SettingsPage)) {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                finish()
            } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
                val intent = Intent(applicationContext, AdditionalSettingsActivity::class.java)
                startActivity(intent)
                finish()
            } else if (get_navigationS2222.equals(Constants.WebViewPage)) {
                val intent = Intent(applicationContext, WebActivity::class.java)
                startActivity(intent)
                finish()
            }


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.decrementRunningActivities()
    }

    override fun onStart() {
        super.onStart()

    }

    private fun showCustomProgressDialog(message: String) {
        try {
            customProgressDialog = Dialog(this)
            val binding = ProgressDialogLayoutBinding.inflate(LayoutInflater.from(this))
            customProgressDialog.setContentView(binding.root)
            customProgressDialog.setCancelable(true)
            customProgressDialog.setCanceledOnTouchOutside(false)
            customProgressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            binding.textLoading.text = message

            customProgressDialog.show()
        } catch (_: Exception) {
        }
    }


    private fun testConnectionSetup() {
        binding.apply {
            val getFolderClo = editTextCLOpath.text.toString().trim()
            val getFolderSubpath = editTextSubPathFolder.text.toString().trim()

            val editor = myDownloadClass.edit()
            if (isNetworkAvailable()) {
                if (!imagSwtichEnableManualOrNot.isChecked && isNetworkAvailable()) {
                    when (getUrlBasedOnSpinnerText) {
                        CP_server -> {
                            if (getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty()) {
                                httpNetworkTester(getFolderClo, getFolderSubpath)
                                editor.putString(Constants.getSavedCLOImPutFiled, getFolderClo)
                                editor.putString(
                                    Constants.getSaveSubFolderInPutFiled,
                                    getFolderSubpath
                                )
                                editor.apply()

                                // save also to room data base
                                val user =
                                    User(
                                        CLO = getFolderClo,
                                        DEMO = getFolderSubpath,
                                        EditUrl = ""
                                    )
                                mUserViewModel.addUser(user)

                            } else {
                                editTextCLOpath.error = "Input a valid path e.g CLO"
                                editTextSubPathFolder.error =
                                    "Input a valid path e.g DE_MO_2021000"
                                showToastMessage("Fields can not be empty")
                            }
                        }

                        API_Server -> {
                            showToastMessage("No Logic For API Server Yet")
                        }

                    }

                }
                //// enf of the main if
            } else {
                showToastMessage("No Internet Connection")
            }


            /// when the button is checked
            if (isNetworkAvailable()) {
                if (imagSwtichEnableManualOrNot.isChecked && isNetworkAvailable()) {
                    when (getUrlBasedOnSpinnerText) {
                        CP_server -> {

                            val editInputUrl = editTextInputSynUrlZip.text.toString().trim()
                            if (editInputUrl.isNotEmpty() && isUrlValid(editInputUrl)) {
                                httpNetSingleUrlTest(editInputUrl)
                                editor.putString(
                                    Constants.getSavedEditTextInputSynUrlZip,
                                    editInputUrl
                                )
                                editor.apply()
                                val user = User(CLO = "", DEMO = "", EditUrl = editInputUrl)
                                mUserViewModel.addUser(user)
                            } else {
                                showToastMessage("Invalid url format")
                                binding.editTextInputSynUrlZip.error = "Invalid url format"
                            }

                        }

                        API_Server -> {
                            showToastMessage("No Logic For API Server Yet")
                        }

                    }
                }
            } else {
                showToastMessage("No Internet Connection")
            }

        }
    }


    private fun httpNetworkTester(getFolderClo: String, getFolderSubpath: String) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Testing connection")


            val editor = myDownloadClass.edit()

            if (binding.imagSwtichEnableSyncFromAPI.isChecked) {
                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Zip/App.zip"
                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Successful"
                            )
                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                        }
                    } finally {
                        customProgressDialog.dismiss()

                    }
                }

            } else {


                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Api/update.csv"

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Successful"
                            )
                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                        }
                    } finally {
                        customProgressDialog.dismiss()
                    }
                }


            }


        }, 300)
    }


    private fun httpNetSingleUrlTest(baseUrl33: String) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Testing connection")


            val lastString = baseUrl33.substringAfterLast("/")
            val fileNameWithoutExtension = lastString.substringBeforeLast(".")

            lifecycleScope.launch {
                try {
                    val result = checkUrlExistence(baseUrl33)
                    if (result) {
                        showPopsForMyConnectionTest(
                            "CLO",
                            fileNameWithoutExtension,
                            "Successful"
                        )
                    } else {

                        showPopsForMyConnectionTest("CLO", fileNameWithoutExtension, "Failed!")
                    }
                } finally {
                    customProgressDialog.dismiss()
                }
            }

        }, 300)
    }


    private fun initViewTooggle() {
        binding.apply {

            //for history

            constraintLayoutSavedDwonlaod.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)

                showSaveduserHistory()


            }


            // for server
            getUrlBasedOnSpinnerText = CP_server
            constraintLayout4.setOnClickListener {
                hideKeyBoard(binding.editTextInputSynUrlZip)

                serVerOptionDialog()

            }


            //// inilaizing the Set Syn Timmer
            // for time intervals
            // getTimeDefined = timeMinuetesDefined

            initSyncTimmer()



            textIntervalsSelect.setOnClickListener {

                definedTimeIntervals()
            }


            textView12.setOnClickListener {
                setUpCustomeTimmer()
            }


            val imgLunchOnline =
                sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "")
            imagSwtichEnableLaucngOnline.isChecked =
                imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)

            val editor333 = myDownloadClass.edit()

            if (imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)) {
                textLunchOnline.setText("Launch online")
                editor333.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOnline)
                editor333.apply()


            } else {

                textLunchOnline.setText("Launch offline")
                editor333.putString(
                    Constants.Tapped_OnlineORoffline,
                    Constants.tapped_launchOffline
                )
                editor333.apply()
            }

            imagSwtichEnableLaucngOnline.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")
                    editor.apply()

                    textLunchOnline.setText("Launch online")

                    editor333.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOnline
                    )
                    editor333.apply()

                } else {

                    editor.remove("imgAllowLunchFromOnline")
                    editor.apply()

                    textLunchOnline.setText("Launch offline")

                    editor333.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOffline
                    )
                    editor333.apply()

                }
            }


            /// use first Sync  or Do not use First Sync

            // enable satrt file for first synct

            val startFileFirstSync =
                sharedBiometric.getString(Constants.imgStartFileFirstSync, "")
            imgStartFileFirstSync.isChecked =
                startFileFirstSync.equals(Constants.imgStartFileFirstSync)


            if (startFileFirstSync.equals(Constants.imgStartFileFirstSync)) {
                textUseStartFile.setText("Use start file on")
            } else {
                textUseStartFile.setText("Use start file off")
            }


            imgStartFileFirstSync.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imgStartFileFirstSync, Constants.imgStartFileFirstSync
                    )
                    editor.apply()
                    textUseStartFile.setText("Use start file on")

                } else {

                    editor.remove(Constants.imgStartFileFirstSync)
                    editor.apply()
                    textUseStartFile.setText("Use start file off")

                }
            }


            // enable config

            val imgLCongigFile =
                sharedBiometric.getString(Constants.imagSwtichEnableConfigFileOnline, "")
            imagSwtichEnableConfigFileOnline.isChecked =
                imgLCongigFile.equals(Constants.imagSwtichEnableConfigFileOnline)


            if (imgLCongigFile.equals(Constants.imagSwtichEnableConfigFileOnline)) {
                textConfigfileOnline.setText("Config File Offline")
            } else {
                textConfigfileOnline.setText("Config File Online")
            }


            imagSwtichEnableConfigFileOnline.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableConfigFileOnline,
                        "imagSwtichEnableConfigFileOnline"
                    )
                    editor.apply()
                    textConfigfileOnline.setText("Config File Offline")
                } else {

                    editor.remove("imagSwtichEnableConfigFileOnline")
                    editor.apply()

                    textConfigfileOnline.setText("Config File Online")
                }
            }


            // enable Sync on File Change

            val imgEnableFileOnSyncChange =
                sharedBiometric.getString(Constants.imagSwtichEnableSyncOnFilecahnge, "")
            imagSwtichEnableSyncOnFilecahnge.isChecked =
                imgEnableFileOnSyncChange.equals(Constants.imagSwtichEnableSyncOnFilecahnge)


            if (imgEnableFileOnSyncChange.equals(Constants.imagSwtichEnableSyncOnFilecahnge)) {
                textSyncOnFileChangeIntervals.setText("Download on Intervals")
            } else {
                textSyncOnFileChangeIntervals.setText("Download on change")
                applicationContext.stopService(
                    Intent(
                        applicationContext,
                        NotificationService::class.java
                    )
                )

            }


            imagSwtichEnableSyncOnFilecahnge.setOnCheckedChangeListener { compoundButton, isValued ->
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imagSwtichEnableSyncOnFilecahnge, "imagSwtichEnableSyncOnFilecahnge")
                    editor.putString(Constants.imagEnableDownloadStatus, "imagEnableDownloadStatus")

                    editor.apply()
                    textSyncOnFileChangeIntervals.setText("Download on Intervals")


                } else {

                    editor.remove("imagSwtichEnableSyncOnFilecahnge")
                    editor.remove(Constants.imagEnableDownloadStatus)
                    editor.apply()

                    applicationContext.stopService(
                        Intent(
                            applicationContext,
                            NotificationService::class.java
                        )
                    )

                    textSyncOnFileChangeIntervals.setText("Download on change")
                }
            }


            // enable Toggle Mode

            val imgEnableToggleMode =
                sharedBiometric.getString(Constants.imagSwtichEnablEnableToggleOrNot, "")
            imagSwtichEnablEnableToggleOrNot.isChecked =
                imgEnableToggleMode.equals(Constants.imagSwtichEnablEnableToggleOrNot)

            if (imgEnableToggleMode.equals(Constants.imagSwtichEnablEnableToggleOrNot)) {
                textToogleMode.setText("Disable Test Toggle Mode")
            } else {
                textToogleMode.setText("Enable Test Toggle Mode")
            }

            imagSwtichEnablEnableToggleOrNot.setOnCheckedChangeListener { compoundButton, isValued ->
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnablEnableToggleOrNot,
                        "imagSwtichEnablEnableToggleOrNot"
                    )
                    editor.apply()
                    textToogleMode.setText("Disable Test Toggle Mode")
                } else {

                    editor.remove("imagSwtichEnablEnableToggleOrNot")
                    editor.apply()

                    textToogleMode.setText("Enable Test Toggle Mode")
                }
            }






            funManulOrNotInteView()

            //// logic for Select Partner Url

            val imagPartnerurl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "")
            imagSwtichPartnerUrl.isChecked =
                imagPartnerurl.equals(Constants.imagSwtichPartnerUrl)


            if (imagPartnerurl.equals(Constants.imagSwtichPartnerUrl)) {
                textPartnerUrlLunch.setText("Select Partner Url")
            } else {
                textPartnerUrlLunch.setText("Custom Domain")
            }

            imagSwtichPartnerUrl.setOnCheckedChangeListener { compoundButton, isValued ->
                hideKeyBoard(binding.editTextInputSynUrlZip)
                val editor = sharedBiometric.edit()
                if (compoundButton.isChecked) {
                    editor.putString(Constants.imagSwtichPartnerUrl, "imagSwtichPartnerUrl")
                    editor.apply()
                    textPartnerUrlLunch.setText("Select Partner Url")

                } else {

                    editor.remove("imagSwtichPartnerUrl")
                    editor.apply()
                    textPartnerUrlLunch.setText("Custom Domain")

                }
            }


            // enable satrt file for first synct
            /*    val textSynfromApiZip222 =
                    sharedBiometric.getString(Constants.imagSwtichEnableSyncFromAPI, "")

                if (textSynfromApiZip222.equals(Constants.imagSwtichEnableSyncFromAPI)) {
                    textSynfromApiZip.setText("Use ZIP Sync")
                    textDownloadZipSyncOrApiSyncNow.setText("Connect ZIP Sync")
                } else {
                    textSynfromApiZip.setText("Use API Sync")
                    textDownloadZipSyncOrApiSyncNow.setText("Connect ZIP Sync")
                }
    */

            imagSwtichEnableSyncFromAPI.isChecked = true

            val editor = sharedBiometric.edit()
            editor.putString(
                Constants.imagSwtichEnableSyncFromAPI,
                Constants.imagSwtichEnableSyncFromAPI
            )
            editor.apply()

            imagSwtichEnableSyncFromAPI.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableSyncFromAPI,
                        Constants.imagSwtichEnableSyncFromAPI
                    )
                    editor.apply()
                    textSynfromApiZip.setText("Use ZIP Sync")
                    editTextInputSynUrlZip.setHint("Input url  ZIP Sync")
                    textDownloadZipSyncOrApiSyncNow.setText("Connect ZIP Sync")
                } else {

                    editor.remove(Constants.imagSwtichEnableSyncFromAPI)
                    editor.apply()

                    textSynfromApiZip.setText("Use API Sync")
                    textDownloadZipSyncOrApiSyncNow.setText("Connect API Sync")
                    editTextInputSynUrlZip.setHint("Input url  API Sync")


                }
            }

        }


    }

    private fun initSyncTimmer() {

        val get_savedIntervals = myDownloadClass.getString(Constants.getTimeDefined, "")

        if (get_savedIntervals!!.isNotEmpty()) {

            if (get_savedIntervals.equals(Constants.Sync_interval_timer)){
                binding.textIntervalsSelect.text = "Sync interval timer"
                binding.textDisplaytime.text = "Selected time : 00:55"

            }else{
                binding.textIntervalsSelect.text = get_savedIntervals + "ute"
                binding.textDisplaytime.text = get_savedIntervals  + "ute"

            }

        } else {
            getTimeDefined_Prime = timeMinuetesDefined

            val editor = myDownloadClass.edit()
            editor.putString(Constants.getTimeDefined, Constants.Sync_interval_timer)
            editor.apply()


        }

    }

    private fun showSaveduserHistory() {

        customSavedDownloadDialog = Dialog(this)
        val bindingCm = CustomSavedHistoryLayoutBinding.inflate(LayoutInflater.from(this))
        customSavedDownloadDialog.setContentView(bindingCm.root)
        customSavedDownloadDialog.setCancelable(true)
        customSavedDownloadDialog.setCanceledOnTouchOutside(true)
        customSavedDownloadDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bindingCm.apply {


            textClearAllData.setOnClickListener {
                mUserViewModel.deleteAllUsers()
                customSavedDownloadDialog.dismiss()
            }

            closeBs.setOnClickListener {
                customSavedDownloadDialog.dismiss()
            }


            imageCrossClose.setOnClickListener {
                customSavedDownloadDialog.dismiss()
            }

            textClearAllData.setOnClickListener {
                startActivity(Intent(applicationContext, FileExplorerActivity::class.java))
                customSavedDownloadDialog.dismiss()
            }



            handler.postDelayed(Runnable {
                recyclerSavedDownload.adapter = adapter
                recyclerSavedDownload.layoutManager = LinearLayoutManager(applicationContext)

                mUserViewModel.readAllData.observe(this@ReSyncActivity, Observer { user ->
                    adapter.setData(user)
                    if (user.isNotEmpty()) {
                        textErrorText.visibility = View.GONE
                        textClearAllData.visibility = View.VISIBLE
                    } else {
                        textClearAllData.visibility = View.GONE
                        textErrorText.visibility = View.VISIBLE
                    }
                })
            }, 100)
        }




        customSavedDownloadDialog.show()

    }


    @SuppressLint("InflateParams", "SuspiciousIndentation")
    private fun definedTimeIntervals() {
        val bindingCm: CustomDefinedTimeIntervalsBinding =
            CustomDefinedTimeIntervalsBinding.inflate(
                layoutInflater
            )
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(bindingCm.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bindingCm.apply {


//            textTwoMinutes.setOnClickListener {
//                binding.textIntervalsSelect.text = timeMinuetesDefined
//                binding.textDisplaytime.text = "00:55 Seconds"
//                getTimeDefined = timeMinuetesDefined
//            }

            imageCrossClose.setOnClickListener {
                alertDialog.dismiss()
            }

            closeBs.setOnClickListener {
                alertDialog.dismiss()
            }



            textTwoMinutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes22
                binding.textDisplaytime.text = "2 Minutes"
                getTimeDefined_Prime = timeMinuetes22
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_2min)
                editor.apply()
                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                   // showToastMessage("Service not running")
                }



            }


            text55minutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes55
                binding.textDisplaytime.text = "5 Minutes"
                getTimeDefined_Prime = timeMinuetes55
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_5min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                   // showToastMessage("Service not running")
                }




            }

            text100minutes2.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes10
                binding.textDisplaytime.text = "10 Minutes"
                getTimeDefined_Prime = timeMinuetes10
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_10min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                   // showToastMessage("Service not running")
                }


            }


            text1500minutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes15
                binding.textDisplaytime.text = "15 Minutes"
                getTimeDefined_Prime = timeMinuetes15
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_15min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()

                }else{
                 //   showToastMessage("Service not running")
                }


            }

            text3000minutes2.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes30
                binding.textDisplaytime.text = "30 Minutes"
                getTimeDefined_Prime = timeMinuetes30
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_30min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                  //  showToastMessage("Service not running")
                }




            }

            text6000minutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes60
                binding.textDisplaytime.text = "60 Minutes"
                getTimeDefined_Prime = timeMinuetes60
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_60min)
                editor.apply()


                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                   // showToastMessage("Service not running")
                }




            }


            textOneTwentyMinutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes120
                binding.textDisplaytime.text = "2 Hours"
                getTimeDefined_Prime = timeMinuetes120
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_120min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                 //   showToastMessage("Service not running")
                }





            }

            textOneEightThyMinutes2.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes180
                binding.textDisplaytime.text = "3 hours"
                getTimeDefined_Prime = timeMinuetes180
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_180min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                  //  showToastMessage("Service not running")
                }





            }


            tex24000ThyMinutes.setOnClickListener {
                binding.textIntervalsSelect.text = timeMinuetes240
                binding.textDisplaytime.text = "4 hours"
                getTimeDefined_Prime = timeMinuetes240
                alertDialog.dismiss()

                val editor = myDownloadClass.edit()
                editor.putString(Constants.getTimeDefined, Constants.t_240min)
                editor.apply()

                if (foregroundServiceRunning()) {
                    val intent = Intent(Constants.UpdateTimmer_Reciver)
                    sendBroadcast(intent)

                    val editor = myDownloadClass.edit()
                    editor.remove(Constants.SAVED_CN_TIME)
                    editor.apply()


                }else{
                 //   showToastMessage("Service not running")
                }




            }


        }


        alertDialog.show()
    }


    private fun serVerOptionDialog() {
        val serverOptions = arrayOf(CP_server, API_Server)

        val builder = AlertDialog.Builder(this@ReSyncActivity)
        // builder.setTitle("Choose Server")
        builder.setItems(serverOptions) { dialog, which ->
            when (which) {
                0 -> {
                    // Handle CP-Cloud App Server selection
                    binding.texturlsViews.text = CP_server
                    getUrlBasedOnSpinnerText = CP_server
                }

                1 -> {
                    binding.texturlsViews.text = API_Server
                    getUrlBasedOnSpinnerText = API_Server
                }
            }
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }


    @SuppressLint("InflateParams", "SuspiciousIndentation")
    private fun setUpCustomeTimmer() {
        val bindingCm: CustomSetUpTimerOptionLayoutBinding =
            CustomSetUpTimerOptionLayoutBinding.inflate(
                layoutInflater
            )
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(bindingCm.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bindingCm.apply {


            textCancel.setOnClickListener {
                alertDialog.dismiss()
            }


            textOkayAccept.setOnClickListener {
                val getHourVlaue = editTexthour.text.toString()
                val getHourMinutes = editTextMinutes.text.toString()

                val selectedTime = "$getHourVlaue:$getHourMinutes"
                binding.textDisplaytime.text = selectedTime
                alertDialog.dismiss()
            }


        }

        alertDialog.show()
    }


    private fun funManulOrNotInteView() {
        binding.apply {
            // logic for use manual or not
            val imagUsemanualOrnotuseManual =
                sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "")
            imagSwtichEnableManualOrNot.isChecked =
                imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)


            if (imagUsemanualOrnotuseManual.equals(Constants.imagSwtichEnableManualOrNot)) {
                textUseManual.setText("Use manual")

                editTextInputSynUrlZip.visibility = View.VISIBLE
                editTextInputIndexManual?.visibility = View.VISIBLE
                // for clos
                editTextCLOpath.visibility = View.GONE
                editTextSubPathFolder.visibility = View.GONE


            } else {
                textUseManual.setText("Do not use manual")

                editTextInputSynUrlZip.visibility = View.GONE
                editTextInputIndexManual?.visibility = View.GONE
                // for clos
                editTextCLOpath.visibility = View.VISIBLE
                editTextSubPathFolder.visibility = View.VISIBLE


            }

            imagSwtichEnableManualOrNot.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                val editor = sharedBiometric.edit()
                hideKeyBoard(binding.editTextInputSynUrlZip)
                if (compoundButton.isChecked) {
                    editor.putString(
                        Constants.imagSwtichEnableManualOrNot,
                        "imagSwtichEnableManualOrNot"
                    )
                    editor.apply()
                    textUseManual.setText("Use manual")

                    editTextInputSynUrlZip.visibility = View.VISIBLE
                    editTextInputIndexManual?.visibility = View.VISIBLE
                    // for clos
                    editTextCLOpath.visibility = View.GONE
                    editTextSubPathFolder.visibility = View.GONE


                } else {

                    editor.remove("imagSwtichEnableManualOrNot")
                    editor.apply()
                    textUseManual.setText("Do not use manual")

                    editTextInputSynUrlZip.visibility = View.GONE
                    editTextInputIndexManual?.visibility = View.GONE
                    // for clos
                    editTextCLOpath.visibility = View.VISIBLE
                    editTextSubPathFolder.visibility = View.VISIBLE

                }
            }

        }
    }


    private fun hideKeyBoard(editText: EditText) {
        try {
            editText.clearFocus()
            val imm =
                applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (ignored: java.lang.Exception) {
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun testAndDownLoadZipConnection() {
        binding.apply {
            val getFolderClo = editTextCLOpath.text.toString().trim()
            val getFolderSubpath = editTextSubPathFolder.text.toString().trim()
            val editor = myDownloadClass.edit()

            if (isNetworkAvailable()) {
                if (!imagSwtichEnableManualOrNot.isChecked && isNetworkAvailable()) {
                    when (getUrlBasedOnSpinnerText) {
                        CP_server -> {
                            if (getFolderClo.isNotEmpty() && getFolderSubpath.isNotEmpty()) {
                                httpNetworkDownloadsMultiplePaths(
                                    getFolderClo,
                                    getFolderSubpath
                                )
                                editor.putString(Constants.getSavedCLOImPutFiled, getFolderClo)
                                editor.putString(
                                    Constants.getSaveSubFolderInPutFiled,
                                    getFolderSubpath
                                )
                                editor.apply()

                                // save also to room data base
                                val user =
                                    User(
                                        CLO = getFolderClo,
                                        DEMO = getFolderSubpath,
                                        EditUrl = ""
                                    )
                                mUserViewModel.addUser(user)


                            } else {
                                editTextCLOpath.error = "Input a valid path e.g CLO"
                                editTextSubPathFolder.error =
                                    "Input a valid path e.g DE_MO_2021000"
                                showToastMessage("Fields can not be empty")
                            }
                        }

                        API_Server -> {
                            showToastMessage("No Logic For API Server Yet")
                        }

                    }

                }
                //// enf of the main if
            } else {
                showToastMessage("No Internet Connection")
            }


            /// when the button is checked
            if (isNetworkAvailable()) {
                if (imagSwtichEnableManualOrNot.isChecked && isNetworkAvailable()) {
                    when (getUrlBasedOnSpinnerText) {
                        CP_server -> {

                            val editInputUrl = editTextInputSynUrlZip.text.toString().trim()
                            if (editInputUrl.isNotEmpty() && isUrlValid(editInputUrl)) {
                                httpNetSingleDwonload(editInputUrl)
                                editor.putString(
                                    Constants.getSavedEditTextInputSynUrlZip,
                                    editInputUrl
                                )
                                editor.apply()

                                val user = User(CLO = "", DEMO = "", EditUrl = editInputUrl)
                                mUserViewModel.addUser(user)

                            } else {
                                showToastMessage("Invalid url format")
                                binding.editTextInputSynUrlZip.error = "Invalid url format"
                            }

                        }

                        API_Server -> {
                            showToastMessage("No Logic For API Server Yet")
                        }

                    }
                }
            } else {
                showToastMessage("No Internet Connection")
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun httpNetworkDownloadsMultiplePaths(
        getFolderClo: String,
        getFolderSubpath: String,
    ) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Please wait")

            val editior = myDownloadClass.edit()

            if (binding.imagSwtichEnableSyncFromAPI.isChecked) {

                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Zip/App.zip"

                val syncUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/App/index.html"



                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startMyDownlaodsMutiplesPath(
                                baseUrl,
                                getFolderClo,
                                getFolderSubpath,
                                "Zip",
                                "App.zip",
                                syncUrl
                            )
                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                        }
                    } finally {
                        handler.postDelayed(Runnable {
                            customProgressDialog.dismiss()
                        }, 900)
                    }
                }


            } else {

                val baseUrl =
                    "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/Api/update.csv"



                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startMyDownlaodsMutiplesPath(
                                baseUrl,
                                getFolderClo,
                                getFolderSubpath,
                                "Api",
                                "update.csv",
                                ""
                            )
                        } else {
                            showPopsForMyConnectionTest(
                                getFolderClo,
                                getFolderSubpath,
                                "Failed!"
                            )
                        }
                    } finally {
                        handler.postDelayed(Runnable {
                            customProgressDialog.dismiss()
                        }, 900)
                    }
                }

            }


        }, 300)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun httpNetSingleDwonload(baseUrl: String) {
        handler.postDelayed(Runnable {
            showCustomProgressDialog("Please wait")
            val lastString = baseUrl.substringAfterLast("/")
            val fileNameWithoutExtension = lastString.substringBeforeLast(".")


            if (binding.imagSwtichEnableSyncFromAPI.isChecked) {

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startDownloadSingles(baseUrl, "Zip", "App.zip")
                        } else {
                            showPopsForMyConnectionTest(
                                "CLO",
                                fileNameWithoutExtension,
                                "Failed!"
                            )
                        }
                    } finally {
                        handler.postDelayed(Runnable {
                            customProgressDialog.dismiss()
                        }, 900)
                    }
                }

            } else {

                lifecycleScope.launch {
                    try {
                        val result = checkUrlExistence(baseUrl)
                        if (result) {
                            startDownloadSingles(baseUrl, "Api", "App.zip")
                        } else {

                            showPopsForMyConnectionTest(
                                "CLO",
                                fileNameWithoutExtension,
                                "Failed!"
                            )
                        }
                    } finally {
                        handler.postDelayed(Runnable {
                            customProgressDialog.dismiss()
                        }, 900)
                    }
                }
            }


        }, 300)
    }


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private fun showPopsForMyConnectionTest(
        getFolderClo: String,
        getFolderSubpath: String,
        message: String,
    ) {

        val bindingCM: CustomContinueDownloadLayoutBinding =
            CustomContinueDownloadLayoutBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(bindingCM.root)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(true)


        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        bindingCM.apply {


            val userPath = "Username-$getFolderClo\nlicense-$getFolderSubpath"

            textYourUrlTest.text = userPath

            textSucessful.text = message

            textContinuPassword2.setOnClickListener {
                alertDialog.dismiss()

                binding.apply {

                    if (imagSwtichEnableSyncOnFilecahnge.isChecked) {


                        if (getTimeDefined_Prime == "15 Minutes") {
                            showToastMessage("Preparing Interval Download in $getTimeDefined_Prime")
                        }


                    }
                }

            }

        }


        alertDialog.show()


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startMyDownlaodsMutiplesPath(
        baseUrl: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileName: String,
        syncUrl: String,
    ) {

        val threeFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"


        val Extracted = "Offline_app"



        download(baseUrl, getFolderClo, getFolderSubpath, Zip, fileName, Extracted, threeFolderPath)

        /// similar but used on under second cancel downoad in danwload pager
        val editor = myDownloadClass.edit()
        editor.putString(Constants.getFolderClo, getFolderClo)
        editor.putString(Constants.getFolderSubpath, getFolderSubpath)
        editor.putString("Zip", Zip)
        editor.putString("fileName", fileName)
        editor.putString("Extracted", Extracted)
        editor.putString("baseUrl", baseUrl)


        val url =
            "https://cp.cloudappserver.co.uk/app_base/public/$getFolderClo/$getFolderSubpath/App/index.html"


        if (binding.imagSwtichEnableLaucngOnline.isChecked) {
            editor.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOnline)
            editor.putString(Constants.syncUrl, url)

        } else {
            editor.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOffline)

        }

        editor.apply()


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startDownloadSingles(baseUrl: String, Zip: String, fileNamy: String) {


        val Extracted = "Manual_offline_app"
        val threeFolderPath = "/MANUAL/DEMO/$Zip"

        download(baseUrl, "MANUAL", "DEMO", Zip, fileNamy, Extracted, threeFolderPath)

        val editor = myDownloadClass.edit()
        editor.putString(Constants.getFolderClo, "MANUAL")
        editor.putString(Constants.getFolderSubpath, "DEMO")
        editor.putString("Zip", Zip)
        editor.putString("fileName", fileNamy)
        editor.putString("Extracted", Extracted)
        editor.putString("baseUrl", baseUrl)
        editor.apply()


    }


    private fun showToastMessage(messages: String) {

        try {
            Toast.makeText(applicationContext, messages, Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun doOperation() {

        testAndDownLoadZipConnection()


    }


    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    // here all permission granted successfully
                    doOperation()
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }

                    if (someDenied) {
                        showPermissionDeniedDialog()

                    } else {
                        showPermissionDeniedDialog()
                    }


                }
            }
        }
    }


    private fun showPermissionDeniedDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Permission Required")
        builder.setMessage("Please grant the required permissions in the app settings to proceed.")
        builder.setPositiveButton("Go to Settings") { dialog: DialogInterface?, which: Int ->
            openAppSettings()
            dialog?.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface?, which: Int ->
            showToastMessage("Permission Denied!")
            //  activity.finish()
        }
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun download(
        url: String,
        getFolderClo: String,
        getFolderSubpath: String,
        Zip: String,
        fileNamy: String,
        Extracted: String,
        threeFolderPath: String,
    ) {


        //  val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/"

        val DeleteFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"

        val directoryPath =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive$DeleteFolderPath"
        val file = File(directoryPath)
        delete(file)



        handler.postDelayed(Runnable {

            val finalFolderPath = "/$getFolderClo/$getFolderSubpath/$Zip"
            val Syn2AppLive = "Syn2AppLive"

            val editior = myDownloadClass.edit()
            editior.putString(Constants.getFolderClo, getFolderClo)
            editior.putString(Constants.getFolderSubpath, getFolderSubpath)
            editior.putString("Zip", Zip)
            editior.putString("fileNamy", fileNamy)
            editior.putString("Extracted", Extracted)

            val get_savedIntervals = myDownloadClass.getString(Constants.getTimeDefined, "")

            if (get_savedIntervals!!.isNotEmpty()) {
                editior.putString("getTimeDefined", get_savedIntervals)

            } else {
                editior.putString("getTimeDefined", getTimeDefined_Prime)

            }

            editior.apply()



            val managerDownload = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            val folder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Download/$Syn2AppLive/$finalFolderPath"
            )

            if (!folder.exists()) {
                folder.mkdirs()
            }

            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(fileNamy)
            request.allowScanningByMediaScanner()
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "/$Syn2AppLive/$finalFolderPath/$fileNamy"
            )
            val downloadReferenceMain = managerDownload.enqueue(request)

            val editor = myDownloadClass.edit()
            editor.putLong(Constants.downloadKey, downloadReferenceMain)
            editor.apply()


            val intent = Intent(applicationContext, DownlodPagger::class.java)
            intent.putExtra("baseUrl", url)
            intent.putExtra(Constants.getFolderClo, getFolderClo)
            intent.putExtra(Constants.getFolderSubpath, getFolderSubpath)
            intent.putExtra("Zip", Zip)
            intent.putExtra("fileName", fileNamy)
            intent.putExtra("Extracted", Extracted)

            intent.putExtra("threeFolderPath", threeFolderPath)
            intent.putExtra("baseUrl", url)
            startActivity(intent)
            finish()


        }, 1000)


    }


    fun delete(file: File): Boolean {
        if (file.isFile) {
            return file.delete()
        } else if (file.isDirectory) {
            for (subFile in Objects.requireNonNull(file.listFiles())) {
                if (!delete(subFile)) return false
            }
            return file.delete()
        }
        return false
    }


    override fun onItemClicked(photo: User) {


        val clo = photo.CLO.toString()
        val demo = photo.DEMO
        val editurl = photo.EditUrl

        if (!clo.isNullOrEmpty()) {
            binding.editTextCLOpath.setText(clo)
            fil_CLO = clo

            binding.textLauncheSaveDownload.visibility = View.VISIBLE

        }

        if (!demo.isNullOrEmpty()) {
            binding.editTextSubPathFolder.setText(demo)
            fil_DEMO = demo

            binding.textLauncheSaveDownload.visibility = View.VISIBLE

        }

        if (!editurl.isNullOrEmpty()) {
            binding.editTextInputSynUrlZip.setText(editurl)
            fil_baseUrl = editurl

            binding.textLauncheSaveDownload.visibility = View.VISIBLE
        }

        customSavedDownloadDialog.dismiss()
    }


    @SuppressLint("MissingInflatedId")
    private fun showPopForLaunch_Oblin_offline() {
        val bindingCM: CustomSelectLauncOrOfflinePopLayoutBinding =
            CustomSelectLauncOrOfflinePopLayoutBinding.inflate(
                layoutInflater
            )
        val builder = AlertDialog.Builder(this)
        builder.setView(bindingCM.getRoot())
        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val textLaunchMyOnline: TextView = bindingCM.textLaunchMyOnline
        val textLaunchMyOffline: TextView = bindingCM.textLaunchMyOffline
        val imgCloseDialog: ImageView = bindingCM.imageCrossClose
        val close_bs: ImageView = bindingCM.closeBs

        val userPath = "Username-$fil_CLO\nlicense-$fil_DEMO"

        bindingCM.textDescription.text = userPath


        textLaunchMyOnline.setOnClickListener {

            binding.apply {
                val editor = myDownloadClass.edit()


                textLunchOnline.setText("Launch offline")

                if (fil_CLO.isNotEmpty() && fil_DEMO.isNotEmpty()) {

                    val url =
                        "https://cp.cloudappserver.co.uk/app_base/public/$fil_CLO/$fil_DEMO/App/index.html"

                    imagSwtichEnableLaucngOnline.isChecked = true


                    editor.putString(Constants.imgAllowLunchFromOnline, "imgAllowLunchFromOnline")

                    editor.putString(Constants.getFolderClo, fil_CLO)
                    editor.putString(Constants.getFolderSubpath, fil_DEMO)
                    editor.putString(Constants.syncUrl, url)
                    editor.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOnline
                    )


                    editor.apply()

                    val intent = Intent(applicationContext, WebActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    showToastMessage("Select Saved Download Path")
                }





                alertDialog.dismiss()
            }

        }



        textLaunchMyOffline.setOnClickListener {

            binding.apply {
                val editor = myDownloadClass.edit()

                if (fil_CLO.isNotEmpty() && fil_DEMO.isNotEmpty()) {

                    val url =
                        "https://cp.cloudappserver.co.uk/app_base/public/$fil_CLO/$fil_DEMO/App/index.html"

                    imagSwtichEnableLaucngOnline.isChecked = true

                    editor.putString(
                        Constants.imgAllowLunchFromOnline,
                        "imgAllowLunchFromOnline"
                    )

                    editor.putString(Constants.getFolderClo, fil_CLO)
                    editor.putString(Constants.getFolderSubpath, fil_DEMO)
                    // editor.putString(Constants.PASS_URL, url)
                    editor.putString(Constants.syncUrl, url)
                    editor.putString(
                        Constants.Tapped_OnlineORoffline,
                        Constants.tapped_launchOffline
                    )

                    editor.apply()

                    textLunchOnline.setText("Launch online")

                    val intent = Intent(applicationContext, WebActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    showToastMessage("Select Saved Download Path")


                }

                alertDialog.dismiss()
            }
        }


        imgCloseDialog.setOnClickListener { alertDialog.dismiss() }

        close_bs.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }



    fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (NotificationService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }



}
