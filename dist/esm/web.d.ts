import { WebPlugin } from '@capacitor/core';
import { GalleryPluginPlugin } from './definitions';
export declare class GalleryPluginWeb extends WebPlugin implements GalleryPluginPlugin {
    constructor();
    callGallery(options: {
        value: Object;
    }): Promise<{
        value: Object;
    }>;
}
declare const GalleryPlugin: GalleryPluginWeb;
export { GalleryPlugin };
