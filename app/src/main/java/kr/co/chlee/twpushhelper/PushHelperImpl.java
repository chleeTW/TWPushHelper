package kr.co.chlee.twpushhelper;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class PushHelperImpl implements PushHelper {

    private Context mContext;

    public PushHelperImpl(Context context) {
        this.mContext = context;
    }
    public Context getContext(){
        return mContext;
    }

    private final String MQTT_TIME_KEY = "registered_timestamp_in_mill";
    private Long receivedMqttTime = -1L;
    private boolean lock = false;

    public abstract void apiCall(Long timestamp);

    public abstract void setTimestamp(Long timestamp);

    public abstract Long getTimestamp();

    public abstract void mqttHandle(JSONObject message) throws JSONException;

    @Override
    public synchronized void putPush(JSONObject payload) throws JSONException {
        if(payload.has(MQTT_TIME_KEY)) {
            Long timestamp = payload.getLong(MQTT_TIME_KEY);
            putTimeOfPush(timestamp);
        }
    }

    @Override
    public synchronized void putTimeOfPush(Long timestamp) {
        if(!lock) {
            getPayloadFromServer(timestamp);
        }
        setPushTime(timestamp);
    }

    @Override
    public synchronized void setPushTime(Long time) {
        if(time > receivedMqttTime){
            receivedMqttTime = time;
        }
    }

    @Override
    public synchronized void changeLockState(boolean state) {
        lock = state;
    }

    @Override
    public synchronized void getPayloadFromServer(final Long timestamp) {

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                long lastSuccessTimestamp = getTimestamp();
                if(timestamp > lastSuccessTimestamp) {
                    changeLockState(true);
                    apiCall(timestamp);
                }
            }
        }).start();
    }

    @Override
    public synchronized void callBackProcess(Long timestamp, boolean isSuccess) {
        if(isSuccess) setTimestamp(timestamp);
        changeLockState(false);
        if(receivedMqttTime > timestamp) {
            getPayloadFromServer(receivedMqttTime);
        }
    }
}
