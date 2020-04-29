import { WebPlugin } from '@capacitor/core';
import { GalleryPluginPlugin } from './definitions';

export class GalleryPluginWeb extends WebPlugin implements GalleryPluginPlugin {
  constructor() {
    super({
      name: 'GalleryPlugin',
      platforms: ['web','android']
    });
  }

  async callGallery(options: { value: Object }): Promise<{value: Object}> {
    console.log('ECHO', options);
    return options;
  }
}

const GalleryPlugin = new GalleryPluginWeb();

export { GalleryPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(GalleryPlugin);
