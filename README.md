TWPushHelper
=====
<br/>

## Installation
-----
Edit Gradle file :
```gradle
repositories {
    maven { url "https://jitpack.io" }

}

dependencies {
    compile 'com.github.chleeTW:TWPushHelper:1.0.5'
}
```
<br/>





## How to use
<br/>



##### 1. PushHelperImpl를 상속받은 PushHelper 클래스 생성
```javascript
public class PushHelper extends PushHelperImpl {
    private static PushHelper instance = new PushHelper(/** Add user Application context **/); 
    public static PushHelper getInstance(){
        return instance;
    }
    public PushHelper(Context context) {
        super(context);
    }
    @Override
    public void apiCall(Long timestamp) {
        // Add user api call code
        // Add following code in onSucess callback
        //  try {
        //      handlePushMessage(response);
        //      callBackProcess(timestamp, true);
        //  } catch (JSONException e) {
        //      e.printStackTrace();
        //      callBackProcess(timestamp, false);
        //  }
        // Add following code in onFailure callback
        //  callBackProcess(timestamp, false);
    }

    @Override
    public void setTimestamp(Long timestamp) {
        // Add code to store successed timestamp on the device (like SharedPreference)
    }

    @Override
    public Long getTimestamp() {
        retuen /** Add code to get A from device (like SharedPreference) **/;
    }

    @Override
    public void handlePushMessage(JSONObject jsonObject) throws JSONException {
        // Add code to handle the JSONObject received as a result of the API request
    }
}
```
<br/>


##### >>> Todo List
+ `Line2` : Applicaton Context 추가
+ `apiCall()` :  getStroedMessage API 요청하기 위한 코드를 추가 (성공 및 실패 콜백에 주석으로 작성된 코드 추가)
+ `setTimestamp()` : 성공한 타임스템프를 저장하기위한 코드 추가 (예를들어 SharedPreference 활용)
+ `getTimestamp()` : 마지막으로 성공한 타임스템프를 가져오기 위한 코드 추가 (예를들어 SharedPreference 활용)
+ `handlePushMessage()` : getStroedMessage API 요청 결과로 받은 메시지의 JSONObect를 처리하기위한 코드 추가
<br/>



##### 2. FCM의 onMessageReceived(), MQTT의 messageArrived()등 Push 받은 메시지의 JSONObject를 PushHelper.putPush()로 전달
```javascript
PushHelper.getInstance().putPush(jsonObjectMessage);
```
<br/>



##### 3. 유저 로그인 성공 및 동기화 완료시 타임스템프 값을 확인. 해당 값이 없을 경우 당시 시간으로 초기화
```javascript
if(PushHelper.getInstance().getTimestamp() <= 0) {
    PushHelper.getInstance().setTimestamp(System.currentTimeMillis());
}
```
