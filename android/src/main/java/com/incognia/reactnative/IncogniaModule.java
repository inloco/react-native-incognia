package com.incognia.reactnative;

import android.app.Application;
import android.location.Address;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.incognia.ConsentDialogOptions;
import com.incognia.ConsentListener;
import com.incognia.ConsentResult;
import com.incognia.ConsentTypes;
import com.incognia.EventProperties;
import com.incognia.Incognia;
import com.incognia.IncogniaListener;
import com.incognia.IncogniaOptions;
import com.incognia.IncogniaTrial;
import com.incognia.Result;
import com.incognia.TransactionAddress;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused",
                   "Convert2Lambda"})
@ReactModule(name = IncogniaModule.NAME)
public class IncogniaModule extends ReactContextBaseJavaModule {
  public static final String NAME = "IncogniaModule";

  private static final String CONSENT_DIALOG_TITLE = "dialogTitle";
  private static final String CONSENT_DIALOG_MESSAGE = "dialogMessage";
  private static final String CONSENT_DIALOG_ACCEPT_TEXT = "dialogAcceptText";
  private static final String CONSENT_DIALOG_DENY_TEXT = "dialogDenyText";
  private static final String CONSENT_DIALOG_TYPES = "consentTypes";

  private static final String EVENT_NAME = "eventName";
  private static final String EVENT_PROPERTIES = "eventProperties";

  private static final String ADDRESS_LOCALE_KEY = "locale";
  private static final String ADDRESS_COUNTRY_NAME_KEY = "countryName";
  private static final String ADDRESS_COUNTRY_CODE_KEY = "countryCode";
  private static final String ADDRESS_ADMIN_AREA_KEY = "adminArea";
  private static final String ADDRESS_SUB_ADMIN_AREA_KEY = "subAdminArea";
  private static final String ADDRESS_LOCALITY_KEY = "locality";
  private static final String ADDRESS_SUB_LOCALITY_KEY = "subLocality";
  private static final String ADDRESS_THOROUGHFARE_KEY = "thoroughfare";
  private static final String ADDRESS_SUB_THOROUGHFARE_KEY = "subThoroughfare";
  private static final String ADDRESS_POSTAL_CODE_KEY = "postalCode";
  private static final String ADDRESS_LATITUDE_KEY = "latitude";
  private static final String ADDRESS_LONGITUDE_KEY = "longitude";
  private static final String ADDRESS_LINE = "addressLine";

  private static final String SIGNUP_ID = "signupId";
  private static final String SIGNUP_ADDRESS = "signupAddress";
  private static final String SIGNUP_PROPERTIES = "properties";

  private static final String LOGIN_ID = "loginId";
  private static final String LOGIN_ACCOUNT_ID = "accountId";
  private static final String LOGIN_PROPERTIES = "properties";

  private static final String TRANSACTION_ID = "transactionId";
  private static final String TRANSACTION_ADDRESSES = "transactionAddresses";
  private static final String TRANSACTION_PROPERTIES = "properties";
  private static final String TRANSACTION_ADDRESS_TYPE = "type";

  private static final String OPTIONS_APP_ID_KEY = "appId";
  private static final String OPTIONS_LOG_ENABLED_KEY = "logEnabled";
  private static final String OPTIONS_LOCATION_ENABLED_KEY = "locationEnabled";
  private static final String OPTIONS_INSTALLED_APPS_COLLECTION_ENABLED_KEY = "installedAppsCollectionEnabled";
  private static final String OPTIONS_BACKGROUND_WAKEUP_ENABLED_KEY = "backgroundWakeupEnabled";

  public IncogniaModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void initSdk() {
    try {
      Application application = (Application) getReactApplicationContext().getApplicationContext();
      Incognia.init(application);
    } catch (Exception e) {
      Log.e(NAME, "Error initializing Incognia SDK: " + e.getMessage(), e);
    }
  }

