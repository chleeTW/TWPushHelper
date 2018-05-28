TWPushHelper [![](https://jitpack.io/v/chleeTW/TWPushHelper.svg)](https://jitpack.io/#chleeTW/TWPushHelper)
=====
##### - FCM, MQTT 등 서버로부터 푸시받은 시간과 이전 성공시간 사이에 stored_message를 가져오도록 도와주는 라이브러리.
-----
<br/>

## Installation
Edit Gradle file :

Add it in your root build.gradle at the end of repositories
```gradle
allprojects {
    repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```gradle
dependencies {
    implementation 'com.github.chleeTW:TWPushHelper:2.0.5'
}
```
<br/>



## How to use
#### 1. TWPushHelper를 상속받은 PushHelper 클래스 생성

Kotlin Code :
```javascript
object PushHelper: TWPushHelper() {
    override fun apiCall(timestamp: Long) {
        // Add user api call code
        // Add following code in onSucess callback
        //  callBackProcess(timestamp, handlePushMessage(response))
        // Add following code in onFailure callback
        //  callBackProcess(timestamp, false)
    }
    override fun setTimestamp(timestamp: Long) {
        // Add code to store successed timestamp on the device (like SharedPreference)
    }
    override fun getTimestamp(): Long {
        retuen /** Add code to get A from device (like SharedPreference) **/
    }
    override fun handlePushMessage(message: JSONObject): Boolean {
        // Add code to handle the JSONObject received as a result of the API request
    }
```

Java Code :
```javascript
public class PushHelper extends TWPushHelper {
    private static PushHelper instance = new PushHelper(); 
    public static PushHelper getInstance(){
        return instance;
    }
    private PushHelper() {}
    @Override
    public void apiCall(Long timestamp) {
        // Add user api call code
        // Add following code in onSucess callback
        //  callBackProcess(timestamp, handlePushMessage(response));
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
    public boolean handlePushMessage(JSONObject jsonObject) {
        // Add code to handle the JSONObject received as a result of the API request
    }
}
```
<br/>


#### >>> Todo List
+ `apiCall()` :  getStroedMessage API 요청하기 위한 코드를 추가 (성공 및 실패 콜백에 주석으로 작성된 코드 추가)
+ `setTimestamp()` : 성공한 타임스템프를 저장하기위한 코드 추가 (예를들어 SharedPreference 활용)
+ `getTimestamp()` : 마지막으로 성공한 타임스템프를 가져오기 위한 코드 추가 (예를들어 SharedPreference 활용)
+ `handlePushMessage()` : getStroedMessage API 요청 결과로 받은 메시지의 JSONObect를 처리하기위한 코드 추가
<br/>



#### 2. FCM의 onMessageReceived(), MQTT의 messageArrived()등 Push 받은 메시지의 JSONObject를 PushHelper.putPush()로 전달
TWPushHelper(Kotlin or Java) Using Kotlin Code :
```javascript
PushHelper.putPush(jsonObjectMessage)
```
TWPushHelper(Kotlin) Using Java Code :
```javascript
PushHelper.INSTANCE.putPush(jsonObjectMessage);
```
TWPushHelper(Java) Using Java Code :
```javascript
PushHelper.getInstance().putPush(jsonObjectMessage);
```
<br/>



#### 3. 유저 로그인 성공 및 동기화 완료시 타임스템프 값을 확인. 해당 값이 없을 경우 로그인 및 동기화 완료 당시 서버 시간으로 초기화

TWPushHelper(Kotlin or Java) Using Kotlin Code :
```javascript
if(PushHelper.getTimestamp() <= 0) {
    PushHelper.setTimestamp(/** 동기화된 서버 시간 **/);
}
```
TWPushHelper(Kotlin) Using Java Code :
```javascript
if(PushHelper.INSTANCE.getTimestamp() <= 0) {
    PushHelper.INSTANCE.setTimestamp(/** 동기화된 서버 시간 **/);
}
```
TWPushHelper(Java) Using Java Code :
```javascript
if(PushHelper.getInstance().getTimestamp() <= 0) {
    PushHelper.getInstance().setTimestamp(/** 동기화된 서버 시간 **/);
}
```

- - -
