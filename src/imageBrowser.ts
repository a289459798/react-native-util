import {NativeModules} from 'react-native';

const {RNImageBrowser} = NativeModules;

export default {

    show: (images: [], index = 0) => {
        RNImageBrowser.show(images, index);
    },

};


