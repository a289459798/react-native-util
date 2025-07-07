import {DeviceEventEmitter} from 'react-native';

export default {

    emit: (eventType: string, data: any) => {
        return DeviceEventEmitter.emit(eventType, data);
    },

    addListener: (type: string, listener: (data: any) => void, context?: any) => {
        return DeviceEventEmitter.addListener(type, listener);
    },

}
