package kr.co.chlee.twpushhelper

import android.content.Context
import org.json.JSONObject


/**
 * @author chlee
 * @date 2018-02-23
 * API 호출 시 필요한 파라미터 값인 start_timestamp을 저장하기 위해서 SharedPreferences를 활용.
 * 해당 값의 초기화는 로그인 성공 후 동기화된 서버시간을 기준으로 설정함.
 * 2018-04-16 convert code java to kotlin by chlee
 */
open class TWPushHelper constructor(private val mContext: Context): ITWPushHelper{

    private val MQTT_TIME_KEY = "registered_timestamp_in_mill"
    private var receivedMqttTime: Long = -1L
    private var lock = false

    /**
     * getContext
     * @return context
     */
    fun getContext(): Context {
        return mContext
    }


    override fun apiCall(timestamp: Long) {
    }

    override fun setTimestamp(timestamp: Long) {
    }

    override fun getTimestamp(): Long {
        return -1
    }

    override fun handlePushMessage(message: JSONObject): Boolean {
        return false
    }

    /**
     * 푸시 메시지 수신시 처리
     * @param payload 푸시 메시지의
     */
    fun putPush(payload: JSONObject) {
        if (!payload.has(MQTT_TIME_KEY)) return
        val timestamp = payload.getLong(MQTT_TIME_KEY)
        putTimeOfPush(timestamp)
    }
    /**
     * 푸시 메시지의 시간을 추가한다.
     * @param timestamp 새로운 푸시 메시지의 시간
     */
    private fun putTimeOfPush(timestamp: Long) {
        if (!lock) {
            getPayloadFromServer(timestamp)
        }
        setPushTime(timestamp)
    }
    /**
     * 마지막으로 전달받은 푸시 메시지의 시간을 새로운 푸시 메시지 시간로 교체
     * @param time 새로운 MQTT 시간
     */
    private fun setPushTime(time: Long) {
        if (time > receivedMqttTime) {
            receivedMqttTime = time
        }
    }
    /**
     * lock 상태 변경
     * @param state 상태
     */
    private fun changeLockState(state: Boolean) {
        lock = state
    }
    /**
     * 서버로 푸시 메시지의 내용을 받도록 API 요청
     * @param timestamp 호출할 푸시 메시지의 시간
     */
    private fun getPayloadFromServer(timestamp: Long) {
        Thread(Runnable {
            val lastSuccessTimestamp = getTimestamp()!!
            if (timestamp > lastSuccessTimestamp) {
                changeLockState(true)
                apiCall(timestamp)
            }
        }).start()
    }
    /**
     * API 콜백 받은 이후의 처리
     * lock을 풀어주고, 새로운 푸시 메시지를 수신받은 경우 API를 재호출한다.
     * @param timestamp 해당 API에서 사용한 푸시 메시지의 시간
     * @param isSuccess 성공/실패 콜백 여부
     */
    fun callBackProcess(timestamp: Long, isSuccess: Boolean) {
        if (isSuccess) setTimestamp(timestamp)
        changeLockState(false)
        if (receivedMqttTime > timestamp) {
            getPayloadFromServer(receivedMqttTime)
        }
    }
}