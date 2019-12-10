import {NativeAppEventEmitter} from 'react-native';

export default {
    emit: (eventType: string) => {
        return NativeAppEventEmitter.emit(eventType);
    },
    addListener: (type: string, listener: (data: any) => void, context?: any) => {
        return NativeAppEventEmitter.addListener(type, listener);
    },
};
