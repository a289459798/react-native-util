import {NativeAppEventEmitter} from 'react-native';

export default {
    emit: (eventType: string, data: any) => {
        return NativeAppEventEmitter.emit(eventType, data);
    },
    addListener: (type: string, listener: (data: any) => void, context?: any) => {
    return NativeAppEventEmitter.addListener(type, listener);
    },
};
