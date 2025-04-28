import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-android-gallery-picker' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ android: "- You have run 'react-native link react-native-android-gallery-picker'\n" }) +
  '- You rebuilt the app after installing the package\n';

interface PhotoPickerOptions {
  selectionLimit?: number;
  mediaType?: 'photo' | 'video' | 'mixed';
}

interface PhotoPickerResult {
  uri: string;
}

const PhotoPicker = NativeModules.PhotoPicker
  ? NativeModules.PhotoPicker
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export default class GalleryPicker {
  static async pickMedia(options: PhotoPickerOptions = {}): Promise<PhotoPickerResult | PhotoPickerResult[]> {
    if (Platform.OS !== 'android') {
      throw new Error('GalleryPicker is only supported on Android');
    }
    return await PhotoPicker.pick(options);
  }
}

export type { PhotoPickerOptions, PhotoPickerResult };