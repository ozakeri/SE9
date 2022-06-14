package com.gap.sino.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootChatMessageReceiverReceiver extends BroadcastReceiver {
    public BootChatMessageReceiverReceiver() {
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        //BackgroundService.enqueueWork(context, new Intent());
        //AlarmManagerUtil.scheduleChatMessageReceiver(context);
    }
}
