package kr.co.chlee.twpushhelper;

import org.json.JSONException;
import org.json.JSONObject;

interface PushHelper {
    void putPush(final JSONObject payload) throws JSONException;
    void putTimeOfPush(final Long timestamp);
    void setPushTime(Long time);
    void changeLockState(boolean state);
    void getPayloadFromServer(Long timestamp);
    void callBackProcess(Long timestamp, boolean isSuccess);
}
