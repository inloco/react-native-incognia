package com.incognia.reactnative;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.incognia.CardInfo;
import com.incognia.CustomEvent;
import com.incognia.EventAddress;
import com.incognia.EventLocation;
import com.incognia.EventProperties;
import com.incognia.Incognia;
import com.incognia.IncogniaOptions;
import com.incognia.LoginEvent;
import com.incognia.OnboardingEvent;
import com.incognia.PaymentAddress;
import com.incognia.PaymentCoupon;
import com.incognia.PaymentEvent;
import com.incognia.PaymentMethod;
import com.incognia.PaymentValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings({"unused",
                   "Convert2Lambda"})
@ReactModule(name = IncogniaModule.NAME)
public class IncogniaModule extends ReactContextBaseJavaModule {
  public static final String NAME = "IncogniaModule";

  private ReactApplicationContext reactContext;

  private static final String OPTIONS_APP_ID_KEY = "appId";
  private static final String OPTIONS_LOG_ENABLED_KEY = "logEnabled";
  private static final String OPTIONS_LOCATION_ENABLED_KEY = "locationEnabled";
  private static final String OPTIONS_INSTALLED_APPS_COLLECTION_ENABLED_KEY = "installedAppsCollectionEnabled";

  private static final String EVENT_ACCOUNT_ID = "accountId";
  private static final String EVENT_EXTERNAL_ID = "externalId";
  private static final String EVENT_ADDRESS = "address";
  private static final String EVENT_LOCATION = "location";
  private static final String EVENT_TAG = "tag";
  private static final String EVENT_PROPERTIES = "properties";
  private static final String EVENT_STATUS = "status";
  private static final String EVENT_STORE_ID = "storeId";

  private static final String ADDRESS_LOCALE_KEY = "locale";
  private static final String ADDRESS_COUNTRY_CODE_KEY = "countryCode";
  private static final String ADDRESS_COUNTRY_NAME_KEY = "countryName";
  private static final String ADDRESS_STATE_KEY = "state";
  private static final String ADDRESS_CITY_KEY = "city";
  private static final String ADDRESS_NEIGHBORHOOD_KEY = "neighborhood";
  private static final String ADDRESS_NUMBER_KEY = "number";
  private static final String ADDRESS_STREET_KEY = "street";
  private static final String ADDRESS_POSTAL_CODE_KEY = "postalCode";
  private static final String ADDRESS_LINE_KEY = "addressLine";
  private static final String ADDRESS_LATITUDE_KEY = "latitude";
  private static final String ADDRESS_LONGITUDE_KEY = "longitude";

  private static final String LOCATION_LATITUDE_KEY = "latitude";
  private static final String LOCATION_LONGITUDE_KEY = "longitude";
  private static final String LOCATION_TIMESTAMP_KEY = "timestamp";

  private static final String PAYMENT_ADDRESSES = "addresses";
  private static final String PAYMENT_ADDRESS_TYPE = "type";

  private static final String PAYMENT_VALUE = "paymentValue";
  private static final String PAYMENT_VALUE_AMOUNT = "amount";
  private static final String PAYMENT_VALUE_CURRENCY = "currency";
  private static final String PAYMENT_VALUE_INSTALLMENTS = "installments";
  private static final String PAYMENT_VALUE_DISCOUNT_AMOUNT = "discountAmount";

  private static final String PAYMENT_COUPON = "paymentCoupon";
  private static final String PAYMENT_COUPON_TYPE = "type";
  private static final String PAYMENT_COUPON_VALUE = "value";
  private static final String PAYMENT_COUPON_MAX_DISCOUNT = "maxDiscount";
  private static final String PAYMENT_COUPON_ID = "id";
  private static final String PAYMENT_COUPON_NAME = "name";

