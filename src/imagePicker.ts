import {NativeModules, Platform, PermissionsAndroid} from 'react-native';
import CameraRoll from '@react-native-camera-roll/camera-roll';
import file from './file';
import RNFS from 'react-native-fs';

const {RNICImagePicker} = NativeModules;

class ICImagePicker {

    open(params = {}, select: any[] = [], callback: Function) {
        RNICImagePicker.open(params, select, callback);
    }

    save(url: string) {
        if (Platform.OS === 'ios') {
            return RNICImagePicker.save(url);
        } else {
            return new Promise((callback, errorCallback) => {
                PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE).then((res: boolean) => {
                    if (!res) {
                        PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
                            {
                                title: '申请写入权限',
                                message:
                                    '需要保存图片到相册',
                                buttonNegative: '残忍拒绝',
                                buttonPositive: '同意',
                            }).then((granted: string) => {
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

    _saveWithAndroid(url: string, callback: Function, errorCallback: Function) {
        if (url.indexOf('http') === 0) {
            file.download('share', 'share.png', url).then(() => {
                setTimeout(() => {
                    CameraRoll.saveToCameraRoll('file://' + RNFS.ExternalDirectoryPath + '/share/share.png').then(() => {
                        callback();
                    }, (error: any) => {
                        errorCallback(error);
                    });
                }, 300);
            }, (error: any) => {
                errorCallback(error);
            });
        } else {
            file.write('share', 'share.png', url, 'base64').then(() => {
                CameraRoll.saveToCameraRoll('file://' + RNFS.ExternalDirectoryPath + '/share/share.png').then(() => {
                    callback();
                }, (error: any) => {
                    errorCallback(error);
                });
            }, (error: any) => {
                errorCallback(error);
            });
        }
    }

}

export default new ICImagePicker();

