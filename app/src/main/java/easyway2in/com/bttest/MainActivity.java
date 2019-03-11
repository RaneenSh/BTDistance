package easyway2in.com.bttest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    String text;
    int req = 0;

    Button listen , listDevices;
    ListView listView;
    TextView msg_box,stutes ;
    EditText writeMsg;

    BluetoothAdapter myBluetoothAdapter;
    BluetoothDevice[] btarray;
    SendReceive sendReceive;

    static final int STATE_LISTENING=1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    int REQUEST_ENABLE_BLUETOOTH=1;

    private static final String APP_NAME = "BTChat";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static int CALLED = 1;
    public static  int status;


    MediaPlayer mMediaPlayer, m1, m2, m3, m4, m5, m6, m7, m8, m9, m10, m11, m12, m13, m14, m15, m16, m17, m18, m19, m20, m21, m22;
    MediaPlayer m23, m24, m25, m26;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listen = (Button) findViewById(R.id.listen);
        listDevices= (Button) findViewById(R.id.listdevices);
        listView = (ListView) findViewById(R.id.listview);
        msg_box = (TextView) findViewById(R.id.Msg);
        stutes = (TextView) findViewById(R.id.status);

        mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.half);
        m1 = MediaPlayer.create(MainActivity.this, R.raw.one);
        m2 = MediaPlayer.create(MainActivity.this, R.raw.one_half);
        m3 = MediaPlayer.create(MainActivity.this, R.raw.two);
        m4 = MediaPlayer.create(MainActivity.this, R.raw.two_half);
        m5 = MediaPlayer.create(MainActivity.this, R.raw.three);
        m6 = MediaPlayer.create(MainActivity.this, R.raw.three_half);
        m7 = MediaPlayer.create(MainActivity.this, R.raw.four);
        m8 = MediaPlayer.create(MainActivity.this, R.raw.four_half);
        m9 = MediaPlayer.create(MainActivity.this, R.raw.five);
        m10 = MediaPlayer.create(MainActivity.this, R.raw.five_half);
        m11 = MediaPlayer.create(MainActivity.this, R.raw.six);
        m12 = MediaPlayer.create(MainActivity.this, R.raw.six_half);
        m13 = MediaPlayer.create(MainActivity.this, R.raw.seven);
        m14 = MediaPlayer.create(MainActivity.this, R.raw.seven_half);
        m15 = MediaPlayer.create(MainActivity.this, R.raw.eight);
        m16 = MediaPlayer.create(MainActivity.this, R.raw.eight_half);
        m17 = MediaPlayer.create(MainActivity.this, R.raw.nine);
        m18 = MediaPlayer.create(MainActivity.this, R.raw.nine_half);
        m19 = MediaPlayer.create(MainActivity.this, R.raw.ten);
        m20 = MediaPlayer.create(MainActivity.this, R.raw.ten_half);
        m21 = MediaPlayer.create(MainActivity.this, R.raw.eleven);
        m22 = MediaPlayer.create(MainActivity.this, R.raw.eleven_half);
        m23 = MediaPlayer.create(MainActivity.this, R.raw.twelve);
        m24 = MediaPlayer.create(MainActivity.this, R.raw.twelve_half);
        m25 = MediaPlayer.create(MainActivity.this, R.raw.thirteen);
        m26 = MediaPlayer.create(MainActivity.this, R.raw.thirteen_half);

    //    mMediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.whistle_sound);



        if (status == CALLED) {
            //  Toast.makeText(getApplicationContext(), "called", Toast.LENGTH_LONG).show();
            text = getIntent().getExtras().getString("text");
            writeMsg.setText(text);
        }


        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!myBluetoothAdapter.isEnabled())
        {
            Intent enableIntent = new Intent((myBluetoothAdapter.ACTION_REQUEST_ENABLE));
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH);
        }

        ServerClass serverClass = new ServerClass();
        serverClass.start();
        implemeentlisteners();

    }


    private void implemeentlisteners() {
        listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> bt=myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                btarray = new BluetoothDevice[bt.size()];
                int index=0;

                if(bt.size()>0)
                {
                    for(BluetoothDevice device : bt)
                    {
                        // strings[index] = device.getName();
                        btarray[index] = device;
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ClientClass clientClass = new ClientClass(btarray[i]);
                clientClass.start();

                stutes.setText("Connecting");
            }
        });

    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //textView.setText(String.valueof(message.arg1));
            switch (msg.what)
            {
                case STATE_LISTENING:
                    stutes.setText("Liatening");
                    break;
                case STATE_CONNECTING:
                    stutes.setText("CONNECTING");
                    break;
                case STATE_CONNECTED:
                    stutes.setText("CONNECTED");
                    break;
                case STATE_CONNECTION_FAILED:
                    stutes.setText("CONNECTION FAILED");
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMag = new String(readBuff,0,msg.arg1);
                    msg_box.setText(tempMag);
                    //Here are the audios
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    }else if (m1.isPlaying()) {
                        m1.pause();
                        m1.seekTo(0);
                    }else if (m2.isPlaying()) {
                        m2.pause();
                        m2.seekTo(0);
                    }else if (m3.isPlaying()) {
                        m3.pause();
                        m3.seekTo(0);
                    }else if (m4.isPlaying()) {
                        m4.pause();
                        m4.seekTo(0);
                    }else if (m5.isPlaying()) {
                        m5.pause();
                        m5.seekTo(0);
                    }else if (m6.isPlaying()) {
                        m6.pause();
                        m6.seekTo(0);
                    }else if (m7.isPlaying()){
                        m7.pause();
                        m7.seekTo(0);
                    }else if (m8.isPlaying()) {
                        m8.pause();
                        m8.seekTo(0);
                    }else if (m9.isPlaying()) {
                        m9.pause();
                        m9.seekTo(0);
                    }else if (m10.isPlaying()) {
                        m10.pause();
                        m10.seekTo(0);
                    }else if (m11.isPlaying()) {
                        m11.pause();
                        m11.seekTo(0);
                    }else if (m12.isPlaying()) {
                        m12.pause();
                        m12.seekTo(0);
                    }else if (m13.isPlaying()) {
                        m13.pause();
                        m13.seekTo(0);
                    }else if (m14.isPlaying()) {
                        m14.pause();
                        m14.seekTo(0);
                    }else if (m15.isPlaying()) {
                        m15.pause();
                        m15.seekTo(0);
                    }else if (m16.isPlaying()) {
                        m16.pause();
                        m16.seekTo(0);
                    }else if (m17.isPlaying()) {
                        m17.pause();
                        m17.seekTo(0);
                    }else if (m18.isPlaying()) {
                        m18.pause();
                        m18.seekTo(0);
                    }else if (m19.isPlaying()) {
                        m19.pause();
                        m19.seekTo(0);
                    }else if (m20.isPlaying()) {
                        m20.pause();
                        m20.seekTo(0);
                    }else if (m21.isPlaying()) {
                        m21.pause();
                        m21.seekTo(0);
                    }else if (m22.isPlaying()) {
                        m22.pause();
                        m22.seekTo(0);
                    }else if (m23.isPlaying()) {
                        m23.pause();
                        m23.seekTo(0);
                    }else if (m24.isPlaying()) {
                        m24.pause();
                        m24.seekTo(0);
                    }else if (m25.isPlaying()) {
                        m25.pause();
                        m25.seekTo(0);
                    }


                    if (tempMag.startsWith("0.5") || tempMag.startsWith(".5")) {
                        mMediaPlayer.start();
                    }else if (tempMag.startsWith("1.5")){
                        m2.start();
                    }else if (tempMag.startsWith("1.0") || tempMag.startsWith("1")){
                        m1.start();
                    }else if (tempMag.startsWith("2.5")){
                        m4.start();
                    }else if (tempMag.startsWith("2") || tempMag.startsWith("2.0")){
                        m3.start();
                    }else if (tempMag.startsWith("3.5")){
                        m6.start();
                    }else if (tempMag.startsWith("3") || tempMag.startsWith("3.0")){
                        m5.start();
                    }else if (tempMag.startsWith("4.5")){
                        m8.start();
                    }else if (tempMag.startsWith("4") || tempMag.startsWith("4.0")){
                        m7.start();
                    }else if (tempMag.startsWith("5.5")){
                        m10.start();
                    }else if (tempMag.startsWith("5") || tempMag.startsWith("5.0")){
                        m9.start();
                    }else if (tempMag.startsWith("6.5")){
                        m12.start();
                    }else if (tempMag.startsWith("6") || tempMag.startsWith("6.0")){
                        m11.start();
                    }else if (tempMag.startsWith("7.5")){
                        m14.start();
                    }else if (tempMag.startsWith("7") || tempMag.startsWith("7.0")){
                        m13.start();
                    }else if (tempMag.startsWith("8.5")){
                        m16.start();
                    }else if (tempMag.startsWith("8") || tempMag.startsWith("8.0")){
                        m15.start();
                    }else if (tempMag.startsWith("9.5")){
                        m18.start();
                    }else if (tempMag.startsWith("9") || tempMag.startsWith("9.0")){
                        m17.start();
                    }else if (tempMag.startsWith("10.5")){
                        m20.start();
                    }else if (tempMag.startsWith("10") || tempMag.startsWith("10.0")){
                        m19.start();
                    }else if (tempMag.startsWith("11.5")){
                        m22.start();
                    }else if (tempMag.startsWith("11") || tempMag.startsWith("11.0")){
                        m21.start();
                    }else if (tempMag.startsWith("12.5")){
                        m24.start();
                    }else if (tempMag.startsWith("12") || tempMag.startsWith("12.0")){
                        m23.start();
                    }else if (tempMag.startsWith("13") || tempMag.startsWith("13.0")){
                        m25.start();
                    }
                    break;
            }
            return true;
        }
    });

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass()
        {
            try {
                serverSocket = myBluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME,MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//constructer
        public void run()
        {
            BluetoothSocket socket = null;
            while(socket==null)
            {
                try {
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);

                }//chatch

                if(socket!=null)
                {
                    Message message = Message.obtain();
                    message.what=STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendReceive = new SendReceive(socket);
                    sendReceive.start();

                    break;
                }//if socket != null

            }//while
        }
    }
    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1)
        {
            device=device1;
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }//constructer
        public void run()
        {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }//class client

    private class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public SendReceive (BluetoothSocket socket)
        {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempout=null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempout = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = tempIn;
            outputStream = tempout;

        }
        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes=inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//while
        }//run
        public void write(byte[] bytes)
        {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

