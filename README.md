
# react-native-util

集成常用的工具组件

为开源事业做一份绵薄之力，欢迎加入群：161263093

作者：zhangzy  QQ：289459798  微信：zhangzy816


## 效果
![image](/pic/map.GIF)
![image](/pic/crop.GIF)
![image](/pic/image.GIF)


## Getting started

`$ npm install git+https://gitee.com/petdoctor/react-native-util.git --save`

### Mostly automatic installation

`$ react-native link react-native-util`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-util` and add `RNUtil.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNUtil.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.ichong.zzy.util.RNUtilPackage;` to the imports at the top of the file
  - Add `new RNUtilPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-util'
  	project(':react-native-util').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-util/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-util')
  	```

## 功能

- 七牛上传
- 加解密和编码
- App检测更新
- app通知提醒
- 事件监听
- 网络请求封装
- 本地存储封装
- 第三方地图调用
- 图片选择与裁剪
- 文件系统

## Usage

#### 七牛上传
```javascript
import {QiNiu} from '@ichong/react-native-util';

/**
* images 图片对象{"img1": {"uri": ""}, "img2": {"uri": ""}}
* token 上传凭证，一般从服务端获取
* return promise 上传成功后的文件名
*/
QiNiu.upload(images, token).then((res) => {
    // 成功
}, error => {
    // 失败
});
```

#### 加解密与编码

md5 和 base64 马上增加


#### App检测更新

android 基于bugly，ios 在苹果商店查询

```javascript
import {Update} from '@ichong/react-native-util';

/**
* showDialog 是否显示更新弹层
* AppId ios 对应的应用id，在苹果后台查询
* return promise 上传成功后的文件名
*/
Update.check(showDialog, AppId);
```

#### app通知提醒

应用内的本地通知

```javascript
import {Remind} from '@ichong/react-native-util';

/**
* title 通知标题
* time 定时提醒时间
* userInfo 传递的数据如果没有开启会自动跳转到设置界面
* 
* 该方法内置了通知权限检查，
*/
Remind.notify(title, time, userInfo = {});
```

#### 事件监听

官方 `NativeAppEventEmitter `的别名，使用方式一样，简化代码


```javascript
import {Event} from '@ichong/react-native-util';


Event.addListener("name", () => {};
Event.emit("name");
```


#### 网络请求封装

该类只能被继承，不能直接拿来使用

支持post、get、put、delete 常用请求方式

```javascript
import {Http, Storage} from '@ichong/react-native-util';

/**
* model 基类
*/
export default class Base extends Http {
    
    constructor(props) {
        if (new.target === Base) {
            throw new Error('不能直接实例化');
        }

        /**
        * 调用父类构造方法设置域名与请求头
        */
        super(Config.HOST, {
            'User-Agent': `${Platform.OS}/${DeviceInfo.getSystemVersion()} Academy/${DeviceInfo.getVersion()}`,
            'Content-Type': 'application/json',
        });
    }
    
    /**
    * 服务端返回401或403 执行该函数，用于登录过期全局监听
    */
    noAccess() {
        Storage.delete('user');
    }

    /**
    * 动态设置请求头
    * @returns {{Authorization: string}}
    */
    getHeaders() {
        return {
            'Authorization': user && user != null && user != 'null' ? 'Bearer ' + user.token : '',
        };
    }

}

/**
* 具体业务的model
*/
export default new class AssembleModel extends Base {

    // 购物车列表
    list(id): Promise {
        return this.post(`/api/v1/shopping-cart`, {id: id});
    }

};

```

#### 本地存储封装

基于 `@react-native-community/async-storage` 的封装

#### 第三方地图调用

用于调起本地的第三方地图，包含苹果自带、百度和高德

```javascript

import {Map} from '@ichong/react-native-util';

/**
* 获取手机已安装的地图app
*/
Map.getMaps();

/**
* 打开对应的地图
* key 地图标识，由Map.getMaps返回
* add 需要导航的目的地
*/
Map.openMap(key, add);


```

#### 图片选择与裁剪

- ios 基于TZImagePickerController
- android 基于 PictureSelector

```javascript

import {ICImagePicker} from '@ichong/react-native-util';

/**
* max 最大选择数量
* crop 是否开启裁剪
* quality 压缩比例，1为不压缩
* base64 是否返回base64
* 
*/
ICImagePicker.open({max: 1, crop: true}, null, (images) => {
    console.log(images)
});


```

#### 文件系统

用户文件的读写、远程文件下载、缓存清除等

```javascript

import {ICFile} from '@ichong/react-native-util';

/**
* 文件下载
*/
ICFile.download(dir, filename, url).then((res) => {
    CameraRoll.saveToCameraRoll('file://' + ICFile.getCachePath() + dir + "/" + filename).then(() => {
        callback();
    }, error => {
        errorCallback(error);
    });
}, (error) => {
    errorCallback(error);
});

/**
 * 获取缓存大小
 */
getCache(path = ICFile.getCachePath(), isCount = true) {

    ICFile.readDir(path, true).then(res => {

        res.forEach((v) => {
            if (v.isDirectory()) {
                this.getCache(v.path, false);
            } else {
                this.cache += v.size;
            }
        });
    });

    if (isCount) {

        this.setTimeout(() => {
            this.setState({cache: (this.cache / 1024 / 1024).toFixed(2)});
        }, 2000);
    }
}

/**
* 清除缓存
*/
ICFile.readDir(path, true).then(res => {

    res.forEach((v) => {
        if (v.isDirectory()) {
            this.clearCache(v.path);
        } else {
            this.cache -= v.size;
            ICFile.unlink(v.path).then(() => {
                this.setState({cache: (this.cache / 1024 / 1024).toFixed(2)});
            });
        }
    });
});

```
