package kr.co.chlee.twpushhelper

import org.json.JSONObject

/**
 * Created by Twinny013 on 2018-05-28.
 */
internal interface ITWPushHelper {

    /**
     * Server 에서 마지막으로 성공한(푸시 메시지 처리) 시간부터 새로운 푸시 메시지의 시간까지 해당하는 메시지들을 받아와서 처리하도록 구현해야함
     * API 응답 성공시에만 마지막으로 성공한 타임스템프를 변경
     * @param timestamp 호출할 푸시 메시지의 시간
     */
    abstract fun apiCall(timestamp: Long)
    /**
     * SharedPreferences에 MQTT 타임스템프를 저장하도록 구현해야함
     * @param timestamp 새로운 MQTT의 시간
     */
    abstract fun setTimestamp(timestamp: Long)
    /**
     * SharedPreferences에 저장된 MQTT 타임스템프를 반환하도록 구현해야함
     */
    abstract fun getTimestamp(): Long
    /**
     * API 요청 결과로 받은 메시지의 JSONObect를 처리하도록 구현해야함
     * @return 처리 성공여부
     */
    abstract fun handlePushMessage(message: JSONObject): Boolean
}