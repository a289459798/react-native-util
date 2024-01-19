import {NativeModules, Platform} from 'react-native';

const {RNQiNiu} = NativeModules;

class QNManage {

    upload(files: [], token: string, dir: string = '', zone: string = 'zone0') {
        return new Promise(function (callback, errorCallback) {
            if (Platform.OS === 'ios') {
                RNQiNiu.upload(files, token, dir).then((data: any) => {
                    callback(data);
                }, () => {
                    errorCallback({message: '上传失败'});
                });
            } else {
                RNQiNiu.upload(files, token, dir, zone).then((data: any) => {
                    callback(data);
                }, () => {
                    errorCallback({message: '上传失败'});
                });
            }
        });
    }

}

export default new QNManage();
