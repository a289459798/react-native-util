import {NativeModules, Platform} from 'react-native';

const {RNICImagePicker} = NativeModules;

class ICImagePicker {

    open(params = {}, select = [], callback = () => {}) {
        RNICImagePicker.open(params, select, callback);
    }
}

export default new ICImagePicker();

