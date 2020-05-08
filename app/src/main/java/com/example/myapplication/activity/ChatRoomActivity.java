package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.MessageStatus;
import com.backendless.rt.messaging.Channel;
import com.example.myapplication.R;
import com.example.myapplication.utility.ColorPickerUtility;
import com.example.myapplication.utility.TestApplication;


public class ChatRoomActivity extends AppCompatActivity {

    public static final String TAG = "RTChat";
    private EditText message;
    private TextView messages;
    private Channel channel;
    private String color = ColorPickerUtility.next();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        message = findViewById(R.id.message);
        messages = findViewById(R.id.messages);

        final String name = getIntent().getStringExtra("name");
        final String channelName="chat "+ TestApplication.groups.get(TestApplication.position_selected_group).getName();

        channel = Backendless.Messaging.subscribe(channelName);
        channel.addJoinListener(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Backendless.Messaging.publish(channelName, wrapToColor(name) + " joined", new AsyncCallback<MessageStatus>() {
                    @Override
                    public void handleResponse(MessageStatus response) {
                        Log.d(TAG, "Sent joined " + response);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        ChatRoomActivity.this.handleFault(fault);
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                ChatRoomActivity.this.handleFault(fault);
            }
        });
        channel.addMessageListener(new AsyncCallback<String>() {
            @Override
            public void handleResponse(String response) {
                messages.append(Html.fromHtml("<br/>" + response));
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                ChatRoomActivity.this.handleFault(fault);
            }
        });

        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if ( actionId == EditorInfo.IME_ACTION_SEND || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {

                    message.setEnabled(false);

                    Backendless.Messaging.publish(channelName, wrapToColor("[" + name + "]") + ": " + message.getText().toString(), new AsyncCallback<MessageStatus>() {
                        @Override
                        public void handleResponse(MessageStatus response) {
                            Log.d(TAG, "Sent message " + response);
                            message.setText("", TextView.BufferType.EDITABLE);
                            message.setEnabled(true);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            message.setEnabled(true);
                        }
                    });
                    handled = true;
                }

                return handled;
            }
        });

    }

    private void handleFault(BackendlessFault fault) {
        Log.e(TAG, fault.toString());
    }

    private String wrapToColor(String value) {
        return "<font color='" + color + "'>" + value + "</font>";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (channel != null)
            channel.leave();
    }

}
