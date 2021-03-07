package com.inlocomedia.reactnative.engage;

import android.location.Address;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.incognia.core.CheckIn;
import com.incognia.core.ConsentDialogOptions;
import com.incognia.core.ConsentResult;
import com.incognia.core.ConsentTypes;
import com.incognia.core.IncogniaOptions;
import com.incognia.core.listener.ConsentListener;
import com.incognia.core.listener.IncogniaListener;
import com.incognia.core.listener.Result;
import com.incognia.icg.Incognia;
import com.incognia.icg.IncogniaDemo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RNInLocoEngageModule extends ReactContextBaseJavaModule {

    private final static String REACT_CLASS = "RNInLocoEngage";

    private static String ADDRESS_LOCALE_KEY = "locale";
    private static String ADDRESS_COUNTRY_NAME_KEY = "countryName";
    private static String ADDRESS_COUNTRY_CODE_KEY = "countryCode";
    private static String ADDRESS_ADMIN_AREA_KEY = "adminArea";
    private static String ADDRESS_SUBADMIN_AREA_KEY = "subAdminArea";
    private static String ADDRESS_LOCALITY_KEY = "locality";
    private static String ADDRESS_SUB_LOCALITY_KEY = "subLocality";
    private static String ADDRESS_THOROUGHFARE_KEY = "thoroughfare";
    private static String ADDRESS_SUB_THOROUGHFARE_KEY = "subThoroughfare";
    private static String ADDRESS_POSTAL_CODE_KEY = "postalCode";
    private static String ADDRESS_LATITUDE_KEY = "latitude";
    private static String ADDRESS_LONGITUDE_KEY = "longitude";
    private static String ADDRESS_LINE = "address_line";

    private static String OPTIONS_APP_ID = "appId";
    private static String OPTIONS_LOGS_ENABLED = "logsEnabled";
    private static String OPTIONS_DEVELOPMENT_DEVICES = "developmentDevices";
    private static String OPTIONS_VISITS_ENABLED = "visitsEnabled";
    private static String OPTIONS_REQUIRES_USER_PRIVACY_CONSENT = "userPrivacyConsentRequired";
    private static String OPTIONS_BACKGROUND_WAKEUP_ENABLED = "backgroundWakeupEnabled";
    private static String OPTIONS_SCREEN_TRACKING_ENABLED = "screenTrackingEnabled";

    private static String CONSENT_DIALOG_TITLE = "consentDialogTitle";
    private static String CONSENT_DIALOG_MESSAGE = "consentDialogMessage";
    private static String CONSENT_DIALOG_ACCEPT_TEXT = "consentDialogAcceptText";
    private static String CONSENT_DIALOG_DENY_TEXT= "consentDialogDenyText";

    private final ReactApplicationContext reactContext;

    public RNInLocoEngageModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    public void initSdk() {
        Incognia.init(reactContext);
    }

    @ReactMethod
    public void initSdkWithOptions(final ReadableMap optionsMap) {
        IncogniaOptions.Builder options = new IncogniaOptions.Builder();
        options.appId(optionsMap.getString(OPTIONS_APP_ID));
        options.logEnabled(optionsMap.getBoolean(OPTIONS_LOGS_ENABLED));
        options.visitsEnabledByDefault(optionsMap.getBoolean(OPTIONS_VISITS_ENABLED));
        options.privacyConsentRequired(optionsMap.getBoolean(OPTIONS_REQUIRES_USER_PRIVACY_CONSENT));
        options.backgroundWakeupEnabled(optionsMap.getBoolean(OPTIONS_BACKGROUND_WAKEUP_ENABLED));
        options.screenTrackingEnabled(optionsMap.getBoolean(OPTIONS_SCREEN_TRACKING_ENABLED));
        options.developmentDevices(convertReadableArrayToSet(optionsMap.getArray(OPTIONS_DEVELOPMENT_DEVICES)));

        Incognia.init(reactContext, options.build());
    }

    @ReactMethod
    public void setUser(final String userId) {
        Incognia.setUserId(reactContext, userId);
    }

    @ReactMethod
    public void clearUser() {
        Incognia.clearUserId(reactContext);
    }

    @ReactMethod
    public void requestPrivacyConsent(final ReadableMap consentDialogOptionsMap, final ReadableArray consentTypesArray, final Promise promise) {
        ConsentDialogOptions.Builder consentDialogOptions = new ConsentDialogOptions.Builder(reactContext.getCurrentActivity());
        if (consentDialogOptionsMap.hasKey(CONSENT_DIALOG_TITLE)) {
            consentDialogOptions.title(consentDialogOptionsMap.getString(CONSENT_DIALOG_TITLE));
        }
        if (consentDialogOptionsMap.hasKey(CONSENT_DIALOG_MESSAGE)) {
            consentDialogOptions.message(consentDialogOptionsMap.getString(CONSENT_DIALOG_MESSAGE));
        }
        if (consentDialogOptionsMap.hasKey(CONSENT_DIALOG_ACCEPT_TEXT)) {
            consentDialogOptions.acceptText(consentDialogOptionsMap.getString(CONSENT_DIALOG_ACCEPT_TEXT));
        }
        if (consentDialogOptionsMap.hasKey(CONSENT_DIALOG_DENY_TEXT)) {
            consentDialogOptions.denyText(consentDialogOptionsMap.getString(CONSENT_DIALOG_DENY_TEXT));
        }
        consentDialogOptions.consentTypes(convertReadableArrayToSet(consentTypesArray));

        Incognia.requestPrivacyConsent(consentDialogOptions.build(), new ConsentListener() {
            @Override
            public void onConsentResult(final ConsentResult consentResult) {
                boolean hasFinished = consentResult.hasFinished();
                boolean isWaitingConsent = consentResult.isWaitingConsent();
                boolean areAllConsentTypesGiven = consentResult.areAllConsentTypesGiven();
                WritableMap result = Arguments.createMap();
                result.putBoolean("hasFinished", hasFinished);
                result.putBoolean("isWaitingConsent", isWaitingConsent);
                result.putBoolean("areAllConsentTypesGiven", areAllConsentTypesGiven);
                if(promise != null) {
                    promise.resolve(result);
                }
            }
        });
    }

    @ReactMethod
    @Deprecated
    public void giveUserPrivacyConsent(final boolean consentGiven) {
        Incognia.allowConsentTypes(reactContext, ConsentTypes.ALL, false);
    }

    @ReactMethod
    @Deprecated
    public void giveUserPrivacyConsentForTypes(final ReadableArray consentTypesArray) {
        Set<String> consentTypes = convertReadableArrayToSet(consentTypesArray);
        Incognia.allowConsentTypes(reactContext, consentTypes, false);
    }

    @ReactMethod
    public void setAllowedConsentTypes(final ReadableArray consentTypesArray) {
        Set<String> consentTypes = convertReadableArrayToSet(consentTypesArray);
        Incognia.allowConsentTypes(reactContext, consentTypes, true);
    }

    @ReactMethod
    public void allowConsentTypes(final ReadableArray consentTypesArray) {
        Set<String> consentTypes = convertReadableArrayToSet(consentTypesArray);
        Incognia.allowConsentTypes(reactContext, consentTypes, false);
    }

    @ReactMethod
    public void denyConsentTypes(final ReadableArray consentTypesArray) {
        Set<String> consentTypes = convertReadableArrayToSet(consentTypesArray);
        Incognia.denyConsentTypes(reactContext, consentTypes);
    }

    @ReactMethod
    @Deprecated
    public void checkPrivacyConsentMissing(final Promise promise) {
//        Incognia.checkConsent(reactContext, new IncogniaListener<Boolean>() {
//            @Override
//            public void onResult(Result<Boolean> result) {
//                if (result.isSuccessful()) {
//                    boolean privacyConsentMissing = result.getResult();
//                    if(promise != null) {
//                        promise.resolve(privacyConsentMissing);
//                    }
//                } else {
//                    promise.reject(new Exception("Error while checking if privacy consent is missing."));
//                }
//            }
//        });
        if(promise != null) {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void checkConsent(final ReadableArray consentTypesArray, final Promise promise) {
        Set<String> consentTypes = convertReadableArrayToSet(consentTypesArray);
        Incognia.checkConsent(reactContext, new ConsentListener() {
            @Override
            public void onConsentResult(final ConsentResult consentResult) {
                boolean hasFinished = consentResult.hasFinished();
                boolean isWaitingConsent = consentResult.isWaitingConsent();
                boolean areAllConsentTypesGiven = consentResult.areAllConsentTypesGiven();
                WritableMap result = Arguments.createMap();
                result.putBoolean("hasFinished", hasFinished);
                result.putBoolean("isWaitingConsent", isWaitingConsent);
                result.putBoolean("areAllConsentTypesGiven", areAllConsentTypesGiven);
                if(promise != null) {
                    promise.resolve(result);
                }
            }
        }, consentTypes);
    }

    @ReactMethod
    public void setPushProvider(final String name, final String token) {
//        PushProvider pushProvider = new PushProvider.Builder()
//                .setName(name)
//                .setToken(token)
//                .build();
//
//        IncogniaPush.setPushProvider(reactContext, pushProvider);
    }

    @ReactMethod
    public void setPushNotificationsEnabled(final boolean enabled) {
//        IncogniaPush.setEnabled(reactContext, enabled);
    }

    @ReactMethod
    public void presentNotification(ReadableMap messageMap, String channelId, int notificationId) {
//        int smallIconResId = reactContext.getResources().getIdentifier("ic_notification", "mipmap", reactContext.getPackageName());
//        if (smallIconResId == 0) {
//            smallIconResId = reactContext.getResources().getIdentifier("ic_launcher", "mipmap", reactContext.getPackageName());
//            if (smallIconResId == 0) {
//                smallIconResId = android.R.drawable.ic_dialog_info;
//            }
//        }
//
//        final PushMessage pushContent = IncogniaPush.decodeReceivedMessage(reactContext, convertToStringStringMap(messageMap.toHashMap()));
//
//        if (pushContent != null) {
//            IncogniaPush.presentNotification(
//                    reactContext,
//                    pushContent,
//                    smallIconResId,
//                    notificationId,
//                    channelId
//            );
//        }
    }

    @ReactMethod
    public void trackEvent(final String eventName, ReadableMap properties) {
        Map<String, String> propertiesMap = convertToStringStringMap(properties.toHashMap());
        Incognia.trackEvent(reactContext, eventName, propertiesMap);
    }

    @ReactMethod
    public void trackLocalizedEvent(final String eventName, ReadableMap properties) {
        Map<String, String> propertiesMap = convertToStringStringMap(properties.toHashMap());
        Incognia.trackLocalizedEvent(reactContext, eventName, propertiesMap);
    }

    @ReactMethod
    public void registerCheckIn(final String placeName, final String placeId, ReadableMap properties) {
        Map<String, String> propertiesMap = convertToStringStringMap(properties.toHashMap());
        CheckIn checkIn = new CheckIn.Builder()
                .placeName(placeName)
                .placeId(placeId)
                .extras(propertiesMap)
                .build();

        Incognia.registerCheckIn(reactContext, checkIn);
    }

    @ReactMethod
    public void setUserAddress(ReadableMap addressMap) {
        Address address = convertMapToAddress(addressMap);
        Incognia.setAddress(reactContext, address);
    }

    @ReactMethod
    public void clearUserAddress() {
        Incognia.clearAddress(reactContext);
    }

    @ReactMethod
    public void getInstallationId(final Promise promise) {
        Incognia.getInstallationId(reactContext, new IncogniaListener<String>() {
            @Override
            public void onResult(Result<String> result) {
                if (result.isSuccessful()) {
                    String installationId = result.getResult();
                    if (promise != null) {
                        promise.resolve(installationId);
                    }
                } else {
                    promise.reject(new Exception("Error while getting installation id."));
                }
            }
        });
    }

    @ReactMethod
    public void trackSignUp(String signUpId, ReadableMap addressMap) {
        Address address = convertMapToAddress(addressMap);
        IncogniaDemo.trackSignUp(reactContext, signUpId, address);
    }

    @ReactMethod
    public void trackLogin(String accountId) {
        IncogniaDemo.trackLogin(reactContext, accountId);
    }

    private static Address convertMapToAddress(ReadableMap map) {
        if (map != null) {
            Locale locale = localeFromString(map.getString(ADDRESS_LOCALE_KEY));
            Address address = new Address(locale != null ? locale : new Locale("en", "US"));

            if (map.hasKey(ADDRESS_COUNTRY_NAME_KEY)) {
                address.setCountryName(map.getString(ADDRESS_COUNTRY_NAME_KEY));
            }
            if (map.hasKey(ADDRESS_COUNTRY_CODE_KEY)) {
                address.setCountryCode(map.getString(ADDRESS_COUNTRY_CODE_KEY));
            }
            if (map.hasKey(ADDRESS_ADMIN_AREA_KEY)) {
                address.setAdminArea(map.getString(ADDRESS_ADMIN_AREA_KEY));
            }
            if (map.hasKey(ADDRESS_SUBADMIN_AREA_KEY)) {
                address.setSubAdminArea(map.getString(ADDRESS_SUBADMIN_AREA_KEY));
            }
            if (map.hasKey(ADDRESS_LOCALITY_KEY)) {
                address.setLocality(map.getString(ADDRESS_LOCALITY_KEY));
            }
            if (map.hasKey(ADDRESS_SUB_LOCALITY_KEY)) {
                address.setSubLocality(map.getString(ADDRESS_SUB_LOCALITY_KEY));
            }
            if (map.hasKey(ADDRESS_THOROUGHFARE_KEY)) {
                address.setThoroughfare(map.getString(ADDRESS_THOROUGHFARE_KEY));
            }
            if (map.hasKey(ADDRESS_SUB_THOROUGHFARE_KEY)) {
                address.setSubThoroughfare(map.getString(ADDRESS_SUB_THOROUGHFARE_KEY));
            }
            if (map.hasKey(ADDRESS_POSTAL_CODE_KEY)) {
                address.setPostalCode(map.getString(ADDRESS_POSTAL_CODE_KEY));
            }
            if (map.hasKey(ADDRESS_LINE)) {
                address.setAddressLine(0, map.getString(ADDRESS_LINE));
            }
            if (map.hasKey(ADDRESS_LATITUDE_KEY)) {
                address.setLatitude(map.getDouble(ADDRESS_LATITUDE_KEY));
            }
            if (map.hasKey(ADDRESS_LONGITUDE_KEY)) {
                address.setLongitude(map.getDouble(ADDRESS_LONGITUDE_KEY));
            }

            return address;
        }
        return null;
    }

    private static Map<String, String> convertToStringStringMap(Map<String, Object> map) {
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                newMap.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return newMap;
    }

    private static Set<String> convertReadableArrayToSet(ReadableArray array) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < array.size(); i++) {
            set.add(array.getString(i));
        }
        return set;
    }

    private static Locale localeFromString(String locale) {
        try {
            String[] parts = locale.split("-", -1);
            if (parts.length == 1) return new Locale(parts[0]);
            else if (parts.length == 2
                     || (parts.length == 3 && parts[2].startsWith("#")))
                return new Locale(parts[0], parts[1]);
            else return new Locale(parts[0], parts[1], parts[2]);
        } catch (Throwable t) {
            return null;
        }
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }
}