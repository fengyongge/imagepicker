# imagepicker(图片选择器)

![Bintray](https://img.shields.io/bintray/dt/917386389/androidlibs/imagepicker)[![Download](https://api.bintray.com/packages/917386389/androidlibs/imagepicker/images/download.svg) ](https://bintray.com/917386389/androidlibs/imagepicker/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## 图片选择器简介
imagepicker是一款用于在Android设备上获取照片（拍照或从相册、文件中选择）、压缩图片的开源工具库，目前最新版本[V1.4.4](https://github.com/fengyongge/imagepicker)。

* 从相册里面选择图片或者拍照获取照片
* 浏览选择的本地或者网络图片
* 保存图片

## 更新说明

[更新日志](https://github.com/fengyongge/imagepicker/blob/master/readme/updateLog.md)


## 如何引用
* 配置gradle依赖
```java
implementation 'com.zzti.fengyongge:imagepicker:1.4.4'
```

* 配置清单文件所需activity和provider权限
```java

   <!-- 引入imagepicker时需要注册的activity 分别为预览图片和选择图片-->
        <activity android:name="com.zzti.fengyongge.imagepicker.PhotoPreviewActivity" />
        <activity android:name="com.zzti.fengyongge.imagepicker.PhotoSelectorActivity" />

```

## 如何使用
* 拍照或者从图库选择图片
```java

    //获取单例，调用下面方法即可，具体可参考源码sample
     ImagePickerInstance.getInstance()

  /**
     * 对外图库选择图片,或者拍照选择图片方法
     * @param context
     * @param limit  选择图片张数
     * @param isShowCamera 是否支持拍照
     * @param requestCode
     */
    public void photoSelect(Context context, int limit, boolean isShowCamera,int requestCode) {
        Intent intent = new Intent(context, PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(LIMIT, limit);
        intent.putExtra(IS_SHOW_CAMERA, isShowCamera);
        CommonUtils.launchActivityForResult((Activity) context, intent, requestCode);
    }
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

  //获取单例，调用下面方法即可，具体可参考源码sample
     ImagePickerInstance.getInstance()

  /**
     * 对外开放的图片预览方法
     * @param context
     * @param tempList 浏览图片集合，注意！必须封装成imagepicker的bean，url支持网络或者本地
     * @param positon  角标
     * @param isSave 是否支持保存
     */
    public void photoPreview(Context context, List<PhotoModel> tempList, int positon, boolean isSave) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHOTOS, (ArrayList<PhotoModel>) tempList);
        bundle.putInt(POSITION, positon);
        bundle.putBoolean(IS_SAVE, isSave);
        CommonUtils.launchActivity(context, PhotoPreviewActivity.class, bundle);
    }


```
* 保存图片

 网络图片点击保存时，默认保存到内存卡，imagepicker文件夹下

## 实际效果
![](https://raw.githubusercontent.com/917386389/imagepickerdemo/master/app/src/4.gif)



## 关于作者
```java
Log.i("homepage", "http://fengyongge.github.io/");
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
