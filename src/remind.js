import {NativeModules} from 'react-native';

const {RNRemind} = NativeModules;

class RemindManage {

    notify(title, time, userInfo = {}) {
        
    }
}

let Remind = new RemindManage();
export default Remind;
