package com.cheng.corodva.amap.nativelocation;


import android.content.Context;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AMapNativeLocation extends CordovaPlugin{

    CallbackContext callbackContext = null;

    //����AMapLocationClient�����
    public AMapLocationClient mLocationClient = null;
    //������λ�ص�������
    public AMapLocationListener mLocationListener = new AMapLocationListener(){
        @Override
        public void onLocationChanged(AMapLocation aMapLocation){
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    int locationType = aMapLocation.getLocationType();//��ȡ��ǰ��λ�����Դ ��λ���Ͷ��ձ�: http://lbs.amap.com/api/android-location-sdk/guide/utilities/location-type/
                    Double latitude = aMapLocation.getLatitude();//��ȡγ��
                    Double longitude = aMapLocation.getLongitude();//��ȡ����
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("locationType", locationType);
                        jo.put("latitude", latitude);
                        jo.put("longitude", longitude);
                    } catch (JSONException e) {
                        jo = null;
                        e.printStackTrace();
                    }
                    Log.w("��λ�ɹ�:", jo.toString());
                    callbackContext.success(jo);
                } else {
                    // ��������ձ� http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/
                    Log.e("��λʧ�ܴ�����:", aMapLocation.getAdCode());
                    Log.e("��λʧ����Ϣ:", aMapLocation.getErrorInfo());
                    callbackContext.error(aMapLocation.getErrorCode());
                }
            }
        }
    };

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Context context = this.cordova.getActivity().getApplicationContext();
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(mLocationListener);
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);// ʹ��ǩ����λ����
        mLocationClient.setLocationOption(mLocationOption); // ���ö�λ����
    }


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException{
        this.callbackContext = callbackContext;
        if (action.equals("coolMethod")) {
            // ���ó���ģʽ����õ���һ��stop���ٵ���start�Ա�֤����ģʽ��Ч
            mLocationClient.stopLocation();
            mLocationClient.startLocation(); // ������λ
            PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
            r.setKeepCallback(true);
            callbackContext.sendPluginResult(r);
            return true;
        }

        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext){
        if(message != null && message.length() > 0){
            callbackContext.success(message);
        } else{
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

}
