import {NativeModules} from 'react-native';

const {RNEncrypt} = NativeModules;

class EncryptManage {

    string2GBK(str: string, callback?: Function) {
        RNEncrypt.string2GBK(str, (data: any) => {
            callback?.(data);
        });
    }

    encode(str: string, key: string) {
        return new Promise(function (callback, errorCallback) {
            RNEncrypt.encode(str, key).then((data: any) => {
                callback(data);
            });
        });
    }

    decode(str: string, key: string) {
        return new Promise(function (callback, errorCallback) {
            RNEncrypt.decode(str, key).then((data: any) => {
                callback(data);
            }, (error) => {
                errorCallback(error);
            });
        });
    }

}

export default new EncryptManage();
