interface PhotoPickerOptions {
    selectionLimit?: number;
    mediaType?: 'photo' | 'video' | 'mixed';
}
interface PhotoPickerResult {
    uri: string;
}
export default class GalleryPicker {
    static pickMedia(options?: PhotoPickerOptions): Promise<PhotoPickerResult | PhotoPickerResult[]>;
}
export type { PhotoPickerOptions, PhotoPickerResult };
