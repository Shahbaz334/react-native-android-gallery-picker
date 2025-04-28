"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const react_native_1 = require("react-native");
const LINKING_ERROR = `The package 'react-native-android-gallery-picker' doesn't seem to be linked. Make sure: \n\n` +
    react_native_1.Platform.select({ android: "- You have run 'react-native link react-native-android-gallery-picker'\n" }) +
    '- You rebuilt the app after installing the package\n';
const PhotoPicker = react_native_1.NativeModules.PhotoPicker
    ? react_native_1.NativeModules.PhotoPicker
    : new Proxy({}, {
        get() {
            throw new Error(LINKING_ERROR);
        },
    });
class GalleryPicker {
    static async pickMedia(options = {}) {
        if (react_native_1.Platform.OS !== 'android') {
            throw new Error('GalleryPicker is only supported on Android');
        }
        return await PhotoPicker.pick(options);
    }
}
exports.default = GalleryPicker;
