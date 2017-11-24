package com.boundservices.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boundservices.IMyAidlCallbackInterface;
import com.boundservices.IMyAidlInterface;
import com.boundservices.R;
import com.boundservices.services.RandomNoGenService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Client that uses the service of a remote service to get a random no
 */
public class MainActivity extends AppCompatActivity {

    private IMyAidlInterface mBoundServiceInterface;
    private boolean mServiceConnected = false;

    @BindView(R.id.txt_gen_rand)
    TextView txt_gen_rand;

    @BindView(R.id.btn_gen_rand)
    Button btn_gen_rand;

    @OnClick(R.id.btn_gen_rand)
    public void submit() {
        try {
            //pass the callback to server(ie. service in other process)
            (mBoundServiceInterface).getRandomNo(mCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //server contacts the client
    IMyAidlCallbackInterface.Stub mCallback = new IMyAidlCallbackInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
            //does nothing
        }

        public void handleResponsePid(int pid) throws RemoteException {
            //will have the process id if used
        }

        public void handleResponseRandomNo(final int no) throws RemoteException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //thread that has created the UI object can only access it,hence runonUI...
                    txt_gen_rand.setText(String.valueOf(no));
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create global var's
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, RandomNoGenService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceConnected) {
            unbindService(mServiceConnection);
            mServiceConnected = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceConnected = false;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_disconnected), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //can get the binder only in this method
            mBoundServiceInterface = IMyAidlInterface.Stub.asInterface(service);
            mServiceConnected = true;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_connected), Toast.LENGTH_SHORT).show();
        }
    };
}
