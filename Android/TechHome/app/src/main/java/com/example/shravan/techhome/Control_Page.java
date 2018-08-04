package com.example.shravan.techhome;

import android.annotation.SuppressLint;
import  android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static android.R.attr.value;
import static android.R.id.message;


public class Control_Page extends AppCompatActivity {
    pl.droidsonroids.gif.GifImageView gif;
    private String address = null;
    private ProgressDialog progress;
    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();
    private String save;
    public  int secureflag=0;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectedThread mConnectedThread;
    final int RECIEVE_MESSAGE = 2;
    private Handler h;
    String temp_temp;
    public String fanspeed;
    public int ledstatus=0,fanstatus=0;
    public int ledflag=0,fanflag=1,statusflag=0;
    TextView welcome,led,fan,status,fanmode,showspeed;
    SeekBar speed;
    private final String TAG="CONTROL";
    Button auto,manual,checkstatus,secure;
    ImageView ledoff,ledon,fanoff;
    public String msg_recieved="";
    UserInfo userInfo;
    @SuppressLint({"SetTextI18n", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(Device_List.EXTRA_ADDRESS);
        userInfo=(UserInfo)newint.getSerializableExtra("userinfo");

        setContentView(R.layout.activity_control__page);

        // initializzing the widgets
        gif=(pl.droidsonroids.gif.GifImageView)findViewById(R.id.gif);
        welcome=(TextView)findViewById(R.id.welcome);
        led=(TextView)findViewById(R.id.led);
        speed=(SeekBar)findViewById(R.id.speed);
        fan=(TextView)findViewById(R.id.fan);
        status=(TextView)findViewById(R.id.status);
        fanmode=(TextView)findViewById(R.id.fanmode);
        showspeed=(TextView)findViewById(R.id.showspeed);
        secure=(Button)findViewById(R.id.secure);
        auto=(Button)findViewById(R.id.auto);
        manual=(Button)findViewById(R.id.manual);
        checkstatus=(Button)findViewById(R.id.checkstatus);
        ledoff=(ImageView)findViewById(R.id.ledoff);
        ledon=(ImageView)findViewById(R.id.ledon);
        fanoff=(ImageView)findViewById(R.id.fanoff);
        gif.setVisibility(View.INVISIBLE);
        fanoff.setVisibility(View.VISIBLE);


        new ConnectBT().execute(); //Call the class to connect
        welcome.setText("Welcome To TechHome "+userInfo.getname());




        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE: // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1); // create string from bytes array
                        sb.append(strIncom); // append string
                        int endOfLineIndex = sb.indexOf("\r\n");// determine the end-of-line
                        if (endOfLineIndex > 0) {
                            msg_recieved = sb.substring(0, endOfLineIndex); // extract string



                            sb.delete(0, sb.length()); // and clear
switch (msg_recieved){
    case "s":

        status.setText("Intrusion Is Happening Contact Police ");
        status.setTextColor(Color.parseColor("#ff0000"));

        break;


    case "x":status.setText("Smoke Detected Call Fire Station");
        status.setTextColor(Color.parseColor("#ff0000"));

        gif.setVisibility(View.INVISIBLE);
        fanoff.setVisibility(View.VISIBLE);
        speed.setProgress(0);
        showspeed.setText("0%");
        fanstatus=0;
        break;
    case "y":status.setText("Fire has been extinguished");
        status.setTextColor(Color.parseColor("#008000"));
        break;


}



                            if(fanflag==1){

                                switch (msg_recieved){
                                    case "c":fan.setText("Fan is currently operating at 0%");
                                        gif.setVisibility(View.INVISIBLE);
                                        fanoff.setVisibility(View.VISIBLE);
                                        fanstatus=0;
                                        speed.setProgress(0);
                                        showspeed.setText("0%");
                                        break;
                                    case "d":fan.setText("Fan is currently operating at 25%");
                                        gif.setVisibility(View.VISIBLE);
                                        fanstatus=1;
                                        fanoff.setVisibility(View.INVISIBLE);
                                        speed.setProgress(25);
                                        showspeed.setText("25%");
                                        break;
                                    case "e":fan.setText("Fan is currently operating at 50%");
                                        gif.setVisibility(View.VISIBLE);
                                        fanstatus=1;
                                        fanoff.setVisibility(View.INVISIBLE);
                                        speed.setProgress(50);
                                        showspeed.setText("50%");
                                        break;

                                    case "f":fan.setText("Fan is currently operating at 75%");
                                        gif.setVisibility(View.VISIBLE);
                                        fanstatus=1;
                                        fanoff.setVisibility(View.INVISIBLE);
                                        speed.setProgress(75);
                                        showspeed.setText("75%");
                                        break;
                                    case "g":fan.setText("Fan is currently operating at 100%");
                                        gif.setVisibility(View.VISIBLE);
                                        fanstatus=1;
                                        fanoff.setVisibility(View.INVISIBLE);
                                        speed.setProgress(100);
                                        showspeed.setText("100%");
                                        break;





                                }fanflag=0;
                            }else if(statusflag==1){
                                
                                    status.setText("");
                                    statusflag=0;
                                if(ledstatus==1){
                                    status.append("1 Led Is On \n");

                                }
                                if(fanstatus==1){
                                    status.append("Fan Is On");

                                }
                                status.append("\nTemperature is"+msg_recieved);

                            }






                        }
                        break;
                }
            };
        };


        speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int stepSize=25;
                progress = ((int)Math.round(progress/stepSize))*stepSize;
                seekBar.setProgress(progress);

