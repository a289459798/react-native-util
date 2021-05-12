import {NativeModules} from 'react-native';

const {RNEncrypt} = NativeModules;

class EncryptManage {

    string2GBK(str, callback) {
        RNEncrypt.string2GBK(str, (data) => {
            callback && callback(data);
        });
    }

    encode(str, key) {
        return new Promise(function (callback, errorCallback) {
            RNEncrypt.encode(str, key).then((data) => {
                callback(data);
            });
        });
    }

    decode(str, key) {
        return new Promise(function (callback, errorCallback) {
            RNEncrypt.decode(str, key).then((data) => {
                callback(data);
            }, (error) => {
                errorCallback(error);
            });
        });
    }

}

export default new EncryptManage();
