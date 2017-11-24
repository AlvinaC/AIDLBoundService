package com.boundservices.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.boundservices.IMyAidlCallbackInterface;
import com.boundservices.IMyAidlInterface;

import java.util.Random;


/**
 * Service that runs remotely(in another process) - provides a random no on request from a client
 */
public class RandomNoGenService extends Service {

    //async interface- returns immediately- hence callbacks used
    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {

        public void getPid(IMyAidlCallbackInterface callback) throws RemoteException {
            callback.handleResponsePid(android.os.Process.myPid());
        }

        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing
        }

        public void getRandomNo(IMyAidlCallbackInterface callback) throws RemoteException {
            Random rand = new Random();
            callback.handleResponseRandomNo(rand.nextInt(100));
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
