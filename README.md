# AndroidPytorch
classification demo

<img src="https://github.com/wocks1123/AndroidPytorch/blob/master/img/result.jpg?raw=true" alt="img" width="320" height="640" />



```
API23 : Android 6.0 (Marshmallow) ↑
```



`build.gradle` 에서 `dependencies`에 pytorch dependecy 추가

```
// ...

dependencies {
	// ...

	implementation 'org.pytorch:pytorch_android:1.6.0'
	implementation 'org.pytorch:pytorch_android_torchvision:1.6.0'
}
```

`meanifests/AndroidManifest.xml`에 저장소 읽기/쓰기 권한 추가

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
		...
          
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
	
    <application
		...
                 
        android:requestLegacyExternalStorage="true"
        >
        
        ...
        
	</application>


</manifest>

```

`models/□□□.pt` 를 핸드폰 저장소에 복사