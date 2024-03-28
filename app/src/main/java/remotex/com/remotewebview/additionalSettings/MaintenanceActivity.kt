package remotex.com.remotewebview.additionalSettings

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import remotex.com.remotewebview.MyApplication
import remotex.com.remotewebview.SettingsActivity
import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityMaintenanceBinding
import remotex.com.remotewebview.databinding.CustomCrashReportBinding

class MaintenanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaintenanceBinding



    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editor = sharedBiometric.edit()
        MyApplication.incrementRunningActivities()



        // show online Status

        binding.apply {


            imagShowOnlineStatus.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {
                    editor.remove(Constants.imagShowOnlineStatus)
                    editor.apply()
                    binding.textShowOnlineStatus.text = "Show Online Status Indicator"

                } else {
                    editor.putString(Constants.imagShowOnlineStatus, "imagShowOnlineStatus")
                    editor.apply()
                    binding.textShowOnlineStatus.text = "Hide Online Status Indicator"
                }
            }




            val get_indicator_satate = sharedBiometric.getString(Constants.img_Make_OnlineIndicator_Default_visible, "")

            if (get_indicator_satate.isNullOrEmpty()){
                editor.putString(Constants.img_Make_OnlineIndicator_Default_visible, "img_Make_OnlineIndicator_Default_visible")
                editor.remove(Constants.imagShowOnlineStatus)
                editor.apply()
                binding.textShowOnlineStatus.text = "Show Online Status Indicator"
                binding.imagShowOnlineStatus.isChecked = true

            }else{

                val get_imagShowOnlineStatus = sharedBiometric.getString(Constants.imagShowOnlineStatus, "")

                imagShowOnlineStatus.isChecked = get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)

                if (get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)) {

                    binding.textShowOnlineStatus.text = "Hide Online Status Indicator"
                    binding.imagShowOnlineStatus.isChecked = false

                } else {
                    binding.textShowOnlineStatus.text = "Show Online Status Indicator"
                    binding.imagShowOnlineStatus.isChecked = true


                }



            }


        }







        binding.apply {


            val editor = sharedBiometric.edit()


            imagEnableDownloadStatus.setOnCheckedChangeListener { compoundButton, isValued -> // we are putting the values into SHARED PREFERENCE
                if (compoundButton.isChecked) {

                    editor.putString(Constants.showDownloadSyncStatus, "showDownloadSyncStatus")
                    editor.apply()
                    binding.textCheckDownloadStatus2.text = "Show Download Status"

                } else {
                    editor.remove(Constants.showDownloadSyncStatus)
                    editor.apply()
                    binding.textCheckDownloadStatus2.text = "Hide Download Status"
                }
            }


            val get_imagEnableDownloadStatus = sharedBiometric.getString(Constants.showDownloadSyncStatus, "")


            imagEnableDownloadStatus.isChecked = get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)

            if (get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)) {

                binding.textCheckDownloadStatus2.text = "Show Download Status"

            } else {

                binding.textCheckDownloadStatus2.text = "Hide Download Status"

            }




            closeBs.setOnClickListener {

                val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")

                if (get_navigationS2222.equals(Constants.SettingsPage)) {
                    val intent = Intent(applicationContext, SettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
                    val intent =
                        Intent(applicationContext, AdditionalSettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }






        }


    }



    override fun onDestroy() {
        super.onDestroy()
        try {
            MyApplication.decrementRunningActivities()
        } catch (ignored: java.lang.Exception) {
        }
    }


    override fun onBackPressed() {
        val get_navigationS2222 = sharedBiometric.getString(Constants.SAVE_NAVIGATION, "")

        if (get_navigationS2222.equals(Constants.SettingsPage)) {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        } else if (get_navigationS2222.equals(Constants.AdditionNalPage)) {
            val intent =
                Intent(applicationContext, AdditionalSettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}