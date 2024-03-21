package remotex.com.remotewebview.additionalSettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import remotex.com.remotewebview.databinding.ActivityDonwloadPageBinding


class DonwloadPageActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityDonwloadPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonwloadPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.closeBs.setOnClickListener {
            onBackPressed()
        }

    }
}