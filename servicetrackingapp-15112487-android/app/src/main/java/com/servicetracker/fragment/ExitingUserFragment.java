package com.servicetracker.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.servicetracker.R;
import com.servicetracker.activity.CustomerInformationActivity;
import com.servicetracker.adapter.ExitingUserAdapter;
import com.servicetracker.constants.CommonUtils;
import com.servicetracker.constants.Constants;
import com.servicetracker.database.DatabaseHandler;
import com.servicetracker.model.UserListModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;


public class ExitingUserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private View view;
    private ListView lvUsers;
    private List<UserListModel> arlExistingUsers;
    private ExitingUserAdapter adapter;
    private DatabaseHandler databaseHandler;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_user, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        context = getActivity();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        arlExistingUsers = new ArrayList<>();
        UserListModel model = new UserListModel();
        databaseHandler = new DatabaseHandler(context);
        if (databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)) != null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).size() > 0) {
            for (int i = 0; i < databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).size(); i++) {
                if (databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp() != null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp().length() > 0) {
                    Log.e("time stamp is", "time stamp is" + databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp());
                    arlExistingUsers.add(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i));
                }
            }
        }
        getIds();
        setListners();

        if (arlExistingUsers.size() > 0) {
            Collections.sort(arlExistingUsers, new Comparator<UserListModel>() {
                @Override
                public int compare(UserListModel object1, UserListModel object2) {
                    if (object1.getTimestamp() != null && object1.getTimestamp().length() > 0 && object2.getTimestamp() != null && object2.getTimestamp().length() > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            return Long.compare(Long.parseLong(object2.getTimestamp()), Long.parseLong(object1.getTimestamp()));
                        }
                    }
                    return 0;
                }
            });
        }
        adapter = new ExitingUserAdapter(context, arlExistingUsers);
        lvUsers.setAdapter(adapter);
        return view;
    }

    private void setListners() {

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("customer id", "customer id" + arlExistingUsers.get(position).getUserid());
                Intent intent = new Intent(context, CustomerInformationActivity.class);
                intent.putExtra("user_id", arlExistingUsers.get(position).getUserid());
                getActivity().startActivityForResult(intent, 1);
            }
        });
    }

    public void onEventMainThread(String value) {
        if (value != null && value.equalsIgnoreCase("2")) {
            arlExistingUsers.clear();
            if (databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)) != null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).size() > 0) {
                for (int i = 0; i < databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).size(); i++) {
                    if (databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp() != null && databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i).getTimestamp().length() > 0) {
                        arlExistingUsers.add(databaseHandler.getNewUserList(CommonUtils.getStringPreferences(context, Constants.USERID)).get(i));
                    }
                }
            }
            adapter = new ExitingUserAdapter(context, arlExistingUsers);
            lvUsers.setAdapter(adapter);
        }
    }

    private void getIds() {
        lvUsers = (ListView) view.findViewById(R.id.lvUsers);
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
