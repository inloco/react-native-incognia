import { NativeModules, Platform } from 'react-native';

type IncogniaType = {
  setAccountId(accountId: string): void;
  clearAccountId(): void;
  trackEvent(params: TrackEventParamsType): void;
  trackLocalizedEvent(params: TrackEventParamsType): void;
  requestPrivacyConsent(params: ConsentRequestParamsType): Promise<any>;
  checkConsent(consentTypes: Array<string>): Promise<any>;
  allowConsentTypes(consentTypes: Array<string>): void;
  denyConsentTypes(consentTypes: Array<string>): void;
  fetchInstallationId(): Promise<string>;
  setLocationEnabled(enabled: boolean): void;
  notifyAppInForeground(): void;
  generateRequestToken(): Promise<string>;
  ConsentTypes: ConsentTypesType;
  Trial: IncogniaTrialType;
};

type IncogniaTrialType = {
  trackSignupSent(params: TrackSignupParamsType): void;
  trackLoginSucceeded(params: TrackLoginSucceededParamsType): void;
  trackPaymentSent(params: TrackPaymentParamsType): void;
  TransactionAddressTypes: TranscationAddressTypesType;
};

type ConsentTypesType = {
  readonly ADDRESS_VALIDATION: string;
  readonly ADVERTISEMENT: string;
  readonly ENGAGE: string;
  readonly EVENTS: string;
  readonly LOCATION: string;
  readonly CONTEXT_PROVIDER: string;
  readonly ALL: Array<string>;
  readonly NONE: Array<string>;
};

type TranscationAddressTypesType = {
  readonly BILLING: string;
  readonly SHIPPING: string;
  readonly HOME: string;
};

type TrackEventParamsType = {
  eventName: string;
  eventProperties?: Object;
};

type TrackSignupParamsType = {
  signupId?: string;
  signupAddress?: UserAddressType;
  properties?: Object;
};

type TrackLoginSucceededParamsType = {
  loginId?: string;
  accountId?: string;
  properties?: Object;
};

type TrackPaymentParamsType = {
  transactionId?: string;
  transactionAddresses?: Array<TransactionAddressType>;
  properties?: Object;
};

type UserAddressType = {
  locale?: string;
  countryName?: string;
  countryCode?: string;
  adminArea?: string;
  subAdminArea?: string;
  locality?: string;
  subLocality?: string;
  thoroughfare?: string;
  subThoroughfare?: string;
  postalCode?: string;
  latitude?: number;
  longitude?: number;
  addressLine?: string;
};

type TransactionAddressType = UserAddressType & {
  type: string;
};

type ConsentRequestParamsType = {
  dialogTitle: string;
  dialogMessage: string;
  dialogAcceptText: string;
  dialogDenyText: string;
  consentTypes: Array<string>;
};

const { IncogniaModule } = NativeModules;

export const setAccountId = IncogniaModule.setAccountId;
export const clearAccountId = IncogniaModule.clearAccountId;
export const trackEvent = IncogniaModule.trackEvent;
export const trackLocalizedEvent = IncogniaModule.trackLocalizedEvent;
export const requestPrivacyConsent = IncogniaModule.requestPrivacyConsent;
export const checkConsent = IncogniaModule.checkConsent;
export const allowConsentTypes = IncogniaModule.allowConsentTypes;
export const denyConsentTypes = IncogniaModule.denyConsentTypes;
export const fetchInstallationId = IncogniaModule.fetchInstallationId;
export const setLocationEnabled = IncogniaModule.setLocationEnabled;
export const generateRequestToken = IncogniaModule.generateRequestToken;
export const notifyAppInForeground = () => {
  if (Platform.OS === 'android') {
    IncogniaModule.notifyAppInForeground();
  }
};

export const ConsentTypes: ConsentTypesType = {
  ADDRESS_VALIDATION: IncogniaModule.CONSENT_TYPE_ADDRESS_VALIDATION,
  ADVERTISEMENT: IncogniaModule.CONSENT_TYPE_ADVERTISEMENT,
  ENGAGE: IncogniaModule.CONSENT_TYPE_ENGAGE,
  EVENTS: IncogniaModule.CONSENT_TYPE_EVENTS,
  LOCATION: IncogniaModule.CONSENT_TYPE_LOCATION,
  CONTEXT_PROVIDER: IncogniaModule.CONSENT_TYPE_CONTEXT_PROVIDER,
  ALL: IncogniaModule.CONSENT_TYPES_ALL,
  NONE: IncogniaModule.CONSENT_TYPES_NONE,
};

export const Trial: IncogniaTrialType = {
  trackSignupSent: IncogniaModule.trackSignupSent,
  trackLoginSucceeded: IncogniaModule.trackLoginSucceeded,
  trackPaymentSent: IncogniaModule.trackPaymentSent,
  TransactionAddressTypes: {
    BILLING: 'billing',
    HOME: 'home',
    SHIPPING: 'shipping',
  },
};

export default {
  setAccountId,
  clearAccountId,
  trackEvent,
  trackLocalizedEvent,
  requestPrivacyConsent,
  checkConsent,
  allowConsentTypes,
  denyConsentTypes,
  fetchInstallationId,
  setLocationEnabled,
  notifyAppInForeground,
  generateRequestToken,
  Trial,
  ConsentTypes,
} as IncogniaType;
