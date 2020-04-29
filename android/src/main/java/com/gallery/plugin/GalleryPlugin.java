package com.gallery.plugin;

import android.content.Intent;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONException;

@NativePlugin()
public class GalleryPlugin extends Plugin {
    static boolean isMultiple = false;
    private static PluginCall response = null;

    @PluginMethod()
    public void callGallery(PluginCall call) {
      response = call;
      isMultiple = call.getBoolean("isMultiple");
      Intent intent = new Intent(getContext(), GalleryMainActivity.class);
      getActivity().startActivity(intent);
    }

    public static void returnResponse(Object object){
      JSObject jsObject = new JSObject();
      try {
        jsObject.putSafe("data",object);
        response.success(jsObject);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
}
