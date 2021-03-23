import {NativeModules, Platform} from 'react-native';

const {RNUpdate} = NativeModules;

class UpdateManage {

    check(showDialog = false, appid) {
        if (Platform.OS === 'ios') {
            return RNUpdate.check(appid, showDialog);
        } else {
            return RNUpdate.check(showDialog);
        }
    }

}

export default new UpdateManage();
