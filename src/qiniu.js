import {NativeModules} from 'react-native';

const {RNQiNiu} = NativeModules;

class QNManage {

    upload(files: Array, token, dir = '') {

        return new Promise(function (callback, errorCallback) {
            RNQiNiu.upload(files, token, dir)
                .then((data) => {

                    callback(data);
                }, (error) => {

                    errorCallback('上传失败');
                });
        });
    }
}

let QiNiu = new QNManage();
export default QiNiu;
