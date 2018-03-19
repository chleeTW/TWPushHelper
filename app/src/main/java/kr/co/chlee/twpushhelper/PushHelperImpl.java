package kr.co.chlee.twpushhelper;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @brief 푸시 메시지 처리를 위한 PushHelper 구현 클래스
 * API 호출 시 필요한 파라미터 값인 start_timestamp을 저장하기 위해서 SharedPreferences를 활용.
 * 해당 값의 초기화는 로그인 성공 후 동기화된 서버시간을 기준으로 설정함.
 * @author chlee
 * @date 2018-02-23
 */

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

    /**
     * Server 에서 마지막으로 성공한(푸시 메시지 처리) 시간부터 새로운 푸시 메시지의 시간까지 해당하는 메시지들을 받아와서 처리하도록 구현해야함
     * API 응답 성공시에만 마지막으로 성공한 타임스템프를 변경
     * @param timestamp 호출할 푸시 메시지의 시간
     */
    abstract void apiCall(Long timestamp);


    /**
     * SharedPreferences에 MQTT 타임스템프를 저장하도록 구현해야함
     * @param timestamp 새로운 MQTT의 시간
     */
    abstract void setTimestamp(Long timestamp);


    /**
     * SharedPreferences에 저장된 MQTT 타임스템프를 반환하도록 구현해야함
     */
    abstract Long getTimestamp();

    public abstract void handlePushMessage(JSONObject message) throws JSONException;

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
