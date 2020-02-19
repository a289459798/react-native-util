import {NativeModules, Platform, PermissionsAndroid} from 'react-native';
import CameraRoll from '@react-native-community/cameraroll';
import file from './file';
import RNFS from 'react-native-fs';

const {RNICImagePicker} = NativeModules;

class ICImagePicker {

    open(params = {}, select = [], callback = () => {
    }) {
        RNICImagePicker.open(params, select, callback);
    }

    save(url) {
        if (Platform.OS == 'ios') {
            return CameraRoll.saveToCameraRoll(url, 'photo');
        } else {
            return new Promise((callback, errorCallback) => {
                PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE).then((res) => {
                    if (!res) {
                        PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
                            {
                                title: '申请写入权限',
                                message:
                                    '需要保存图片到相册',
                                buttonNegative: '残忍拒绝',
                                buttonPositive: '同意',
                            }).then((granted) => {
                            if (granted === PermissionsAndroid.RESULTS.GRANTED) {
                                this._saveWithAndroid(url, callback, errorCallback);
                            } else {
                                errorCallback('用户拒绝授权');
                            }
                        });
                    } else {
                        this._saveWithAndroid(url, callback, errorCallback);
                    }
                });
            });
        }
    }

    _saveWithAndroid(url, callback, errorCallback) {
        if (url.indexOf('http') == 0) {
            file.download('share', 'share.png', url).then((res) => {
                CameraRoll.saveToCameraRoll('file://' + RNFS.ExternalDirectoryPath + '/cache/share/share.png').then(() => {
                    callback();
                }, error => {
                    errorCallback(error);
                });
            }, (error) => {
                errorCallback(error);
            });
        } else {
            file.write('share', 'share.png', url, 'base64').then((res) => {
                CameraRoll.saveToCameraRoll('file://' + RNFS.ExternalDirectoryPath + '/cache/share/share.png').then(() => {
                    callback();
                }, error => {
                    errorCallback(error);
                });
            }, (error) => {
                errorCallback(error);
            });
        }

    }
}

export default new ICImagePicker();

