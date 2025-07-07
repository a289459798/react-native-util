import {NativeEventEmitter, NativeModules} from 'react-native';
const {RNUtil} = NativeModules;

class Event extends NativeEventEmitter {

    // 构造
    constructor() {
        super(RNUtil);
    }
}

export default new Event();
