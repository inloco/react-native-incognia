import { NativeModules, Platform } from 'react-native';
import {
  sendLoginEvent,
  sendOnboardingEvent,
  sendPaymentEvent,
} from '../index';

jest.mock('react-native', () => ({
  NativeModules: {
    IncogniaModule: {
      initSdk: jest.fn(),
      initSdkWithOptions: jest.fn(),
      setAccountId: jest.fn(),
      clearAccountId: jest.fn(),
      setLocationEnabled: jest.fn(),
      generateRequestToken: jest.fn(),
      generateRequestTokenWithStatus: jest.fn(),
      reportBusinessUnitId: jest.fn(),
      trackLocalizedEvent: jest.fn(),
      trackSignupSent: jest.fn(),
      trackLoginSucceeded: jest.fn(),
      trackPaymentSent: jest.fn(),
      sendCustomEvent: jest.fn(),
      sendOnboardingEvent: jest.fn(),
      sendLoginEvent: jest.fn(),
      sendPaymentEvent: jest.fn(),
    },
  },
  Platform: { OS: 'ios' },
}));

const IncogniaModule = NativeModules.IncogniaModule;

const setPlatform = (os: string) => {
  (Platform as unknown as { OS: string }).OS = os;
};

const firstCallArgument = (mock: jest.Mock) => mock.mock.calls[0][0];

describe('sendLoginEvent', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('sends the account id on the key the iOS module reads', () => {
    setPlatform('ios');

    sendLoginEvent({ accountId: 'account-123', externalId: 'login-456' });

    const parameters = firstCallArgument(IncogniaModule.trackLoginSucceeded);
    expect(parameters.accountId).toBe('account-123');
  });

  it('keeps sending the external id and the account id event property on iOS', () => {
    setPlatform('ios');

    sendLoginEvent({ accountId: 'account-123', externalId: 'login-456' });

    const parameters = firstCallArgument(IncogniaModule.trackLoginSucceeded);
    expect(parameters.external_id).toBe('login-456');
    expect(parameters.reactProperties.account_id).toBe('account-123');
  });

  it('keeps sending the login payload on iOS', () => {
    setPlatform('ios');

    sendLoginEvent({
      accountId: 'account-123',
      externalId: 'login-456',
      location: { latitude: 1, longitude: 2, timestamp: 3 },
      tag: 'a-tag',
      status: 'a-status',
      properties: { anInt: 1, aBool: true, aString: 'a-value' },
    });

    const parameters = firstCallArgument(IncogniaModule.trackLoginSucceeded);
    expect(JSON.parse(parameters.reactProperties.rn_lgn)).toEqual({
      loc: { lat: 1, lng: 2, ts: 3 },
      base: {
        evnt_tag: 'a-tag',
        stts: 'a-status',
        prop: { anInt: '1', aBool: 'true', aString: 'a-value' },
      },
    });
  });

  it('sends the parameters as they are on Android', () => {
    setPlatform('android');

    sendLoginEvent({ accountId: 'account-123', externalId: 'login-456' });

    expect(IncogniaModule.trackLoginSucceeded).not.toHaveBeenCalled();
    expect(firstCallArgument(IncogniaModule.sendLoginEvent)).toEqual({
      accountId: 'account-123',
      externalId: 'login-456',
    });
  });
});

describe('the other point of view events', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    setPlatform('ios');
  });

  it('sends the onboarding event with the account id as an event property', () => {
    sendOnboardingEvent({ accountId: 'account-123', externalId: 'signup-456' });

    const parameters = firstCallArgument(IncogniaModule.trackSignupSent);
    expect(parameters.external_id).toBe('signup-456');
    expect(parameters.reactProperties.account_id).toBe('account-123');
  });

  it('sends the payment event with the account id as an event property', () => {
    sendPaymentEvent({ accountId: 'account-123', externalId: 'payment-456' });

    const parameters = firstCallArgument(IncogniaModule.trackPaymentSent);
    expect(parameters.external_id).toBe('payment-456');
    expect(parameters.reactProperties.account_id).toBe('account-123');
  });
});
