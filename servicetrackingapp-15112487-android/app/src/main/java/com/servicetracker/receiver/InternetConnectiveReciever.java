package com.servicetracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.servicetracker.constants.CommonUtils;
import com.servicetracker.service.BackgroundService;

import de.greenrobot.event.EventBus;

/**
 * Created by ${pushpender} on ${1/12/15}.
 */
public class InternetConnectiveReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (CommonUtils.isOnline(context)) {
            EventBus.getDefault().post("1");
            intent = new Intent(context, BackgroundService.class);
            context.startService(intent);
        }
    }
}
