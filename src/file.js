/**
 * Created by zhangzy on 16/8/24.
 */
'use strict';


import RNFS from 'react-native-fs';
import DeviceInfo from 'react-native-device-info';

class file {

    async download(dir = '', file, data) {

        let path = RNFS.CachesDirectoryPath + '/' + DeviceInfo.getBundleId + '/' + dir;
        return await RNFS.exists(path).then(exists => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return this._download(path + '/' + file, data);
                });
            }

            return this._download(path + '/' + file, data);
        });
    }

    _download(path, data) {
        const options = {
            fromUrl: data,
            toFile: path,
            background: true,
        };
        return RNFS.downloadFile(options);
    }

    exists(dir = '', file) {
        return RNFS.exists(RNFS.CachesDirectoryPath + '/' + DeviceInfo.getBundleId + '/' + dir + '/' + file);
    }

    async write(dir, file, data) {
        let path = RNFS.CachesDirectoryPath + '/com.ichong.CloudMarket/' + dir;
        return await RNFS.exists(path).then(exists => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return RNFS.writeFile(path + '/' + file, data);
                });
            }

            return RNFS.writeFile(path + '/' + file, data);
        });

    }

    read(dir, file) {
        return RNFS.readFile(RNFS.CachesDirectoryPath + '/' + DeviceInfo.getBundleId + '/' + dir + '/' + file);
    }

    readDir(dir = '', isAbsolute = false) {
        return RNFS.readDir(!isAbsolute ? RNFS.CachesDirectoryPath + '/' + DeviceInfo.getBundleId : dir);
    }

    unlink(path = RNFS.CachesDirectoryPath + '/' + DeviceInfo.getBundleId) {
        return RNFS.unlink(path);
    }

}

module.exports = new file();
