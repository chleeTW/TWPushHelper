package kr.co.chlee.twpushhelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chlee
 * @date 2018-02-23
 */
interface TWPushHelper {

    /**
     * 푸시 메시지 수신시 처리
     * @param payload 푸시 메시지의
     */
    void putPush(final JSONObject payload) throws JSONException;


    /**
     * 푸시 메시지의 시간을 추가한다.
     * @param timestamp 새로운 푸시 메시지의 시간
     */
    void putTimeOfPush(final Long timestamp);


    /**
     * 마지막으로 전달받은 푸시 메시지의 시간을 새로운 푸시 메시지 시간로 교체
     * @param time 새로운 MQTT 시간
     */
    void setPushTime(Long time);


    /**
     * lock 상태 변경
     * @param state 상태
     */
    void changeLockState(boolean state);


    /**
     * 서버로 푸시 메시지의 내용을 받도록 API 요청
     * @param timestamp 호출할 푸시 메시지의 시간
     */
    void getPayloadFromServer(Long timestamp);


    /**
     * API 콜백 받은 이후의 처리
     * lock을 풀어주고, 새로운 푸시 메시지를 수신받은 경우 API를 재호출한다.
     * @param timestamp 해당 API에서 사용한 푸시 메시지의 시간
     * @param isSuccess 성공/실패 콜백 여부
     */
    void callBackProcess(Long timestamp, boolean isSuccess);
}
