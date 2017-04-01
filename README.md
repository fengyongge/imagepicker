# imagepicker(图片选择器)
## 图片选择器简介
* 从相册里面选择图片或者拍照获取照片
* 浏览所选图片
* 保存所选图片

## 如何引用
* 配置gradle依赖
```java
compile 'com.zzti.fengyongge:imagepicker:1.0'
```
* 配置清单文件权限
```java
<!-- 从sdcard中读取数据的权限 -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<!-- 往sdcard中写入数据的权限 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
* 配置清单文件所需activity
```java
<activity android:name="com.zzti.fengyongge.imagepicker.PhotoSelectorActivity"></activity>//选择图片
<activity android:name="com.zzti.fengyongge.imagepicker.PhotoPreviewActivity"></activity>//预览图片
```

## 如何使用
* 拍照或者从图库选择图片
```java
Intent intent = new Intent(MainActivity.this, PhotoSelectorActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
intent.putExtra("limit", number );//number是选择图片的数量
startActivityForResult(intent, 0);
```
* 获取拍照或者图片地址
```java
  	@Override
  	public void onActivityResult(int requestCode, int resultCode, Intent data) {
  		switch (requestCode) {
  		case 0:
			if (data != null) {
				List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
				//处理代码

	  	}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		}
```		
* 浏览图片
```java
private List<PhotoModel> single_photos = new ArrayList<PhotoModel>();//存放对象list
int position;
//预览
Bundle bundle = new Bundle();
bundle.putSerializable("pics",(Serializable)single_photos);
bundle.putInt("position", position);//position预览图片地址
bundle.putString("save","save");//save表示可以保存预览图片
CommonUtils.launchActivity(MainActivity.this, PhotoPreviewActivity.class, bundle);
```	
## 实际效果
![](https://raw.githubusercontent.com/917386389/imagepickerdemo/master/app/src/4.gif)



## 关于作者
```java
Log.i("name", "fsuper");
Log.i("email", "fengyongge98@gmail.com");
Log.i("motto1", "可以让步，却不可以退缩，可以羞涩，却不可以软弱");
Log.i("motto2", "纸上得来终觉浅 绝知此事要躬行");
```	

## License
```
Copyright 2016 fengyongge

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```



