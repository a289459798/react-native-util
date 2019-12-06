
# react-native-util

## Getting started

`$ npm install react-native-util --save`

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


## Usage
```javascript
import RNUtil from 'react-native-util';

// TODO: What to do with the module?
RNUtil;
```
  