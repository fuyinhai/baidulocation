package com.yuanbaopu.databox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类

public class BaiduLocationPlugin extends CordovaPlugin {
    private static final String TAG = "baidulocation";
    
    public BaiduLocationPlugin() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * Entry-point for JS calls from Cordova
     */
    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        try {
            if ("getLocation".equals(action)) {
                //String wwwRoot = inputs.getString(0);
                //String cordovaRoot = inputs.getString(1);

                //String result = startServer(wwwRoot, cordovaRoot, callbackContext);

                callbackContext.success("Hello");

                return true;
            } else {
                Log.w(TAG, "Invalid action passed: " + action);
                PluginResult result = new PluginResult(Status.INVALID_ACTION);
                callbackContext.sendPluginResult(result);
            }
        } catch (Exception e) {
            Log.w(TAG, "Caught exception during execution: " + e);
            String message = e.toString();
            callbackContext.error(message);
        }

        return true;
    }
}
