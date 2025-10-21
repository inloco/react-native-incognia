import { NativeModules, Platform } from 'react-native';

type IncogniaType = {
  initSdk(): void;
  initSdkWithOptions(options: IncogniaOptionsType): void;
  setAccountId(accountId: string): void;
  clearAccountId(): void;
  setLocationEnabled(enabled: boolean): void;
  generateRequestToken(): Promise<string>;
  sendCustomEvent(params: CustomEventParamsType): void;
  sendOnboardingEvent(params: OnboardingEventParamsType): void;
  sendLoginEvent(params: LoginEventParamsType): void;
  sendPaymentEvent(params: PaymentEventParamsType): void;
  PaymentAddressTypes: PaymentAddressTypesType;
  PaymentCouponTypes: PaymentCouponTypesType;
  PaymentMethodTypes: PaymentMethodTypesType;
  PaymentMethodBrands: PaymentMethodBrandsType;
};

type IncogniaOptionsType = {
  androidOptions: AndroidOptionsType;
  iosOptions: IOSOptionsType;
};

type AndroidOptionsType = {
  appId: string;
  logEnabled?: boolean;
  locationEnabled?: boolean;
  installedAppsCollectionEnabled?: boolean;
};

type IOSOptionsType = {
  appId: string;
  logEnabled?: boolean;
  locationEnabled?: boolean;
  urlSchemesCheckEnabled?: boolean;
};

type PaymentAddressTypesType = {
  readonly BILLING: string;
  readonly SHIPPING: string;
  readonly HOME: string;
};

type PaymentCouponTypesType = {
  readonly PERCENT_OFF: string;
  readonly FIXED_VALUE: string;
};

type PaymentMethodTypesType = {
  readonly CREDIT_CARD: string;
  readonly DEBIT_CARD: string;
  readonly APPLE_PAY: string;
  readonly GOOGLE_PAY: string;
  readonly NU_PAY: string;
  readonly PIX: string;
  readonly MEAL_VOUCHER: string;
};

type PaymentMethodBrandsType = {
  readonly VISA: string;
  readonly MASTERCARD: string;
  readonly AMERICAN_EXPRESS: string;
  readonly TARJETA_NARANJA: string;
  readonly CABAL: string;
  readonly ARGENCARD: string;
};

type CustomEventParamsType = {
  accountId?: string;
  externalId?: string;
  address?: EventAddressType;
  tag?: string;
  properties?: { [key: string]: string | number | boolean };
  status?: string;
};

type OnboardingEventParamsType = {
  accountId?: string;
  externalId?: string;
  address?: EventAddressType;
  tag?: string;
  properties?: { [key: string]: string | number | boolean };
  status?: string;
};

type LoginEventParamsType = {
  accountId: string;
  externalId?: string;
  location?: EventLocationType;
  tag?: string;
  properties?: { [key: string]: string | number | boolean };
  status?: string;
};

type PaymentEventParamsType = {
  accountId: string;
  externalId?: string;
  location?: EventLocationType;
  addresses?: Array<PaymentAddressType>;
  paymentValue?: PaymentValueType;
  paymentCoupon?: PaymentCouponType;
  paymentMethods?: Array<PaymentMethodType>;
  storeId?: string;
  tag?: string;
  properties?: { [key: string]: string | number | boolean };
  status?: string;
};

type EventLocationType = {
  latitude: number;
  longitude: number;
  timestamp?: number;
};

type EventAddressType = {
  locale?: string;
  countryCode?: string;
  countryName?: string;
  state?: string;
  city?: string;
  neighborhood?: string;
  number?: string;
  street?: string;
  postalCode?: string;
  addressLine?: string;
  latitude?: number;
  longitude?: number;
};

type PaymentAddressType = EventAddressType & {
  type: string;
};

type PaymentValueType = {
  amount: number;
  currency?: string;
  installments?: number;
  discountAmount?: number;
};

type PaymentCouponType = {
  type: string;
  value?: number;
  maxDiscount?: number;
  id?: string;
  name?: string;
};

