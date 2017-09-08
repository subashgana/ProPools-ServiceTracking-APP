package com.servicetracker;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import com.servicetracker.activity.HomeActivity;
import com.servicetracker.activity.LoginActivity;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.constants.Constants;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends Activity {
    private Context context;
    private String TAG = SplashActivity.class.getSimpleName();
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("com.propools",
                            PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Key Hash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                    if (CommonUtils.getStringPreferences(SplashActivity.this,Constants.LOGIN).equalsIgnoreCase("1")) {
                            intent = new Intent(SplashActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
