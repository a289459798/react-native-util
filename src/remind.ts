import {NativeModules, Linking, Alert, Platform} from 'react-native';
import PushNotificationIOS from '@react-native-community/push-notification-ios';

const {RNRemind} = NativeModules;

class Remind {

    notify(title: string, time: Date, userInfo = {}) {
        this.notifyEnabled((state: boolean) => {
            if (state) {
                if (Platform.OS === 'ios') {
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

    delNotify(time: any, userInfo = {}) {
        if (Platform.OS === 'ios') {
            PushNotificationIOS.cancelLocalNotifications({content: JSON.stringify(userInfo)});
        } else {
            RNRemind.cancelNotify(time);
        }
    }

    notifyEnabled(callback?: Function) {
        if (Platform.OS === 'ios') {
            PushNotificationIOS.checkPermissions((data: any) => {
                callback?.(data.alert);
            });
        } else {
            RNRemind.notifyEnabled(callback);
        }
    }

    openSetting() {
        Alert.alert('提示', '需要开启/关闭推送功能', [
            {
                text: '取消', onPress: () => {
                }, style: 'cancel',
            },
            {
                text: '确定', onPress: () => {
                    if (Platform.OS === 'ios') {
                        Linking.openURL('app-settings:');
                    } else {
                        RNRemind.openSetting();
                    }
                },
            },
        ]);
    }

}

export default new Remind();