type PaymentMethodType = {
  type: string;
  identifier?: string;
  brand?: string;
  creditCardInfo?: CardInfoType;
  debitCardInfo?: CardInfoType;
};

type CardInfoType = {
  bin: string;
  lastFourDigits: string;
  expiryYear?: string;
  expiryMonth?: string;
};

const transformToStringMap = (
  obj: { [key: string]: string | number | boolean } | undefined
): { [key: string]: string } | undefined => {
  return obj
    ? Object.fromEntries(Object.entries(obj).map(([k, v]) => [k, String(v)]))
    : undefined;
};

const { IncogniaModule } = NativeModules;

export const initSdk = IncogniaModule.initSdk;
export const initSdkWithOptions = (params: IncogniaOptionsType) => {
  if (Platform.OS === 'ios') {
    IncogniaModule.initSdkWithOptions(params.iosOptions);
  } else {
    IncogniaModule.initSdkWithOptions(params.androidOptions);
  }
};

export const setAccountId = IncogniaModule.setAccountId;
export const clearAccountId = IncogniaModule.clearAccountId;
export const setLocationEnabled = IncogniaModule.setLocationEnabled;
export const generateRequestToken = IncogniaModule.generateRequestToken;

export const sendCustomEvent = (params: CustomEventParamsType) => {
  if (Platform.OS === 'ios') {
    let reactParams = {
      reactProperties: {
        account_id: params.accountId,
        rn_ctm: JSON.stringify({
          add: {
            country_name: params.address?.countryName,
            country_code: params.address?.countryCode,
            admin_area: params.address?.state,
            locality: params.address?.city,
            sub_locality: params.address?.neighborhood,
            thoroughfare: params.address?.street,
            sub_thoroughfare: params.address?.number,
            postal_code: params.address?.postalCode,
            address_line: params.address?.addressLine,
            locale: params.address?.locale,
            latitude: params.address?.latitude,
            longitude: params.address?.longitude,
          },
          base: {
            ext_id: params.externalId,
            evnt_tag: params.tag,
            stts: params.status,
            prop: transformToStringMap(params.properties),
          },
        }),
      },
    };

    IncogniaModule.trackLocalizedEvent(reactParams);
  } else {
    IncogniaModule.sendCustomEvent(params);
  }
};

export const sendOnboardingEvent = (params: OnboardingEventParamsType) => {
  if (Platform.OS === 'ios') {
    let reactParams = {
      reactProperties: {
        account_id: params.accountId,
        rn_onbrd: JSON.stringify({
          add: {
            country_name: params.address?.countryName,
            country_code: params.address?.countryCode,
            admin_area: params.address?.state,
            locality: params.address?.city,
            sub_locality: params.address?.neighborhood,
            thoroughfare: params.address?.street,
            sub_thoroughfare: params.address?.number,
            postal_code: params.address?.postalCode,
            address_line: params.address?.addressLine,
            locale: params.address?.locale,
            latitude: params.address?.latitude,
            longitude: params.address?.longitude,
          },
          base: {
            evnt_tag: params.tag,
            stts: params.status,
            prop: transformToStringMap(params.properties),
          },
        }),
      },
    };

    IncogniaModule.trackSignupSent(reactParams);
  } else {
    IncogniaModule.sendOnboardingEvent(params);
  }
};

export const sendLoginEvent = (params: LoginEventParamsType) => {
  if (Platform.OS === 'ios') {
    let reactParams = {
      reactProperties: {
        account_id: params.accountId,
        rn_lgn: JSON.stringify({
          loc: {
            lat: params.location?.latitude,
            lng: params.location?.longitude,
            ts: params.location?.timestamp,
          },
          base: {
            evnt_tag: params.tag,
            stts: params.status,
            prop: transformToStringMap(params.properties),
          },
        }),
      },
    };

    IncogniaModule.trackLoginSucceeded(reactParams);
  } else {
    IncogniaModule.sendLoginEvent(params);
  }
};

