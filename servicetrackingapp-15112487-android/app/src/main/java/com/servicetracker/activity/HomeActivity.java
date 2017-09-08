package com.servicetracker.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.servicetracker.R;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.constants.Constant;
import com.servicetracker.constants.Constants;
import com.servicetracker.fragment.ExitingUserFragment;
import com.servicetracker.fragment.NewUserFragment;
import com.servicetracker.model.ServiceResponse;

/**
 * Created by pushpender.singh on 1/12/15.
 */
public class HomeActivity extends Activity implements View.OnClickListener{
    private Context context;
    private TextView tvNew,tvExiting;
    private FragmentManager fm;
    private Intent intent;
    private  Fragment fragment;
    private ImageView ivLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=HomeActivity.this;
        getIds();
        setListners();
        fragment = new NewUserFragment();
        getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
    }
    private void getIds() {
        tvNew=(TextView)findViewById(R.id.tvNew);
        tvExiting=(TextView)findViewById(R.id.tvExiting);
        ivLogout=(ImageView)findViewById(R.id.ivLogout);
    }
    private void setListners() {
        tvNew.setOnClickListener(this);
        tvExiting.setOnClickListener(this);
        ivLogout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNew:
                tvExiting.setBackgroundResource(R.drawable.tab_unselected_left);
                tvNew.setBackgroundResource(R.drawable.tab_selected_left);
                tvNew.setTextColor(getResources().getColor(R.color.white));
                tvExiting.setTextColor(getResources().getColor(R.color.black));
                fragment = new NewUserFragment();
                getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                break;
            case R.id.tvExiting:
                tvExiting.setBackgroundResource(R.drawable.tab_selected_right);
                tvNew.setBackgroundResource(R.drawable.tab_unselected_right);
                tvNew.setTextColor(getResources().getColor(R.color.black));
                tvExiting.setTextColor(getResources().getColor(R.color.white));
                fragment = new ExitingUserFragment();
                getFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
                break;
            case R.id.ivLogout:
                dialogLogout();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        exitFromApp();
    }
    public void exitFromApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {}
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void dialogLogout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog mDialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.dimAmount = 0.75f;
        mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow();
        View dialoglayout = inflater.inflate(R.layout.dialog_sign_out, null);
        mDialog.setContentView(dialoglayout);
        mDialog.show();
        TextView btnCancel=(TextView) mDialog.findViewById(R.id.btnCancel);
        TextView btnSignOut=(TextView) mDialog.findViewById(R.id.btnSignOut);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if(CommonUtils.isOnline(HomeActivity.this)){
                    logoutApi();
                }else{
                    intent=new Intent(HomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    CommonUtils.saveStringPreferences(HomeActivity.this, Constants.LOGIN, "");
                    CommonUtils.saveStringPreferences(HomeActivity.this, Constants.USERID,"");
                }
            }
        });
    }
    private void logoutApi() {
        final ProgressDialog progressDialog = ProgressDialog.show(HomeActivity.this, "", getString(R.string.please_wait));
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("userID", CommonUtils.getStringPreferences(HomeActivity.this, Constants.USERID));
        requestJson.addProperty("deviceToken", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        requestJson.addProperty("deviceType", Constant.DEVICETYPE);
        Log.e("request", "request" + requestJson);
        Ion.with(HomeActivity.this)
                .load(Constant.LOGOUT)
                .addHeader(Constant.AUTHORIZATION, "Basic " + Base64.encodeToString(("serviceTrackingApp" + ":" + "@1!2@3#QWER#").getBytes(), Base64.NO_WRAP))
                .setJsonObjectBody(requestJson)
                .as(new TypeToken<ServiceResponse>() {
                }).setCallback(new FutureCallback<ServiceResponse>() {
            @Override
            public void onCompleted(Exception e, final ServiceResponse result) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                String resultLocal = "";
                try {
                    Gson gson = new Gson();
                    resultLocal = gson.toJson(result, ServiceResponse.class);
                    Log.e("response", "response" + resultLocal);
                } catch (Exception e2) {
                }
                if (result != null) {
                    if (result.responseCode != null&&result.responseCode.equals("200")) {
                        intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        CommonUtils.saveStringPreferences(HomeActivity.this, Constants.LOGIN, "");
                        CommonUtils.saveStringPreferences(HomeActivity.this, Constants.USERID, "");
                    } else {
                        Toast.makeText(HomeActivity.this, result.responseMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, getString(R.string.server_not_responding), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