try {


    if (fromUser == true) {
        fanspeed = String.valueOf(progress);



        switch (fanspeed) {

            case "0":
                btSocket.getOutputStream().write("c".toString().getBytes());
                showspeed.setText(fanspeed + "%");
                gif.setVisibility(View.INVISIBLE);
                fanoff.setVisibility(View.VISIBLE);
                fanstatus=0;
                fan.setText("Fan is currently operating at 0%");
                break;

            case "25":
                fanstatus=1;
                btSocket.getOutputStream().write("d".toString().getBytes());
                showspeed.setText(fanspeed + "%");
                fan.setText("Fan is currently operating at 25%");

                gif.setVisibility(View.VISIBLE);
                fanoff.setVisibility(View.INVISIBLE);
                break;

            case "50":
                fanstatus=1;
                btSocket.getOutputStream().write("e".toString().getBytes());
                showspeed.setText(fanspeed + "%");
                fan.setText("Fan is currently operating at 50%");
                gif.setVisibility(View.VISIBLE);
                fanoff.setVisibility(View.INVISIBLE);
                break;

            case "75":
                fanstatus=1;
                btSocket.getOutputStream().write("f".toString().getBytes());
                showspeed.setText(fanspeed + "%");
                fan.setText("Fan is currently operating at 75%");
                gif.setVisibility(View.VISIBLE);
                fanoff.setVisibility(View.INVISIBLE);
                break;

            case "100":fanstatus=1;
                btSocket.getOutputStream().write("g".toString().getBytes());
                showspeed.setText(fanspeed + "%");
                fan.setText("Fan is currently operating at 100%");
                gif.setVisibility(View.VISIBLE);
                fanoff.setVisibility(View.INVISIBLE);
                break;
        }
    }





                    }catch(Exception e){


}




            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        gif.setOnClickListener(new View.OnClickListener()
{
    @Override
    public void onClick(View v)
    {


              //method to turn on
    }
});
//creating listener for led off

        ledoff.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ledon.setVisibility(View.VISIBLE);
                ledoff.setVisibility(View.INVISIBLE);
                turnLedOn();
                ledstatus=1;
                led.setText("Currently The LED is On");
            }
        });

//create listener for ledon
        ledon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ledon.setVisibility(View.INVISIBLE);
                ledoff.setVisibility(View.VISIBLE);

                ledstatus=0;

                turnLedOff();      //method to turn on
                led.setText("Currently The LED is Off");
            }
        });
        auto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try {

                    speed.setVisibility(View.INVISIBLE);
                    showspeed.setVisibility(View.INVISIBLE);
                    fanmode.setText("Fan is in Auto Mode");
                    setfanauto();



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        manual.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                    speed.setVisibility(View.VISIBLE);
                    showspeed.setVisibility(View.VISIBLE);
                    fanmode.setText("Fan is in Manual Mode");









            }
        });
//secure click listener
        secure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {   if(secureflag==0) {
                secure.setText("Motion Sensor Currently Active  ");
                secureflag=1;
                try {
                    btSocket.getOutputStream().write("k".toString().getBytes());


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else{
                secureflag=0;
                secure.setText("Motion Sensor Is Currently Inactive ");
                try {
                    btSocket.getOutputStream().write("l".toString().getBytes());
                status.setText("");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            }
        });
//end of secure





        checkstatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try {

                    statusflag=1;

                    btSocket.getOutputStream().write("j".toString().getBytes());




                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

































    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            btSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void turnLedOff() {


        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("b,".toString().getBytes());


            } catch (IOException e) {
                msg("Error");
            }
        }
    }

public void SaveMsg(String msg){

    save=msg;


}



    public void setfanauto() throws IOException {


        fanflag=1;

        btSocket.getOutputStream().write("i".toString().getBytes());



        return;

    }



    private void turnLedOn()
    {
        if (btSocket!=null)
        {
            try {



                btSocket.getOutputStream().write("a".toString().getBytes());





            }



            catch (Exception e){



            }
        }
    }




    private void msg(String s) {


        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();

    }







    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;


        public ConnectedThread(BluetoothSocket socket)
        {
            InputStream tmpIn = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
        }


        public void run() {
            byte[] buffer = new byte[256]; // buffer store for the stream
            int bytes; // bytes returned from read()

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);// Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();// Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

    }







    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Control_Page.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection



                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }



            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");



                //finish();


            }
            else
            {



                msg("Connected.");
                isBtConnected = true;

            }
            progress.dismiss();
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
            try {
                setfanauto();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }




}


