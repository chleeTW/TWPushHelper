TWPushHelper
=====



## Installation
-----
Gradle 파일 수정 :
```gradle
repositories {
    maven { url "https://jitpack.io" }

}

dependencies {
    compile 'com.github.chleeTW:TWPushHelper:1.0.1'
}
```



## How to use

##### 1. Start Activity
Add your request code for `startActivityForResult()` and start `ImagePickerActivity`

```javascript

    private static final int INTENT_REQUEST_GET_IMAGES = 13;

    private void getImages() {

        Intent intent  = new Intent(this, ImagePickerActivity.class);
        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);

    }

```


##### 2. Receive Activity
If you finish image select, you will recieve image path array (Uri type)
```javascript

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

            if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK ) {

                ArrayList<Uri>  image_uris = intent.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                //do something
            }
    }

```

