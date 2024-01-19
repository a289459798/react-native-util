import {NativeModules, Linking, Platform} from 'react-native';

const {RNMap} = NativeModules;

class Map {

    getMaps() {
        return new Promise((okCallback, errorCallback) => {
            let items = [];
            if (Platform.OS === 'ios') {
                items.push({key: 'iosmap', name: '苹果地图'});
                Linking.canOpenURL('iosamap://').then((res: boolean) => {
                    if (res) {
                        items.push({key: 'iosamap', name: '高德地图'});
                    }
                    Linking.canOpenURL('baidumap://').then((res: boolean) => {
                        if (res) {
                            items.push({key: 'baidumap', name: '百度地图'});
                        }
                        okCallback(items);
                    });
                });
            } else {
                RNMap.checkInstall('com.autonavi.minimap', (res: boolean) => {
                    if (res) {
                        items.push({key: 'iosamap', name: '高德地图'});
                    }
                    RNMap.checkInstall('com.baidu.BaiduMap', (res: boolean) => {
                        if (res) {
                            items.push({key: 'baidumap', name: '百度地图'});
                        }
                        okCallback(items);
                    });
                });
            }
        });
    }

    openMap(key: string, address: string) {
        switch (key) {
            case 'iosmap':
                if (Platform.OS === 'ios') {
                    Linking.openURL(`http://maps.apple.com/?daddr=${address}`);
                }
                break;
            case 'iosamap':
                if (Platform.OS === 'ios') {
                    Linking.openURL(`iosamap://path?sname=我的位置&dname=${address}&dev=0&t=0&sid=BGVIS1&did=BGVIS2`);
                } else {
                    RNMap.open('gaode', address);
                }
                break;
            case 'baidumap':
                if (Platform.OS === 'ios') {
                    Linking.openURL(`baidumap://map/direction?origin={{我的位置}}&destination=${address}`);
                } else {
                    RNMap.open('baidu', address);
                }
                break;
        }
    }

}

export default new Map();
