package com.appspot.wecookbob.lib;

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil
{
   private static PreferenceUtil _instance = null;
 
   private static final String PROPERTY_REG_ID = "registration_id";
   private static final String PROPERTY_APP_VERSION = "appVersion";
   private static final String PROPERTY_USER_ID = "user_id";
 
   public static synchronized PreferenceUtil instance(Context $context)
   {
      if (_instance == null)
         _instance = new PreferenceUtil($context);
      return _instance;
   }
 
   protected PreferenceUtil(Context $context)
   {
      super($context);
   }
 
   public void putRegId(String $regId)
   {
      put(PROPERTY_REG_ID, $regId);
   }
 
   public String regId()
   {
      return get(PROPERTY_REG_ID);
   }
   
   public void putUserId(String $userId)
   {
      put(PROPERTY_USER_ID, $userId);
   }
 
   public String userId()
   {
      return get(PROPERTY_USER_ID);
   }
 
   public void putAppVersion(int $appVersion)
   {
      put(PROPERTY_APP_VERSION, $appVersion);
   }
 
   public int appVersion()
   {
      return get(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
   }
}