  @ReactMethod
  public void initSdkWithOptions(final ReadableMap optionsParameters) {
    try {
      Application application = (Application) getReactApplicationContext().getApplicationContext();

      String appId = optionsParameters.hasKey(OPTIONS_APP_ID_KEY) ? optionsParameters.getString(OPTIONS_APP_ID_KEY) : null;
      boolean logEnabled = optionsParameters.hasKey(OPTIONS_LOG_ENABLED_KEY) && optionsParameters.getBoolean(OPTIONS_LOG_ENABLED_KEY);
      boolean locationEnabled = !optionsParameters.hasKey(OPTIONS_LOCATION_ENABLED_KEY) || optionsParameters.getBoolean(OPTIONS_LOCATION_ENABLED_KEY);
      boolean installedAppsCollectionEnabled = optionsParameters.hasKey(OPTIONS_INSTALLED_APPS_COLLECTION_ENABLED_KEY) &&
                                               optionsParameters.getBoolean(OPTIONS_INSTALLED_APPS_COLLECTION_ENABLED_KEY);
      boolean backgroundWakeupEnabled = optionsParameters.hasKey(OPTIONS_BACKGROUND_WAKEUP_ENABLED_KEY) && optionsParameters.getBoolean(
        OPTIONS_BACKGROUND_WAKEUP_ENABLED_KEY);

      IncogniaOptions options = new IncogniaOptions.Builder()
        .appId(appId)
        .logEnabled(logEnabled)
        .visitsEnabledByDefault(locationEnabled)
        .installedAppsCollectionEnabled(installedAppsCollectionEnabled)
        .backgroundWakeupEnabled(backgroundWakeupEnabled)
        .build();

      Incognia.init(application, options);
    } catch (Exception e) {
      Log.e(NAME, "Error initializing Incognia SDK with options: " + e.getMessage(), e);
    }
  }

  @ReactMethod
  public void setAccountId(final String accountId) {
    Incognia.setAccountId(accountId);
  }

  @ReactMethod
  public void clearAccountId() {
    Incognia.clearAccountId();
  }

  @ReactMethod
  public void allowConsentTypes(final ReadableArray consentTypesArray) {
    Set<String> consentTypes = convertStringReadableArrayToSet(consentTypesArray);
    Incognia.allowConsentTypes(consentTypes);
  }

  @ReactMethod
  public void denyConsentTypes(final ReadableArray consentTypesArray) {
    Set<String> consentTypes = convertStringReadableArrayToSet(consentTypesArray);
    Incognia.denyConsentTypes(consentTypes);
  }

  @ReactMethod
  public void setLocationEnabled(final boolean enabled) {
    Incognia.setLocationEnabled(enabled);
  }

  @ReactMethod
  public void fetchInstallationId(final Promise promise) {
    Incognia.fetchInstallationId(new IncogniaListener<String>() {
      @Override
      public void onResult(Result<String> result) {
        if (result.isSuccessful()) {
          String installationId = result.getResult();
          promise.resolve(installationId);
        } else {
          promise.reject(new Exception("Error while getting installation id."));
        }
      }
    });
  }

  @ReactMethod
  public void generateRequestToken(final Promise promise) {
    Incognia.generateRequestToken(new IncogniaListener<String>() {
      @Override
      public void onResult(Result<String> result) {
        if (result.isSuccessful()) {
          String requestToken = result.getResult();
          promise.resolve(requestToken);
        } else {
          promise.reject(new Exception("Error while generating a request token."));
        }
      }
    });
  }

  @ReactMethod
  public void generateUniqueRequestToken(final Promise promise) {
    Incognia.generateUniqueRequestToken(new IncogniaListener<String>() {
      @Override
      public void onResult(Result<String> result) {
        if (result.isSuccessful()) {
          String requestToken = result.getResult();
          promise.resolve(requestToken);
        } else {
          promise.reject(new Exception("Error while generating a unique request token."));
        }
      }
    });
  }

