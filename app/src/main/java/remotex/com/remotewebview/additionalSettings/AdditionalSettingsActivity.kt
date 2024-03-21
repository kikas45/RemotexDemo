package remotex.com.remotewebview.additionalSettings

import android.content.Context
import android.content.Intent

import android.content.SharedPreferences

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import remotex.com.remotewebview.MyApplication
import remotex.com.remotewebview.SettingsActivity

import remotex.com.remotewebview.additionalSettings.utils.Constants
import remotex.com.remotewebview.databinding.ActivityAppAdminBinding

class AdditionalSettingsActivity : AppCompatActivity() {



    private val sharedBiometric: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            Constants.SHARED_BIOMETRIC,
            Context.MODE_PRIVATE
        )
    }




    private lateinit var binding: ActivityAppAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyApplication.incrementRunningActivities()




        binding.apply {


            textSyncManager.setOnClickListener {

                val editor = sharedBiometric.edit()
                val intent = Intent(applicationContext, ReSyncActivity::class.java)
                startActivity(intent)
                finish()

                editor.putString(Constants.SAVE_NAVIGATION, Constants.AdditionNalPage)
                editor.apply()


            }







            textMaintencePage.setOnClickListener {
                val intent = Intent(applicationContext, MaintenanceActivity::class.java)
                startActivity(intent)
                finish()

                val editor = sharedBiometric.edit()
                editor.putString(Constants.SAVE_NAVIGATION, Constants.AdditionNalPage)
                editor.apply()

            }



            closeBs.setOnClickListener {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                finish()

            }


        }


    }


    override fun onBackPressed() {

        val intent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.decrementRunningActivities()

    }



}