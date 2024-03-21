package remotex.com.remotewebview.additionalSettings.scanutil;

import android.content.Intent;

import com.journeyapps.barcodescanner.CaptureActivity;

import remotex.com.remotewebview.R;
import remotex.com.remotewebview.WebActivity;

public class CaptureAct extends CaptureActivity

{

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        startActivity(intent);
        finish();
    }
}
