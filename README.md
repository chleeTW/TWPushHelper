TWPushHelper
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
    compile 'com.github.chleeTW:TWPushHelper:1.0.6'
}
```
<br/>



## How to use
#### 1. TWPushHelperImpl를 상속받은 PushHelper 클래스 생성
```javascript
public class PushHelper extends TWPushHelperImpl {
    private static PushHelper instance = new PushHelper(/** Add user Application context **/); 
    public static PushHelper getInstance(){
        return instance;
    }
    public TWPushHelper(Context context) {
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


#### >>> Todo List
+ `Line2` : Applicaton Context 추가
+ `apiCall()` :  getStroedMessage API 요청하기 위한 코드를 추가 (성공 및 실패 콜백에 주석으로 작성된 코드 추가)
+ `setTimestamp()` : 성공한 타임스템프를 저장하기위한 코드 추가 (예를들어 SharedPreference 활용)
+ `getTimestamp()` : 마지막으로 성공한 타임스템프를 가져오기 위한 코드 추가 (예를들어 SharedPreference 활용)
+ `handlePushMessage()` : getStroedMessage API 요청 결과로 받은 메시지의 JSONObect를 처리하기위한 코드 추가
<br/>



#### 2. FCM의 onMessageReceived(), MQTT의 messageArrived()등 Push 받은 메시지의 JSONObject를 PushHelper.putPush()로 전달
```javascript
PushHelper.getInstance().putPush(jsonObjectMessage);
```
<br/>



#### 3. 유저 로그인 성공 및 동기화 완료시 타임스템프 값을 확인. 해당 값이 없을 경우 로그인 및 동기화 완료 당시 서버 시간으로 초기화
```javascript
if(PushHelper.getInstance().getTimestamp() <= 0) {
    PushHelper.getInstance().setTimestamp(/** 동기화된 서버 시간 **/);
}
```
- - -
###### cf. 모이고에서는 (동기화된서버시간-1초)로 초기화하고 있음
###### 1초 전으로 설정한 이유는 SharedPreferences 초기화 사이의 시간 동안 MQTT가 도착할 가능성이 있으며, 혹여 '(연결성공시간-1초) ~ (연결성공시간)' 사이에 있는 데이터가 클라에서 이미 처리한 MQTT라 하더라도, 현재 아래의 경우를 제외하면 MQTT를 중복 처리 한다 하더다도 문제가 없음. (향후 MQTT 추가시 중복처리 이슈에 대한 고려가 필요) 
###### >> MQTT 중복처리를 함으로써 문제가 생기는 부분은 '그룹채팅 읽음 MQTT'이며, 차후 읽음 처리 로직 변경 후에는 이상이 없음.
- - -
