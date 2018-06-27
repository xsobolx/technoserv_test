package ru.alpha.technoservtest.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.alpha.technoservtest.R;


/**
 * Created by Aleksandr Sobol
 * asobol@golamago.com
 * on 25/06/2018.
 */
public class MessagesActivity extends AppCompatActivity {

    private TextView textMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        NotificationService.startService(this);

        textMessage = (TextView) findViewById(R.id.textMessage);

        if (getIntent() != null) {
            if (getIntent().getStringExtra(NotificationService.MESSAGE_EXTRA) != null) {
                textMessage.setText(getIntent().getStringExtra(NotificationService.MESSAGE_EXTRA));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NotificationService.stopService(this);
    }
}