  @ReactMethod
  public void requestPrivacyConsent(final ReadableMap consentDialogOptionsMap, final Promise promise) {
    ConsentDialogOptions.Builder consentDialogOptions = new ConsentDialogOptions.Builder(getCurrentActivity());

    String dialogTitle = consentDialogOptionsMap.hasKey(CONSENT_DIALOG_TITLE) ? consentDialogOptionsMap.getString(CONSENT_DIALOG_TITLE) : null;
    if (dialogTitle != null) {
      consentDialogOptions.title(dialogTitle);
    }

    String dialogMessage = consentDialogOptionsMap.hasKey(CONSENT_DIALOG_MESSAGE) ? consentDialogOptionsMap.getString(CONSENT_DIALOG_MESSAGE) : null;
    if (dialogMessage != null) {
      consentDialogOptions.message(dialogMessage);
    }

    String dialogAccept = consentDialogOptionsMap.hasKey(CONSENT_DIALOG_ACCEPT_TEXT) ? consentDialogOptionsMap.getString(CONSENT_DIALOG_ACCEPT_TEXT) : null;
    if (dialogAccept != null) {
      consentDialogOptions.acceptText(dialogAccept);
    }

    String dialogDeny = consentDialogOptionsMap.hasKey(CONSENT_DIALOG_DENY_TEXT) ? consentDialogOptionsMap.getString(CONSENT_DIALOG_DENY_TEXT) : null;
    if (dialogDeny != null) {
      consentDialogOptions.denyText(dialogDeny);
    }

    ReadableArray consentTypesArray = consentDialogOptionsMap.hasKey(CONSENT_DIALOG_TYPES) ? consentDialogOptionsMap.getArray(CONSENT_DIALOG_TYPES) : null;
    if (consentTypesArray != null) {
      consentDialogOptions.consentTypes(convertStringReadableArrayToSet(consentTypesArray));
    }

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
        promise.resolve(result);
      }
    });
  }

  @ReactMethod
  public void checkConsent(final ReadableArray consentTypesArray, final Promise promise) {
    Set<String> consentTypes = convertStringReadableArrayToSet(consentTypesArray);
    Incognia.checkConsent(new ConsentListener() {
      @Override
      public void onConsentResult(final ConsentResult consentResult) {
        boolean hasFinished = consentResult.hasFinished();
        boolean isWaitingConsent = consentResult.isWaitingConsent();
        boolean areAllConsentTypesGiven = consentResult.areAllConsentTypesGiven();
        WritableMap result = Arguments.createMap();
        result.putBoolean("hasFinished", hasFinished);
        result.putBoolean("isWaitingConsent", isWaitingConsent);
        result.putBoolean("areAllConsentTypesGiven", areAllConsentTypesGiven);
        promise.resolve(result);
      }
    }, consentTypes);
  }

  @ReactMethod
  public void trackEvent(final ReadableMap parameters) {
    String eventName = parameters.hasKey(EVENT_NAME) ? parameters.getString(EVENT_NAME) : null;
    EventProperties eventProperties = parameters.hasKey(EVENT_PROPERTIES) ? convertToEventProperties(parameters.getMap(EVENT_PROPERTIES)) : null;
    Incognia.trackEvent(eventName, eventProperties);
  }

  @ReactMethod
  public void trackLocalizedEvent(final ReadableMap parameters) {
    String eventName = parameters.hasKey(EVENT_NAME) ? parameters.getString(EVENT_NAME) : null;
    EventProperties eventProperties = parameters.hasKey(EVENT_PROPERTIES) ? convertToEventProperties(parameters.getMap(EVENT_PROPERTIES)) : null;
    Incognia.trackLocalizedEvent(eventName, eventProperties);
  }

  @ReactMethod
  public void trackSignupSent(ReadableMap parameters) {
    String signUpId = parameters.hasKey(SIGNUP_ID) ? parameters.getString(SIGNUP_ID) : null;
    Address address = parameters.hasKey(SIGNUP_ADDRESS) ? convertToAddress(parameters.getMap(SIGNUP_ADDRESS)) : null;
    EventProperties eventProperties = parameters.hasKey(SIGNUP_PROPERTIES) ? convertToEventProperties(parameters.getMap(SIGNUP_PROPERTIES)) : null;

    IncogniaTrial.trackSignupSent(signUpId, eventProperties, address);
  }

  @SuppressWarnings("ConstantConditions")
  @ReactMethod
  public void trackLoginSucceeded(ReadableMap parameters) {
    String accountId = parameters.hasKey(LOGIN_ACCOUNT_ID) ? parameters.getString(LOGIN_ACCOUNT_ID) : null;
    String loginId = parameters.hasKey(LOGIN_ID) ? parameters.getString(LOGIN_ID) : null;
    EventProperties eventProperties = parameters.hasKey(LOGIN_PROPERTIES) ? convertToEventProperties(parameters.getMap(LOGIN_PROPERTIES)) : null;

    IncogniaTrial.trackLoginSucceeded(accountId, loginId, eventProperties);
  }

  @ReactMethod
  public void trackPaymentSent(ReadableMap parameters) {
    String transactionId = parameters.hasKey(TRANSACTION_ID) ? parameters.getString(TRANSACTION_ID) : null;
    Set<TransactionAddress> transactionAddresses = parameters.hasKey(TRANSACTION_ADDRESSES) ? convertTransactionAddressReadableArrayToSet(parameters.getArray(TRANSACTION_ADDRESSES)) : null;
    EventProperties eventProperties = parameters.hasKey(TRANSACTION_PROPERTIES) ? convertToEventProperties(parameters.getMap(TRANSACTION_PROPERTIES)) : null;

    IncogniaTrial.trackPaymentSent(transactionId, eventProperties, transactionAddresses);
  }

  @ReactMethod
  public void notifyAppInForeground() {
    Incognia.notifyAppInForeground();
  }

  @ReactMethod
  public void refreshLocation() {
    Incognia.refreshLocation();
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("CONSENT_TYPE_ADDRESS_VALIDATION", ConsentTypes.ADDRESS_VALIDATION);
    constants.put("CONSENT_TYPE_ENGAGE", ConsentTypes.PUSH);
    constants.put("CONSENT_TYPE_EVENTS", ConsentTypes.EVENTS);
    constants.put("CONSENT_TYPE_LOCATION", ConsentTypes.LOCATION);
    constants.put("CONSENT_TYPE_CONTEXT_PROVIDER", ConsentTypes.CONTEXT_PROVIDER);
    constants.put("CONSENT_TYPES_ALL", ConsentTypes.ALL.toArray());
    constants.put("CONSENT_TYPES_NONE", new String[0]);

    return constants;
  }

  private static EventProperties convertToEventProperties(ReadableMap readableMap) {
    if (readableMap == null) {
      return null;
    }

    Map<String, Object> map = readableMap.toHashMap();
    EventProperties eventProperties = EventProperties.newProperties();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() instanceof String) {
        eventProperties.put(entry.getKey(), (String) entry.getValue());
      } else if (entry.getValue() instanceof Integer) {
        eventProperties.put(entry.getKey(), (Integer) entry.getValue());
      } else if (entry.getValue() instanceof Float) {
        eventProperties.put(entry.getKey(), (Float) entry.getValue());
      } else if (entry.getValue() instanceof Double) {
        eventProperties.put(entry.getKey(), (Double) entry.getValue());
      } else if (entry.getValue() instanceof Boolean) {
        eventProperties.put(entry.getKey(), (Boolean) entry.getValue());
      }
    }
    return eventProperties;
  }

  private static Set<String> convertStringReadableArrayToSet(ReadableArray array) {
    if (array == null) {
      return null;
    }
    Set<String> set = new HashSet<>();
    for (int i = 0; i < array.size(); i++) {
      set.add(array.getString(i));
    }
    return set;
  }

  private static Address convertToAddress(ReadableMap map) {
    if (map == null) {
      return null;
    }

    Locale locale = map.hasKey(ADDRESS_LOCALE_KEY) ? localeFromString(map.getString(ADDRESS_LOCALE_KEY)) : null;
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
    if (map.hasKey(ADDRESS_SUB_ADMIN_AREA_KEY)) {
      address.setSubAdminArea(map.getString(ADDRESS_SUB_ADMIN_AREA_KEY));
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

  private static Set<TransactionAddress> convertTransactionAddressReadableArrayToSet(final ReadableArray array) {
    if (array == null) {
      return null;
    }

    Set<TransactionAddress> set = new HashSet<>();
    for (int i = 0; i < array.size(); i++) {
      TransactionAddress transactionAddress = convertMapToTransactionAddress(array.getMap(i));
      if (transactionAddress != null) {
        set.add(transactionAddress);
      }
    }
    return set;
  }

  @SuppressWarnings("ConstantConditions")
  private static TransactionAddress convertMapToTransactionAddress(ReadableMap map) {
    if (map == null) {
      return null;
    }

    Address address = convertToAddress(map);
    String addressType = map.hasKey(TRANSACTION_ADDRESS_TYPE) ? map.getString(TRANSACTION_ADDRESS_TYPE) : null;
    return new TransactionAddress(addressType, address);
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
}