export const sendPaymentEvent = (params: PaymentEventParamsType) => {
  if (Platform.OS === 'ios') {
    let addresses;
    if (params.addresses) {
      addresses = params.addresses.map((address) => ({
        tp: address.type,
        add: {
          country_name: address?.countryName,
          country_code: address?.countryCode,
          admin_area: address?.state,
          locality: address?.city,
          sub_locality: address?.neighborhood,
          thoroughfare: address?.street,
          sub_thoroughfare: address?.number,
          postal_code: address?.postalCode,
          address_line: address?.addressLine,
          locale: address?.locale,
          latitude: address?.latitude,
          longitude: address?.longitude,
        },
      }));
    }

    let paymentMethods;
    if (params.paymentMethods) {
      paymentMethods = params.paymentMethods.map((method) => ({
        tp: method.type,
        id: method.identifier,
        brnd: method.brand,
        cc_inf: method.creditCardInfo
          ? {
              bin: method.creditCardInfo.bin,
              lst_fr_dgt: method.creditCardInfo.lastFourDigits,
              exp_yr: method.creditCardInfo.expiryYear,
              exp_mnth: method.creditCardInfo.expiryMonth,
            }
          : undefined,
        dc_inf: method.debitCardInfo
          ? {
              bin: method.debitCardInfo.bin,
              lst_fr_dgt: method.debitCardInfo.lastFourDigits,
              exp_yr: method.debitCardInfo.expiryYear,
              exp_mnth: method.debitCardInfo.expiryMonth,
            }
          : undefined,
      }));
    }

    let reactParams = {
      reactProperties: {
        account_id: params.accountId,
        rn_paymnt: JSON.stringify({
          str_id: params.storeId,
          add: addresses,
          val: {
            amnt: params.paymentValue?.amount,
            curc: params.paymentValue?.currency,
            insmts: params.paymentValue?.installments,
            dc_amnt: params.paymentValue?.discountAmount,
          },
          cpn: {
            tp: params.paymentCoupon?.type,
            vle: params.paymentCoupon?.value,
            mx_dsc: params.paymentCoupon?.maxDiscount,
            id: params.paymentCoupon?.id,
            nm: params.paymentCoupon?.name,
          },
          mthds: paymentMethods,
          loc: {
            lat: params.location?.latitude,
            lng: params.location?.longitude,
            ts: params.location?.timestamp,
          },
          base: {
            evnt_tag: params.tag,
            stts: params.status,
            prop: transformToStringMap(params.properties),
          },
        }),
      },
    };

    IncogniaModule.trackPaymentSent(reactParams);
  } else {
    IncogniaModule.sendPaymentEvent(params);
  }
};

export const PaymentAddressTypes: PaymentAddressTypesType = {
  BILLING: 'billing',
  HOME: 'home',
  SHIPPING: 'shipping',
};

export const PaymentCouponTypes: PaymentCouponTypesType = {
  PERCENT_OFF: 'percent_off',
  FIXED_VALUE: 'fixed_value',
};

export const PaymentMethodTypes: PaymentMethodTypesType = {
  CREDIT_CARD: 'credit_card',
  DEBIT_CARD: 'debit_card',
  APPLE_PAY: 'apple_pay',
  GOOGLE_PAY: 'google_pay',
  NU_PAY: 'nu_pay',
  PIX: 'pix',
  MEAL_VOUCHER: 'meal_voucher',
};

export const PaymentMethodBrands: PaymentMethodBrandsType = {
  VISA: 'visa',
  MASTERCARD: 'mastercard',
  AMERICAN_EXPRESS: 'american_express',
  TARJETA_NARANJA: 'tarjeta_naranja',
  CABAL: 'cabal_brand',
  ARGENCARD: 'argencard_brand',
};

export default {
  initSdk,
  initSdkWithOptions,
  setAccountId,
  clearAccountId,
  setLocationEnabled,
  generateRequestToken,
  sendCustomEvent,
  sendOnboardingEvent,
  sendLoginEvent,
  sendPaymentEvent,
  PaymentAddressTypes,
  PaymentCouponTypes,
  PaymentMethodTypes,
  PaymentMethodBrands,
} as IncogniaType;
