import AsyncStorage from '@react-native-community/async-storage';

export default {

    set: (key, value) => {
        if (typeof value === 'object') {
            value = JSON.stringify(value);
        }
        return AsyncStorage.setItem(key, value);
    },

    get: (key) => {
        return new Promise((callback, errorCallback) => {
            AsyncStorage.getItem(key).then((data) => {
                callback(JSON.parse(data));
            }, errorCallback);
        });
    },

    merge: (key, value) => {
        return AsyncStorage.mergeItem(key, value);
    }

    delete: (key) => {
        return AsyncStorage.removeItem(key);
    },

    clear: () => {
        return AsyncStorage.clear();
    },
};
