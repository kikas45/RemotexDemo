package remotex.com.remotewebview.additionalSettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import remotex.com.remotewebview.R
import remotex.com.remotewebview.databinding.ActivityDisplayCsvFilesBinding
import remotex.com.remotewebview.databinding.ActivityMyTestDownloadApiBinding
import remotex.com.remotewebview.databinding.ActivityMyWelcomeSliderBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DisplayCSvFiles : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayCsvFilesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayCsvFilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            closeBs.setOnClickListener {
                onBackPressed()
            }


                    lifecycleScope.launch(Dispatchers.IO) {
                        val csvData = downloadCSV("CLO", "DE_MO_2021001")

                        runOnUiThread {
                            textDisplayText.text = csvData
                        }
                    }


        }


    }

    private fun downloadCSV(clo: String, demo: String): String {
        val stringBuilder = StringBuilder()
        try {
            val downloadUrl =
                "https://cloudappserver.co.uk/cp/app_base/public/$clo/$demo/Api/update.csv"
            Log.d("downloadUrl", "downloadUrl: $downloadUrl")

            val url = URL(downloadUrl)
            val connection = url.openConnection() as HttpURLConnection
            val code = connection.responseCode

            if (code == 200) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")

                }
            } else {
                Log.e("CSVReadError", "Response code: $code")
            }
        } catch (e: Exception) {
            Log.e("CSVReadError", "Error reading CSV: ${e.message}")
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }


}