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

        let path = Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath : RNFS.ExternalDirectoryPath + '/cache/' + dir;
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
        return RNFS.exists(Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath : RNFS.ExternalDirectoryPath + '/cache/' + dir + '/' + file);
    }

    async write(dir, file, data, encoding = 'utf8') {
        let path = Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath : RNFS.ExternalDirectoryPath + '/cache/' + dir;
        console.log(path)
        return await RNFS.exists(path).then(exists => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return RNFS.writeFile(path + '/' + file, data, encoding);
                });
            }

            return RNFS.writeFile(path + '/' + file, data, encoding);
        });

    }

    read(dir, file) {
        return RNFS.readFile(Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath : RNFS.ExternalDirectoryPath + '/cache/' + dir + '/' + file);
    }

    readDir(dir = '', isAbsolute = false) {
        return RNFS.readDir(!isAbsolute ? Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath : RNFS.ExternalDirectoryPath + '/cache/' : dir);
    }

    unlink(path = Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath : RNFS.ExternalDirectoryPath + '/cache/') {
        return RNFS.unlink(path);
    }

}

module.exports = new file();
