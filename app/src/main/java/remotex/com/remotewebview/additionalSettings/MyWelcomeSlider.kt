package remotex.com.remotewebview.additionalSettings

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import remotex.com.remotewebview.databinding.ActivityMyWelcomeSliderBinding
import java.io.File
import android.widget.Toast


class MyWelcomeSlider : AppCompatActivity() {

    private lateinit var binding: ActivityMyWelcomeSliderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWelcomeSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val destinationFolder = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Syn2AppLive/song.mp4")

        if (destinationFolder.exists()) {
            // play video
            binding.splashView.setVideoURI(Uri.parse(destinationFolder.toString()))
            binding.splashView.start()
        } else {
            val videoURL = "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"

            binding.splashView.setVideoURI(Uri.parse(videoURL))
            binding.splashView.start()
        }

        // Set completion listener
        binding.splashView.setOnCompletionListener {
            // Show toast when video finishes playing
            Toast.makeText(this@MyWelcomeSlider, "Video Finished", Toast.LENGTH_SHORT).show()
        }
    }
}
