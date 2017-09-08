package com.servicetracker.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.servicetracker.R;
import com.servicetracker.activity.CustomerInformationActivity;
import com.servicetracker.adapter.NewUserAdapter;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.constants.Constant;
import com.servicetracker.constants.Constants;
import com.servicetracker.database.DatabaseHandler;
import com.servicetracker.model.ServiceResponse;
import com.servicetracker.model.UserListModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class NewUserFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    private Context context;
    private View view;
    private ListView lvUsers;
    private List<UserListModel> arlUsers;
    private NewUserAdapter adapter;
    private DatabaseHandler databaseHandler;
    private SwipeRefreshLayout swipeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_user, container, false);
        swipeLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        context=getActivity();
        databaseHandler=new DatabaseHandler(getActivity());
        arlUsers= new ArrayList<>();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getIds();
        setListners();
        if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID))!=null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).size()>0) {
            for (int i = 0; i < databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).size(); i++) {
                if(!databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i).getTimestamp().equalsIgnoreCase("")){
                    Log.e("inoside iftime stamp ","time stamp "+databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i).getTimestamp());
                }else{
                    Log.e("inoside esletime stamp ","time stamp "+databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i).getTimestamp());
                    arlUsers.add(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i));
                }
            }
        }
        if (arlUsers.size() > 0) {
            Collections.sort(arlUsers, new Comparator<UserListModel>() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public int compare(UserListModel object1, UserListModel object2) {
                    if (object1.getTimestamp() != null&&object1.getTimestamp() .length()>0 && object2.getTimestamp() != null&&object2.getTimestamp() .length()>0 ) {
                        return Long.compare(Long.parseLong(object2.getTimestamp()), Long.parseLong(object1.getTimestamp()));
                    }
                    return 0;
                }
            });
        }
        adapter=new NewUserAdapter(context,arlUsers);
        lvUsers.setAdapter(adapter);
        return view;
    }
    private void setListners() {
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, CustomerInformationActivity.class);
                intent.putExtra("user_id",arlUsers.get(position).getUserid());
                context.startActivity(intent);
            }
        });
    }
    private void getIds()
    {
       lvUsers=(ListView)view.findViewById(R.id.lvUsers);
    }
    private void getNewUserList() {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("uid", CommonUtils.getStringPreferences(context, Constants.USERID));
        Log.e("request", "request" + requestJson);
        Ion.with(context)
                .load(Constant.CUSTOMERLIST)
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
                        swipeLayout.setRefreshing(false);
                        arlUsers.clear();
                        if (result.customer != null && result.customer.size() > 0) {
                            if (validationForNull(result.customer)) {
                                databaseHandler.delete(CommonUtils.getStringPreferences(context, Constants.USERID));
                                databaseHandler.addUsersList(result.customer, "1", CommonUtils.getStringPreferences(context, Constants.USERID));
                            }
                            if (context != null) {
                                if (databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)) != null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).size() > 0) {
                                    for (int i = 0; i < databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).size(); i++) {
                                        if (!databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp().equalsIgnoreCase("")) {
                                            Log.e("inoside iftime stamp ", "time stamp " + databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp());
                                        } else {
                                            Log.e("inoside esletime stamp ", "time stamp " + databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp());
                                            arlUsers.add(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i));
                                        }
                                    }
                                }
                                if (arlUsers.size() > 0) {
                                    Collections.sort(arlUsers, new Comparator<UserListModel>() {
                                        @Override
                                        public int compare(UserListModel object1, UserListModel object2) {
                                            if (object1.getTimestamp() != null && object1.getTimestamp().length() > 0 && object2.getTimestamp() != null && object2.getTimestamp().length() > 0) {
                                                return Long.compare(Long.parseLong(object2.getTimestamp()), Long.parseLong(object1.getTimestamp()));
                                            }
                                            return 0;
                                        }
                                    });
                                }
                            }
                            adapter = new NewUserAdapter(context, arlUsers);
                            lvUsers.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(context, result.responseMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                }
            }
        });
    }

    public void onEventMainThread(String value) {
        if(value!=null &&value.equalsIgnoreCase("1")){
            getNewUserList();
        }else if(value!=null &&value.equalsIgnoreCase("2")){
            arlUsers.clear();
            if(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID))!=null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).size()>0) {
                for (int i = 0; i < databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).size(); i++) {
                    if(!databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i).getTimestamp().equalsIgnoreCase("")){
                        Log.e("inoside iftime stamp ","time stamp "+databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i).getTimestamp());
                    }else{
                        Log.e("inoside esletime stamp ","time stamp "+databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i).getTimestamp());
                        arlUsers.add(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context,Constants.USERID)).get(i));
                    }
                }
            }
            if (arlUsers.size() > 0) {
                Collections.sort(arlUsers, new Comparator<UserListModel>() {
                    @Override
                    public int compare(UserListModel object1, UserListModel object2) {
                        if (object1.getTimestamp() != null&&object1.getTimestamp() .length()>0 && object2.getTimestamp() != null&&object2.getTimestamp() .length()>0 ) {
                            return Long.compare(Long.parseLong(object2.getTimestamp()), Long.parseLong(object1.getTimestamp()));
                        }
                        return 0;
                    }
                });
            }
            adapter=new NewUserAdapter(context,arlUsers);
            lvUsers.setAdapter(adapter);
        }
    }
    @Override
    public void onRefresh() {
        if(CommonUtils.isOnline(context)){
            getNewUserList();
        }else{
            swipeLayout.setRefreshing(false);
            Toast.makeText(context, getString(R.string.please_check_your_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    private boolean validationForNull(List<UserListModel> userList) {
        int pass=0;
        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getField_nfc_id()!=null && userList.get(i).getUserid()!=null&&userList.get(i).getField_nfc_id().length()>0&&userList.get(i).getUserid().length()>0){
                pass++;
            }
        }
        Log.e("pass== userlist","pass== userlist"+pass+"=="+ userList.size());
        if(pass==userList.size()){
            return true;
        }
        return false;
    }
}
