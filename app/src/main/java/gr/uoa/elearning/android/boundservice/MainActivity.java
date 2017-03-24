package gr.uoa.elearning.android.boundservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SimpleService simpleService;
    private boolean bound;

    public ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            simpleService = ((SimpleService.MyBinder) service).getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText valueEditText = (EditText)findViewById(R.id.valueEditText);
        Button setValueButton = (Button)findViewById(R.id.setValueButton);
        final TextView valueTextView = (TextView)findViewById(R.id.valueTextView);
        Button getValueButton = (Button)findViewById(R.id.getValueButton);

        bound = false;

        Intent intent = new Intent(MainActivity.this, SimpleService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);

        setValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long value = Long.parseLong(valueEditText.getText().toString());
                if (bound){
                    simpleService.writeLongToFile(value);
                }
            }
        });

        getValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bound){
                    long value = simpleService.readLongFromFile();
                    valueTextView.setText(value+"");
                }
            }
        });
    }
}
