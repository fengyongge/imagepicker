# imagepicker(图片选择器)
## 图片选择器简介
imagepicker是一款用于在Android设备上获取照片（拍照或从相册、文件中选择）、压缩图片的开源工具库，目前最新版本[V1.2.0](https://github.com/fengyongge/imagepicker)。

* 从相册里面选择图片或者拍照获取照片
* 浏览选择的本地或者网络图片
* 保存图片

## 更新说明
v1.2.0(2017/7/11)
-----------------
1. 权限适配6.0，添加动态权限申请
2. 优化项目

v1.1.0(2017/6/15)
-----------------
1. 修复图库无图片崩溃
2. 修复三星手机拍照崩溃
3. 修复部分手机（以小米为主）选择图片后内存溢出

v1.0(2016/8/4)
-----------------
1. 新增从相册里面选择图片或者拍照获取照片
2. 新增浏览选择的本地或者网络图片
3. 新增保存图片


## 如何引用
* 配置gradle依赖
```java
compile 'com.zzti.fengyongge:imagepicker:1.2.0'
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
 List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
//PhotoModel 开发者将自己本地bean的list封装成PhotoModel的list，PhotoModel属性源码可查看
 Bundle bundle = new Bundle();
 bundle.putSerializable("photos",(Serializable)single_photos);
 bundle.putInt("position", position);//position预览图片地址
 bundle.putBoolean("isSave",true);//isSave表示是否可以保存预览图片，建议只有预览网络图片时设置true
 CommonUtils.launchActivity(PreViewActivity.this, PhotoPreviewActivity.class, bundle);
```
* 保存图片

 网络图片点击保存时，默认保存到内存卡，imagepicker文件夹下

## 实际效果
![](https://raw.githubusercontent.com/917386389/imagepickerdemo/master/app/src/4.gif)



## 关于作者
```java
Log.i("name", "fsuper");
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