import {NativeModules, Platform} from 'react-native';
import CameraRoll from "@react-native-community/cameraroll";
import file from './file';

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
                file.download('share', 'share.jpg', data.image).then((res) => {
                    // CameraRoll.saveToCameraRoll('file://' + RNFS.CachesDirectoryPath + '/com.ichong.CloudMarket/share/store.jpg').then(() => {
                    //     callback();
                    // }, error => {
                    //     errorCallback(error);
                    // });
                }, (error) => {
                    errorCallback(error);
                });
            });
        }
    }
}

export default new ICImagePicker();

