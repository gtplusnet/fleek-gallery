declare module "@capacitor/core" {
    interface PluginRegistry {
        GalleryPlugin: GalleryPluginPlugin;
    }
}
export interface GalleryPluginPlugin {
    callGallery(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
