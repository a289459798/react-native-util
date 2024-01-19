/**
 * Created by zhangzy on 16/8/24.
 */
'use strict';

import {
    Platform,
} from 'react-native';
import RNFS from 'react-native-fs';

class file {

    async download(dir: string = '', file: string, data: string) {
        let path = this.getCachePath() + dir + '/';
        return await RNFS.exists(path).then((exists: boolean) => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return this._download(path + file, data);
                });
            }
            return this._download(path + file, data);
        });
    }

    getCachePath() {
        return Platform.OS === 'ios' ? RNFS.LibraryDirectoryPath + '/Caches/' : RNFS.ExternalDirectoryPath + '/';
    }

    _download(path: string, data: string) {
        const options = {
            fromUrl: data,
            toFile: path,
            background: true,
        };
        return RNFS.downloadFile(options).promise;
    }

    exists(dir = '', file: string) {
        return RNFS.exists(this.getCachePath() + dir + '/' + file);
    }

    async write(dir: string, file: string, data: string, encoding: string = 'utf8') {
        let path = this.getCachePath() + dir + '/';
        return await RNFS.exists(path).then((exists: boolean) => {
            if (!exists) {
                return RNFS.mkdir(path).then(() => {
                    return RNFS.writeFile(path + file, data, encoding);
                });
            }
            return RNFS.writeFile(path + file, data, encoding);
        });
    }

    read(dir: string, file: string) {
        return RNFS.readFile(this.getCachePath() + dir + '/' + file);
    }

    readDir(dir: string = '', isAbsolute: boolean = false) {
        return RNFS.readDir(!isAbsolute ? this.getCachePath() + dir : dir);
    }

    unlink(path: string) {
        return RNFS.unlink(path);
    }

}

export default new file();
