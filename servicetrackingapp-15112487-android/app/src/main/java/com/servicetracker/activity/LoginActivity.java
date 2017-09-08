package com.servicetracker.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.servicetracker.database.DatabaseHandler;
import com.servicetracker.model.ServiceResponse;
import com.servicetracker.model.UserListModel;

import java.util.List;
import java.util.TimeZone;

public class LoginActivity extends Activity implements View.OnClickListener {
    private Intent intent;
    private EditText etEmail,etPassword;
    private TextView tvLogin;
    private CheckBox cbRemember;
    private DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Context context = LoginActivity.this;
        databaseHandler=new DatabaseHandler(context);
        getIds();
        setListners();
        etEmail.setText(CommonUtils.getStringPreferences(LoginActivity.this, Constants.EMAIL));
        etPassword.setText(CommonUtils.getPreferences(LoginActivity.this, Constants.PASSWORD));
        if(CommonUtils.getPreferences(LoginActivity.this,Constants.REMEMBER).equalsIgnoreCase("1")){
            cbRemember.setChecked(true);
        }else{
            cbRemember.setChecked(false);
        }
    }
    private void setListners() {
        tvLogin.setOnClickListener(this);
        cbRemember.setOnClickListener(this);
    }
    private void getIds() {
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        tvLogin=(TextView)findViewById(R.id.tvLogin);
        cbRemember=(CheckBox)findViewById(R.id.cbRemember);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvLogin:
                if(validation()) {
                    if(CommonUtils.isOnline(LoginActivity.this)){
                            loginService(etEmail.getText().toString().trim(),etPassword.getText().toString().trim());

                    }else{
                            CommonUtils.showAlert(getString(R.string.please_check_your_internet_connection_p),LoginActivity.this);

                    }
                }
                break;
            case R.id.cbRemember:
                if(cbRemember.isChecked()){
                    CommonUtils.saveStringPreferences(LoginActivity.this, Constants.REMEMBER, "1");
                }else{
                    CommonUtils.saveStringPreferences(LoginActivity.this, Constants.REMEMBER, "0");
                }
                break;
        }
    }
    private boolean validation() {
        if (etEmail.getText().toString().trim().length() == 0) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.please_enter_email_id), this);
            etEmail.requestFocus();
            return false;
        } else if (!CommonUtils.isValidEmail(etEmail.getText().toString().trim())) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.please_enter_valid_email_id), this);
            etEmail.requestFocus();
            return false;
        } else if (etPassword.getText().toString().trim().length() == 0) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.please_enter_password), this);
            etPassword.requestFocus();
            return false;
        } else if (etPassword.getText().toString().trim().length() < 5) {
            CommonUtils.showAlertTitle(getString(R.string.app_name),
                    getString(R.string.invalid_password_length), this);
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
    private void loginService(final String email, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.please_wait));
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("emailID", email);
        requestJson.addProperty("password", password);
        requestJson.addProperty("deviceToken", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        requestJson.addProperty("deviceType", Constant.DEVICETYPE);
        requestJson.addProperty("timezone", TimeZone.getDefault().getID());
        Ion.with(LoginActivity.this)
                .load(Constant.LOGIN)
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
                    if (result.responseCode != null && result.responseCode.equals("200")) {
                        databaseHandler.saveUserDetails(email,result.userID,password);
                        try {
                            if(result.userList!=null&& result.userList.size()>0) {
                                if(validationForNull(result.userList)) {
                                    databaseHandler.addUsersList(result.userList, "1", result.userID);
                                }
                            }
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                        CommonUtils.saveStringPreferences(LoginActivity.this, Constants.LOGIN, "1");
                        CommonUtils.saveStringPreferences(LoginActivity.this, Constants.USERID, result.userID);
                        if(CommonUtils.getStringPreferences(LoginActivity.this, Constants.REMEMBER).equalsIgnoreCase("1")){
                            cbRemember.setChecked(true);
                            CommonUtils.saveStringPreferences(LoginActivity.this, Constants.EMAIL, etEmail.getText().toString().trim());
                            CommonUtils.saveStringPreferences(LoginActivity.this, Constants.PASSWORD, etPassword.getText().toString().trim());
                        }else{
                            cbRemember.setChecked(false);
                            CommonUtils.saveStringPreferences(LoginActivity.this, Constants.EMAIL, "");
                            CommonUtils.saveStringPreferences(LoginActivity.this,Constants.PASSWORD,"");
                        }
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, result.responseMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.server_not_responding), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validationForNull(List<UserListModel> userList) {
        int pass=0;
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getField_nfc_id()!=null && userList.get(i).getUserid()!=null&&userList.get(i).getField_nfc_id().length()>0&&userList.get(i).getUserid().length()>0){
                pass++;
            }
        }
        if(pass==userList.size()){
            return true;
        }
        return false;
    }
}
