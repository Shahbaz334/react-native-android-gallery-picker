# React Native Android Gallery Picker 📸  
*A modern, permission-friendly Android gallery picker for React Native, leveraging the native Android Photo Picker for seamless media selection.*  

--- 

## Features ✨  
- **Android 13+ Support**: Uses the native [Android Photo Picker](https://developer.android.com/training/data-storage/shared/photopicker) (no `READ_MEDIA` permissions required).  
- **Legacy Support**: Fallback to classic gallery picker for Android ≤12.  
- **Media Types**: Select images (`image/*`) and videos (`video/*`).  
- **Customizable**: Limit selection count, enable cropping, and more.  

--- 

## Installation 💻  

### 1. Install the Package:  
```bash 
yarn add react-native-android-gallery-picker  
# or  
npm install react-native-android-gallery-picker  
```

Usage 

import GalleryPicker from 'react-native-android-gallery-picker';

// Pick a single photo
const result = await GalleryPicker.pickMedia();
console.log(result.uri);

// Pick multiple photos
const results = await GalleryPicker.pickMedia({ selectionLimit: 5 });
results.forEach(item => console.log(item.uri));

// Pick videos
const video = await GalleryPicker.pickMedia({ mediaType: 'video' });

APi

### pickMedia(options?)
Opens the gallery picker with the specified options.
 Options
- selectionLimit?: number - Maximum number of items that can be selected (default: 1)
- mediaType?: 'photo' | 'video' | 'mixed' - Type of media to pick (default: 'photo') Returns
- Single selection: Promise<PhotoPickerResult>
- Multiple selection: Promise<PhotoPickerResult[]>
Where PhotoPickerResult has the following structure:
interface PhotoPickerResult {
  uri: string;
}

## Requirements
- React Native >= 0.60.0
- Android API level >= 19

## License
MIT
## Contributing
Feel free to submit a pull request or open an issue if you find any bugs or have suggestions for improvements.

After making these changes, run `npm run build` to ensure everything compiles correctly, then you can publish your package using `npm publish`.

Remember to:
1. Test the package thoroughly
2. Set up CI/CD if needed
3. Consider adding example project
4. Add proper error handling documentation
5. Consider adding contribution guidelines