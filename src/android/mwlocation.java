package com.mei1.cordova.location;

import android.location.Location;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PermissionHelper;

import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.utils.app.JSONObjectUtils;
import dev.utils.app.LocationUtils;
import dev.utils.app.permission.PermissionUtils;

/**
 * This class echoes a string called from JavaScript.
 */
public class mwlocation extends CordovaPlugin {

    String TAG = "mwlocationPlugin";
    String [] permissions = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION };
    CallbackContext context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = callbackContext;
        if (action.equals("getCurrentPosition")) {
            String message = args.getString(0);
            this.getCurrentPosition(message, callbackContext);
            return true;
        }
        return false;
    }

    private void getCurrentPosition(String message,  CallbackContext callbackContext) {
        Log.d(TAG, "getCurrentPosition");
        PermissionUtils permissionUtils = PermissionUtils.permission(permissions);
        permissionUtils.callBack(new PermissionUtils.PermissionCallBack() {
            @Override
            public void onGranted() {
                getLocation(callbackContext);
            }
            @Override
            public void onDenied(List<String> grantedList, List<String> deniedList, List<String> notFoundList) {
                callbackContext.error("1001");
            }
        });
        permissionUtils.request(cordova.getActivity());
        PermissionHelper.requestPermissions(this, 0, permissions);
    }

    public void getLocation(CallbackContext callbackContext) {
        LocationUtils.register(0, 100,
                new LocationUtils.OnLocationChangeListener() {
                    @Override
                    public void getLastKnownLocation(Location location) {
                    }
                    @Override
                    public void onLocationChanged(Location location) {
                        LocationUtils.unregister();
                        String json = JSONObjectUtils.toJson(location);
                        Map map = new HashMap();
                        map.put("latitude", location.getLatitude());
                        map.put("longitude", location.getLongitude());
                        map.put("speed", location.getSpeed());
                        map.put("time", location.getTime());
                        map.put("accuracy", location.getAccuracy());
                        PluginResult result = new PluginResult(PluginResult.Status.OK, new JSONObject(map));
                        callbackContext.sendPluginResult(result);
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                });
    }

    public boolean hasPermisssion() {
        for(String p : permissions)
        {
            if(!PermissionHelper.hasPermission(this, p))
            {
                return false;
            }
        }
        return true;
    }
}
