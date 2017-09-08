package com.servicetracker.constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.servicetracker.R;

public class CommonUtils {
    private static ProgressDialog dialogProgress;

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method for checking Internet Connection
     */

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        return !(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable());
    }

    /**
     * Method for checking text that is it a valid mail address or not
     */
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
        //  return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getStringPreferences(Activity context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static String getStringPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static void saveStringPreferences(Context context, String key,
                                             String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferences(Activity context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");

    }

    public static void showAlert(String message, Activity context) {
        AlertDialog.Builder emailAlert = new AlertDialog.Builder(context);
        emailAlert.setMessage(message);
        emailAlert.setNeutralButton(context.getString(R.string.ok), null);
        try {

            AlertDialog dialog = emailAlert.show();
            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertTitle(String title, String message,
                                      final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void disMissProgressDialog(Context context) {
        if (context != null && dialogProgress != null) {
            dialogProgress.dismiss();
            dialogProgress = null;
        }
    }

    public static void showProgressDialog(Context context) {
        if (context != null) {
            dialogProgress = new ProgressDialog(context);
            dialogProgress.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //   dialogProgress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.my_progress_indeterminate));
            dialogProgress.setMessage(context.getResources().getString(R.string.loading));
            if (!dialogProgress.isShowing()) {
                try {
                    dialogProgress.show();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
            dialogProgress.setCancelable(false);

        }
    }

}