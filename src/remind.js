import {NativeModules, Linking, Alert} from 'react-native';
import PushNotificationIOS from '@react-native-community/push-notification-ios';

const {RNRemind} = NativeModules;

class Remind {

    notify(title, time, userInfo = {}) {
        this.notifyEnabled((state) => {
            if (state) {
                if (Platform.OS == 'ios') {
                    PushNotificationIOS.scheduleLocalNotification({
                        alertBody: title,
                        alertAction: '查看',
                        userInfo: {content: JSON.stringify(userInfo)},
                        fireDate: time.toISOString(),
                    });
                } else {
                    RNRemind.notify(title, time.getTime());
                }
            } else {
                this.openSetting();
            }
        });

    }

    notifyEnabled(cb) {
        if (Platform.OS == 'ios') {
            PushNotificationIOS.checkPermissions((data) => {
                cb && cb(data.alert);
            });
        } else {
            RNRemind.notifyEnabled(cb);
        }
    }

    openSetting() {
        Alert.alert('提示', '需要开启推送功能', [
            {
                text: '取消', onPress: () => {
                }, style: 'cancel',
            },
            {
                text: '确定', onPress: () => {
                    if (Platform.OS == 'ios') {
                        Linking.openURL('app-settings:');
                    } else {
                        RNRemind.openSetting();
                    }
                },
            },
        ]);
    }
};

export default new Remind();