  private static final String PAYMENT_METHODS = "paymentMethods";
  private static final String PAYMENT_METHOD_TYPE = "type";
  private static final String PAYMENT_METHOD_IDENTIFIER = "identifier";
  private static final String PAYMENT_METHOD_BRAND = "brand";
  private static final String PAYMENT_METHOD_CREDIT_CARD_INFO = "creditCardInfo";
  private static final String PAYMENT_METHOD_DEBIT_CARD_INFO = "debitCardInfo";
  private static final String PAYMENT_METHOD_CARD_INFO_BIN = "bin";
  private static final String PAYMENT_METHOD_CARD_INFO_LAST_FOUR_DIGITS = "lastFourDigits";
  private static final String PAYMENT_METHOD_CARD_INFO_EXPIRY_MONTH = "expiryMonth";
  private static final String PAYMENT_METHOD_CARD_INFO_EXPIRY_YEAR = "expiryYear";

  public IncogniaModule(ReactApplicationContext reactContext) {
    super(reactContext);

    this.reactContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void initSdk() {
    try {
      Application application = (Application) reactContext.getApplicationContext();
      Incognia.init((Application) getReactApplicationContext().getApplicationContext());
    } catch (Exception e) {
      Log.e(NAME, "Error initializing Incognia SDK: " + e.getMessage(), e);
    }
  }

  @ReactMethod
  public void initSdkWithOptions(final ReadableMap optionsParameters) {
    try {
      Application application = (Application) reactContext.getApplicationContext();
      Incognia.init((Application) getReactApplicationContext().getApplicationContext());

      String appId = optionsParameters.hasKey(OPTIONS_APP_ID_KEY) ? optionsParameters.getString(OPTIONS_APP_ID_KEY) : null;
      boolean logEnabled = optionsParameters.hasKey(OPTIONS_LOG_ENABLED_KEY) ? optionsParameters.getBoolean(OPTIONS_LOG_ENABLED_KEY) : false;
      boolean locationEnabled = optionsParameters.hasKey(OPTIONS_LOCATION_ENABLED_KEY) ? optionsParameters.getBoolean(OPTIONS_LOCATION_ENABLED_KEY) : true;
      boolean installedAppsCollectionEnabled = optionsParameters.hasKey(OPTIONS_INSTALLED_APPS_COLLECTION_ENABLED_KEY) ? optionsParameters.getBoolean(OPTIONS_INSTALLED_APPS_COLLECTION_ENABLED_KEY) : false;

      IncogniaOptions options = new IncogniaOptions.Builder()
        .appId(appId)
        .logEnabled(logEnabled)
        .locationEnabled(locationEnabled)
        .installedAppsCollectionEnabled(installedAppsCollectionEnabled)
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
  public void setLocationEnabled(final boolean enabled) {
    Incognia.setLocationEnabled(enabled);
  }

  @ReactMethod
  public void generateRequestToken(final Promise promise) {
    Incognia.generateRequestToken( requestToken -> {
      if (requestToken != null) {
        promise.resolve(requestToken);
      } else {
        promise.reject(new Exception("Error while generating a request token."));
      }
    });
  }

  @ReactMethod
  public void sendCustomEvent(final ReadableMap parameters) {
    String eventAccountId = parameters.hasKey(EVENT_ACCOUNT_ID) ? parameters.getString(EVENT_ACCOUNT_ID) : null;
    String eventExternalId = parameters.hasKey(EVENT_EXTERNAL_ID) ? parameters.getString(EVENT_EXTERNAL_ID) : null;
    String eventTag = parameters.hasKey(EVENT_TAG) ? parameters.getString(EVENT_TAG) : null;
    String eventStatus = parameters.hasKey(EVENT_STATUS) ? parameters.getString(EVENT_STATUS) : null;
    EventProperties eventProperties = parameters.hasKey(EVENT_PROPERTIES) ? convertToEventProperties(parameters.getMap(EVENT_PROPERTIES)) : null;
    EventAddress eventAddress = parameters.hasKey(EVENT_ADDRESS) ? convertToEventAddress(parameters.getMap(EVENT_ADDRESS)) : null;

    CustomEvent customEvent = new CustomEvent.Builder()
      .accountId(eventAccountId)
      .externalId(eventExternalId)
      .address(eventAddress)
      .tag(eventTag)
      .properties(eventProperties)
      .status(eventStatus)
      .build();
    Incognia.sendCustomEvent(customEvent);
  }

  @ReactMethod
  public void sendOnboardingEvent(ReadableMap parameters) {
    String eventAccountId = parameters.hasKey(EVENT_ACCOUNT_ID) ? parameters.getString(EVENT_ACCOUNT_ID) : null;
    String eventExternalId = parameters.hasKey(EVENT_EXTERNAL_ID) ? parameters.getString(EVENT_EXTERNAL_ID) : null;
    String eventTag = parameters.hasKey(EVENT_TAG) ? parameters.getString(EVENT_TAG) : null;
    String eventStatus = parameters.hasKey(EVENT_STATUS) ? parameters.getString(EVENT_STATUS) : null;
    EventProperties eventProperties = parameters.hasKey(EVENT_PROPERTIES) ? convertToEventProperties(parameters.getMap(EVENT_PROPERTIES)) : null;
    EventAddress eventAddress = parameters.hasKey(EVENT_ADDRESS) ? convertToEventAddress(parameters.getMap(EVENT_ADDRESS)) : null;

    OnboardingEvent onboardingEvent = new OnboardingEvent.Builder()
      .accountId(eventAccountId)
      .externalId(eventExternalId)
      .address(eventAddress)
      .tag(eventTag)
      .properties(eventProperties)
      .status(eventStatus)
      .build();
    Incognia.sendOnboardingEvent(onboardingEvent);
  }

  @ReactMethod
  public void sendLoginEvent(ReadableMap parameters) {
    String eventAccountId = parameters.hasKey(EVENT_ACCOUNT_ID) ? parameters.getString(EVENT_ACCOUNT_ID) : null;
    String eventExternalId = parameters.hasKey(EVENT_EXTERNAL_ID) ? parameters.getString(EVENT_EXTERNAL_ID) : null;
    String eventTag = parameters.hasKey(EVENT_TAG) ? parameters.getString(EVENT_TAG) : null;
    String eventStatus = parameters.hasKey(EVENT_STATUS) ? parameters.getString(EVENT_STATUS) : null;
    EventProperties eventProperties = parameters.hasKey(EVENT_PROPERTIES) ? convertToEventProperties(parameters.getMap(EVENT_PROPERTIES)) : null;
    EventLocation eventLocation = parameters.hasKey(EVENT_LOCATION) ? convertToEventLocation(parameters.getMap(EVENT_LOCATION)) : null;

    LoginEvent loginEvent = new LoginEvent.Builder()
      .accountId(eventAccountId)
      .externalId(eventExternalId)
      .location(eventLocation)
      .tag(eventTag)
      .properties(eventProperties)
      .status(eventStatus)
      .build();
    Incognia.sendLoginEvent(loginEvent);
  }

  @ReactMethod
  public void sendPaymentEvent(ReadableMap parameters) {
    String eventAccountId = parameters.hasKey(EVENT_ACCOUNT_ID) ? parameters.getString(EVENT_ACCOUNT_ID) : null;
    String eventExternalId = parameters.hasKey(EVENT_EXTERNAL_ID) ? parameters.getString(EVENT_EXTERNAL_ID) : null;
    String eventTag = parameters.hasKey(EVENT_TAG) ? parameters.getString(EVENT_TAG) : null;
    String eventStatus = parameters.hasKey(EVENT_STATUS) ? parameters.getString(EVENT_STATUS) : null;
    String eventStoreId = parameters.hasKey(EVENT_STORE_ID) ? parameters.getString(EVENT_STORE_ID) : null;
    EventProperties eventProperties = parameters.hasKey(EVENT_PROPERTIES) ? convertToEventProperties(parameters.getMap(EVENT_PROPERTIES)) : null;
    EventLocation eventLocation = parameters.hasKey(EVENT_LOCATION) ? convertToEventLocation(parameters.getMap(EVENT_LOCATION)) : null;
    List<PaymentAddress> eventAddresses = parameters.hasKey(PAYMENT_ADDRESSES) ? convertPaymentAddressesReadableArrayToList(parameters.getArray(PAYMENT_ADDRESSES)) : null;
    PaymentValue eventPaymentValue = parameters.hasKey(PAYMENT_VALUE) ? convertToPaymentValue(parameters.getMap(PAYMENT_VALUE)) : null;
    PaymentCoupon eventPaymentCoupon = parameters.hasKey(PAYMENT_COUPON) ? convertToPaymentCoupon(parameters.getMap(PAYMENT_COUPON)) : null;
    List<PaymentMethod> eventPaymentMethods = parameters.hasKey(PAYMENT_METHODS) ? convertPaymentMethodsReadableArrayToList(parameters.getArray(PAYMENT_METHODS)) : null;

    PaymentEvent paymentEvent = new PaymentEvent.Builder()
      .accountId(eventAccountId)
      .externalId(eventExternalId)
      .location(eventLocation)
      .addresses(eventAddresses)
      .paymentValue(eventPaymentValue)
      .paymentCoupon(eventPaymentCoupon)
      .paymentMethods(eventPaymentMethods)
      .storeId(eventStoreId)
      .tag(eventTag)
      .properties(eventProperties)
      .status(eventStatus)
      .build();
    Incognia.sendPaymentEvent(paymentEvent);
  }

  private static EventProperties convertToEventProperties(ReadableMap readableMap) {
    if (readableMap == null) {
      return null;
    }

    Map<String, Object> map = readableMap.toHashMap();
    EventProperties eventProperties = new EventProperties();
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() instanceof String) {
        eventProperties.set(entry.getKey(), (String) entry.getValue());
      } else if (entry.getValue() instanceof Integer) {
        eventProperties.set(entry.getKey(), (Integer) entry.getValue());
      } else if (entry.getValue() instanceof Float) {
        eventProperties.set(entry.getKey(), (Float) entry.getValue());
      } else if (entry.getValue() instanceof Double) {
        eventProperties.set(entry.getKey(), (Double) entry.getValue());
      } else if (entry.getValue() instanceof Boolean) {
        eventProperties.set(entry.getKey(), (Boolean) entry.getValue());
      }
    }
    return eventProperties;
  }

  private static EventLocation convertToEventLocation(ReadableMap map) {
    if (map == null) {
      return null;
    }

    Double latitude = map.hasKey(LOCATION_LATITUDE_KEY) ? map.getDouble(LOCATION_LATITUDE_KEY) : null;
    Double longitude = map.hasKey(LOCATION_LONGITUDE_KEY) ? map.getDouble(LOCATION_LONGITUDE_KEY) : null;
    Long timestamp = map.hasKey(LOCATION_TIMESTAMP_KEY) ? (long) map.getDouble(LOCATION_TIMESTAMP_KEY) : null;

    return new EventLocation(latitude, longitude, timestamp);
  }

  private static PaymentValue convertToPaymentValue(ReadableMap map) {
    if (map == null) {
      return null;
    }

    Double amount = map.hasKey(PAYMENT_VALUE_AMOUNT) ? map.getDouble(PAYMENT_VALUE_AMOUNT) : null;
    String currency = map.hasKey(PAYMENT_VALUE_CURRENCY) ? map.getString(PAYMENT_VALUE_CURRENCY) : null;
    Integer installments = map.hasKey(PAYMENT_VALUE_INSTALLMENTS) ? (int) map.getDouble(PAYMENT_VALUE_INSTALLMENTS) : null;
    Double discountAmount = map.hasKey(PAYMENT_VALUE_DISCOUNT_AMOUNT) ? map.getDouble(PAYMENT_VALUE_DISCOUNT_AMOUNT) : null;

    return new PaymentValue.Builder(amount)
      .currency(currency)
      .installments(installments)
      .discountAmount(discountAmount)
      .build();
  }

  private static PaymentCoupon convertToPaymentCoupon(ReadableMap map) {
    if (map == null) {
      return null;
    }

    String type = map.hasKey(PAYMENT_COUPON_TYPE) ? map.getString(PAYMENT_COUPON_TYPE) : null;
    Double value = map.hasKey(PAYMENT_COUPON_VALUE) ? map.getDouble(PAYMENT_COUPON_VALUE) : null;
    Double maxDiscount = map.hasKey(PAYMENT_COUPON_MAX_DISCOUNT) ? map.getDouble(PAYMENT_COUPON_MAX_DISCOUNT) : null;
    String id = map.hasKey(PAYMENT_COUPON_ID) ? map.getString(PAYMENT_COUPON_ID) : null;
    String name = map.hasKey(PAYMENT_COUPON_NAME) ? map.getString(PAYMENT_COUPON_NAME) : null;

    return new PaymentCoupon.Builder()
      .type(type)
      .value(value)
      .maxDiscount(maxDiscount)
      .id(id)
      .name(name)
      .build();
  }

  private static List<PaymentMethod> convertPaymentMethodsReadableArrayToList(final ReadableArray array) {
    if (array == null) {
      return null;
    }

    List<PaymentMethod> list = new ArrayList<>();
    for (int i = 0; i < array.size(); i++) {
      PaymentMethod paymentMethod = convertMapToPaymentMethod(array.getMap(i));
      if (paymentMethod != null) {
        list.add(paymentMethod);
      }
    }
    return list;
  }

  @SuppressWarnings("ConstantConditions")
  private static PaymentMethod convertMapToPaymentMethod(ReadableMap map) {
    if (map == null) {
      return null;
    }

    String type = map.hasKey(PAYMENT_METHOD_TYPE) ? map.getString(PAYMENT_METHOD_TYPE) : null;
    String identifier = map.hasKey(PAYMENT_METHOD_IDENTIFIER) ? map.getString(PAYMENT_METHOD_IDENTIFIER) : null;
    String brand = map.hasKey(PAYMENT_METHOD_BRAND) ? map.getString(PAYMENT_METHOD_BRAND) : null;
    CardInfo creditCardInfo = map.hasKey(PAYMENT_METHOD_CREDIT_CARD_INFO) ? convertMapToCardInfo(map.getMap(PAYMENT_METHOD_CREDIT_CARD_INFO)) : null;
    CardInfo debitCardInfo = map.hasKey(PAYMENT_METHOD_DEBIT_CARD_INFO) ? convertMapToCardInfo(map.getMap(PAYMENT_METHOD_DEBIT_CARD_INFO)) : null;

    return new PaymentMethod.Builder()
      .type(type)
      .identifier(identifier)
      .brand(brand)
      .creditCardInfo(creditCardInfo)
      .debitCardInfo(debitCardInfo)
      .build();
  }

  @SuppressWarnings("ConstantConditions")
  private static CardInfo convertMapToCardInfo(ReadableMap map) {
    if (map == null) {
      return null;
    }

    String bin = map.hasKey(PAYMENT_METHOD_CARD_INFO_BIN) ? map.getString(PAYMENT_METHOD_CARD_INFO_BIN) : null;
    String lastFourDigits = map.hasKey(PAYMENT_METHOD_CARD_INFO_LAST_FOUR_DIGITS) ? map.getString(PAYMENT_METHOD_CARD_INFO_LAST_FOUR_DIGITS) : null;
    String expiryMonth = map.hasKey(PAYMENT_METHOD_CARD_INFO_EXPIRY_MONTH) ? map.getString(PAYMENT_METHOD_CARD_INFO_EXPIRY_MONTH) : null;
    String expiryYear = map.hasKey(PAYMENT_METHOD_CARD_INFO_EXPIRY_YEAR) ? map.getString(PAYMENT_METHOD_CARD_INFO_EXPIRY_YEAR) : null;

    return new CardInfo.Builder()
      .bin(bin)
      .lastFourDigits(lastFourDigits)
      .expiryMonth(expiryMonth)
      .expiryYear(expiryYear)
      .build();
  }

  private static List<PaymentAddress> convertPaymentAddressesReadableArrayToList(final ReadableArray array) {
    if (array == null) {
      return null;
    }

    List<PaymentAddress> list = new ArrayList<>();
    for (int i = 0; i < array.size(); i++) {
      PaymentAddress paymentAddress = convertMapToPaymentAddress(array.getMap(i));
      if (paymentAddress != null) {
        list.add(paymentAddress);
      }
    }
    return list;
  }

  @SuppressWarnings("ConstantConditions")
  private static PaymentAddress convertMapToPaymentAddress(ReadableMap map) {
    if (map == null) {
      return null;
    }

    EventAddress eventAddress = convertToEventAddress(map);
    String addressType = map.hasKey(PAYMENT_ADDRESS_TYPE) ? map.getString(PAYMENT_ADDRESS_TYPE) : null;

    return new PaymentAddress(addressType, eventAddress);
  }

  private static EventAddress convertToEventAddress(ReadableMap map) {
    if (map == null) {
      return null;
    }

    String countryCode = map.hasKey(ADDRESS_COUNTRY_CODE_KEY) ? map.getString(ADDRESS_COUNTRY_CODE_KEY) : null;
    String countryName = map.hasKey(ADDRESS_COUNTRY_NAME_KEY) ? map.getString(ADDRESS_COUNTRY_NAME_KEY) : null;
    String state = map.hasKey(ADDRESS_STATE_KEY) ? map.getString(ADDRESS_STATE_KEY) : null;
    String city = map.hasKey(ADDRESS_CITY_KEY) ? map.getString(ADDRESS_CITY_KEY) : null;
    String neighborhood = map.hasKey(ADDRESS_NEIGHBORHOOD_KEY) ? map.getString(ADDRESS_NEIGHBORHOOD_KEY) : null;
    String number = map.hasKey(ADDRESS_NUMBER_KEY) ? map.getString(ADDRESS_NUMBER_KEY) : null;
    String street = map.hasKey(ADDRESS_STREET_KEY) ? map.getString(ADDRESS_STREET_KEY) : null;
    String postalCode = map.hasKey(ADDRESS_POSTAL_CODE_KEY) ? map.getString(ADDRESS_POSTAL_CODE_KEY) : null;
    String addressLine = map.hasKey(ADDRESS_LINE_KEY) ? map.getString(ADDRESS_LINE_KEY) : null;
    Double latitude = map.hasKey(ADDRESS_LATITUDE_KEY) ? map.getDouble(ADDRESS_LATITUDE_KEY) : null;
    Double longitude = map.hasKey(ADDRESS_LONGITUDE_KEY) ? map.getDouble(ADDRESS_LONGITUDE_KEY) : null;

    Locale locale = map.hasKey(ADDRESS_LOCALE_KEY) ? localeFromString(map.getString(ADDRESS_LOCALE_KEY)) : null;
    if(locale == null && (street != null || addressLine != null)) {
      locale = Locale.getDefault();
    }

    return new EventAddress.Builder()
      .locale(locale)
      .countryCode(countryCode)
      .countryName(countryName)
      .state(state)
      .city(city)
      .neighborhood(neighborhood)
      .number(number)
      .street(street)
      .postalCode(postalCode)
      .addressLine(addressLine)
      .latitude(latitude)
      .longitude(longitude)
      .build();
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
