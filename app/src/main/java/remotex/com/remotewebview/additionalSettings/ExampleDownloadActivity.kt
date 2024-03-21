package remotex.com.remotewebview.additionalSettings

import android.app.DownloadManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import remotex.com.remotewebview.R
import remotex.com.remotewebview.databinding.ActivityDonwloadPageBinding
import remotex.com.remotewebview.databinding.ActivityExampleDownloadBinding

class ExampleDownloadActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityExampleDownloadBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExampleDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {

            btnDownload1111.setOnClickListener {

                val  masterUrl = "https://cp.cloudappserver.co.uk/app_base/public/CLO/DE_MO_2021000/App/index.html"

                val main = "Syn2AppLive"
                val path = "/$main/CLO/DE_MO_2021000/App/index.html"

                val fileNamy = "index.html"

                download(masterUrl, path, fileNamy)
            }

            btnDownload222.setOnClickListener {

                val  masterUrl = "https://cp.cloudappserver.co.uk/app_base/public/CLO/DE_MO_2021000/App/Config/app_background.png"

                val main = "Syn2AppLive"
                val path = "/$main/CLO/DE_MO_2021000/App/Config/app_background.png"

                val fileNamy = "app_background.png"

                download(masterUrl, path, fileNamy)
            }

            btnDownload333.setOnClickListener {

                val  masterUrl = "https://cp.cloudappserver.co.uk/app_base/public/CLO/DE_MO_2021000/App/Schedules/onlineSchedules.csv"

                val main = "Syn2AppLive"
                val path = "/$main/CLO/DE_MO_2021000/App/Schedules/onlineSchedules.csv"

                val fileNamy = "onlineSchedules.csv"

                download(masterUrl, path, fileNamy)
            }


        }


    }


    private fun download(masterUrl: String, path: String, fileNamy: String) {
        showToastMessage(path)

        val request = DownloadManager.Request(Uri.parse(masterUrl))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(fileNamy)
        request.allowScanningByMediaScanner()
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, path)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun showToastMessage(message:String){
        try {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }catch (_:Exception){}
    }





}