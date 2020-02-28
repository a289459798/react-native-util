/**
 * Created by zhangzy on 16/8/24.
 */
'use strict';

import {
    Platform,
} from 'react-native';
import RNFS from 'react-native-fs';

class file {

    async download(dir = '', file, data) {

        let path = this.getCachePath() + dir;
        return await RNFS.exists(path).then(exists => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return this._download(path + file, data);
                });
            }

            return this._download(path + file, data);
        });
    }

    getCachePath() {
        return Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath + '/Caches/' : RNFS.ExternalDirectoryPath;
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
        return RNFS.exists(this.getCachePath() + dir + '/' + file);
    }

    async write(dir, file, data, encoding = 'utf8') {
        let path = this.getCachePath() + dir;
        console.log(path);
        return await RNFS.exists(path).then(exists => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return RNFS.writeFile(path + file, data, encoding);
                });
            }

            return RNFS.writeFile(path + file, data, encoding);
        });

    }

    read(dir, file) {
        return RNFS.readFile(this.getCachePath() + dir + '/' + file);
    }

    readDir(dir = '', isAbsolute = false) {
        return RNFS.readDir(!isAbsolute ? this.getCachePath() + dir : dir);
    }

    unlink(path) {
        return RNFS.unlink(path);
    }

}

module.exports = new file();
