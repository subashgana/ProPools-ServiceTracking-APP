package com.servicetracker.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.constants.Constant;
import com.servicetracker.constants.Constants;
import com.servicetracker.database.DatabaseHandler;
import com.servicetracker.model.ServiceResponse;
import com.servicetracker.model.UserListModel;

import java.io.ByteArrayOutputStream;
public class BackgroundService extends Service {
    private DatabaseHandler databaseHandler;
    BitmapFactory.Options options = new BitmapFactory.Options();
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseHandler=new DatabaseHandler(BackgroundService.this);
        options.inJustDecodeBounds = true;
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID))!=null){
            for (int i = 0; i <databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).size() ; i++) {
                if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getSync().equalsIgnoreCase("0")){
                    if(CommonUtils.isOnline(BackgroundService.this)) {
                        String image2;
                        if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage2()!=null &&
                                databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage2().length()>0){
                            image2 =encodeTobase64(BitmapFactory.decodeFile(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage2(), options));
                        }else{
                            image2 ="";
                        }
                        String image3;
                        if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage3()!=null &&
                                databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage3().length()>0){
                            image3 =encodeTobase64(BitmapFactory.decodeFile(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage3(), options));
                        }else{
                            image3 ="";
                        }
                        String image4;
                        if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage4()!=null &&
                                databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage4().length()>0){
                            image4 =encodeTobase64(BitmapFactory.decodeFile(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this,Constants.USERID)).get(i).getImage4(), options));
                        }else{
                            image4 ="";
                        }
                        if(CommonUtils.isOnline(BackgroundService.this)) {
                            if (databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this, Constants.USERID)).get(i).getImage1() != null) {
                                addChemicalPerCustomers(BackgroundService.this, databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this, Constants.USERID)).get(i), encodeTobase64(BitmapFactory.decodeFile(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(BackgroundService.this, Constants.USERID)).get(i).getImage1(), options)), image2, image3, image4);
                            }
                        }
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private void addChemicalPerCustomers(final Context context, final UserListModel model, String image1, String image2, String image3, String image4) {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("userID", model.getUserid());
        Log.e("hardness", "hardness" + model.getHardness());
        requestJson.addProperty("hardness", model.getHardness());
        requestJson.addProperty("name", model.getName());
        requestJson.addProperty("nfcid", model.getField_nfc_id());
        requestJson.addProperty("address", model.getAddress());
        requestJson.addProperty("totalchlorine", model.getTotalchlorine());
        requestJson.addProperty("freechlorine", model.getFreechlorine());
        requestJson.addProperty("ph", model.getPh());
        requestJson.addProperty("alkalinity", model.getAlkalinity());
        requestJson.addProperty("conditioner", model.getConditioner());
        requestJson.addProperty("chlpowder", model.getChlpowder());
        requestJson.addProperty("chltablets", model.getChltablets());
        requestJson.addProperty("sodiumchl", model.getSodiumchl());
        requestJson.addProperty("phminus", model.getPhminus());
        requestJson.addProperty("phplus", model.getPhplus());
        requestJson.addProperty("chemicalconditioner", model.getChemicalconditioner());
        requestJson.addProperty("superblue", model.getSuperblue());
        requestJson.addProperty("metalgone", model.getMetalgone());
        requestJson.addProperty("metalout", model.getMetalout());
        requestJson.addProperty("algaecide", model.getAlgaecide());
        requestJson.addProperty("alkaninitycont", model.getAlkaninitycont());
        requestJson.addProperty("acid", model.getAcid());
        requestJson.addProperty("image1", image1);
        requestJson.addProperty("miscnotes", model.getMiscnotes());
        requestJson.addProperty("image2", image2);
        requestJson.addProperty("image3", image3);
        requestJson.addProperty("image4", image4);
        requestJson.addProperty("timestamp", model.getTimestamp());
        requestJson.addProperty("endtime", model.getEndtime());
        Log.e("request", "request" + requestJson);
        Ion.with(context)
                .load(Constant.ADDCHEMICALS)
                .addHeader(Constant.AUTHORIZATION, "Basic " + Base64.encodeToString(("serviceTrackingApp" + ":" + "@1!2@3#QWER#").getBytes(), Base64.NO_WRAP))
                .setJsonObjectBody(requestJson)
                .as(new TypeToken<ServiceResponse>() {
                }).setCallback(new FutureCallback<ServiceResponse>() {
            @Override
            public void onCompleted(Exception e, final ServiceResponse result) {
                String resultLocal = "";
                try {
                    Gson gson = new Gson();
                    resultLocal = gson.toJson(result, ServiceResponse.class);
                    Log.e("response", "response" + resultLocal);
                } catch (Exception e2) {
                }
                if (result != null) {
                    if (result.responseCode != null && result.responseCode.equals("200")) {
                        databaseHandler.updateCustomerInformation(model,"1",CommonUtils.getStringPreferences(BackgroundService.this, Constants.USERID));
                    } else {
                    }
                } else {
                }
            }
        });
    }
    public  String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("profile pic url ", imageEncoded);
        return imageEncoded;
    }
}