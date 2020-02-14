import {NativeModules, Linking, Alert, Platform} from 'react-native';

const {RNRemind} = NativeModules;

class Map {

    getMaps() {
        return new Promise((okCallback, errorCallback) => {
            let items = [];
            if (Platform.OS == 'ios') {
                items.push({key: 'iosmap', name: '苹果地图'});
                Linking.canOpenURL('iosamap://').then((res) => {
                    if (res) {
                        items.push({key: 'iosamap', name: '高德地图'});
                    }
                    Linking.canOpenURL('baidumap://').then((res) => {
                        if (res) {
                            items.push({key: 'baidumap', name: '百度地图'});
                        }

                        okCallback(items);
                    });
                });
            }
        });

    }

    openMap(key, address) {
        if (Platform.OS == 'ios') {
            switch (key) {
                case 'iosmap':
                    Linking.openURL(`http://maps.apple.com/?daddr=${address}`);
                    break;
                case 'iosamap':
                    Linking.openURL(`iosamap://path?sname=我的位置&dname=${address}&dev=0&t=0&sid=BGVIS1&did=BGVIS2`);
                    break;
                case 'baidumap':
                    Linking.openURL(`baidumap://map/direction?origin={{我的位置}}&destination=${address}`);
                    break;

            }
        }
    }
};

export default new Map();
