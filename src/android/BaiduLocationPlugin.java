package com.yuanbaopu.databox;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * REF: http://developer.baidu.com/map/index.php?title=android-locsdk/guide/v5-0
 */
public class BaiduLocationPlugin extends CordovaPlugin  implements BDLocationListener {
    private static final String TAG = "baidulocation";
    private LocationClient mLocationClient = null;
    JSONObject addrInfo = new JSONObject();

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Battery_Saving);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000*60; // locate address per minute
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(false);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        mLocationClient = new LocationClient(cordova.getActivity().getApplicationContext());
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();
    }

    public void onDestroy() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        try {
            addrInfo.put("time"     , location.getTime());
            addrInfo.put("errCode"  , location.getLocType());
            addrInfo.put("latitude" , location.getLatitude());
            addrInfo.put("lontitude", location.getLongitude());
            addrInfo.put("radius"   , location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                addrInfo.put("speed", location.getSpeed());
                addrInfo.put("satellite",location.getSatelliteNumber());
                addrInfo.put("height", location.getAltitude());// 单位：米
                addrInfo.put("direction", location.getDirection());// 单位度
                addrInfo.put("addr", location.getAddrStr());
                addrInfo.put("describe", "gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                addrInfo.put("addr", location.getAddrStr());
                addrInfo.put("operationers", location.getOperators());
                addrInfo.put("describe", "网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                addrInfo.put("describe", "离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                addrInfo.put("describe", "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                addrInfo.put("describe", "网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                addrInfo.put("describe", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
        } catch (JSONException e) {
            Log.w(TAG,e);
        }
    }

    /**
     * Entry-point for JS calls from Cordova
     */
    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        try {
            if ("getLocation".equals(action)) {
                callbackContext.success(addrInfo);
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
