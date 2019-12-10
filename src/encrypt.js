import {NativeModules} from 'react-native';

const {RNEncrypt} = NativeModules;

class EncryptManage {

    string2GBK(str, cb) {

        Encryption.string2GBK(str, (data) => {
            cb && cb(data);
        });
    }

    encode(str, key) {

        return new Promise(function (callback, errorCallback) {
            Encryption.encode(str, key)
                .then((data) => {

                    callback(data);
                });
        });
    }

    decode(str, key) {

        return new Promise(function (callback, errorCallback) {
            Encryption.decode(str, key)
                .then((data) => {

                    callback(data);
                }, (error) => {

                    errorCallback(error);
                });
        });
    }
}

let Encrypt = new EncryptManage();
export default Encrypt;
