import * as React from 'react';
import {
  request,
  requestMultiple,
  PERMISSIONS,
  RESULTS,
} from 'react-native-permissions';

import {
  StyleSheet,
  View,
  ScrollView,
  SafeAreaView,
  Button,
  Alert,
  Platform,
  Text,
} from 'react-native';

import Incognia from 'react-native-incognia';
import type { RequestTokenOptionsType } from 'react-native-incognia';

import { RequestTokenOptionsDialog } from './RequestTokenOptionsDialog';

type AppState = {
  requestTokenOptions: RequestTokenOptionsType;
  requestTokenOptionsDialogVisible: boolean;
  snackbarMessage?: string;
};

enum RequestTokenResultDisplay {
  AlertDialog = 'alert',
  Snackbar = 'snackbar',
}

export default class App extends React.Component<
  Record<string, never>,
  AppState
> {
  state: AppState = {
    requestTokenOptions: {
      androidRequestTokenOptions: {
        timeout: 12000,
        requestTokenMaxLength: 8000,
      },
    },
    requestTokenOptionsDialogVisible: false,
  };

  postInitFrame?: number;
  snackbarTimeout?: ReturnType<typeof setTimeout>;
  appMounted = false;

  showSnackbar = (message: string) => {
    if (!this.appMounted) return;
    if (this.snackbarTimeout) clearTimeout(this.snackbarTimeout);

    this.setState({ snackbarMessage: message });
    this.snackbarTimeout = setTimeout(() => {
      this.setState({ snackbarMessage: undefined });
    }, 3000);
  };

  showRequestTokenOptionsDialog = () => {
    this.setState({ requestTokenOptionsDialogVisible: true });
  };

  hideRequestTokenOptionsDialog = () => {
    this.setState({ requestTokenOptionsDialogVisible: false });
  };

  saveRequestTokenOptions = (requestTokenOptions: RequestTokenOptionsType) => {
    this.setState(
      {
        requestTokenOptions,
        requestTokenOptionsDialogVisible: false,
      },
      () => this.showSnackbar('RequestTokenOptions set')
    );
  };

  generateRequestTokenWithStatus = async (
    display: RequestTokenResultDisplay,
    requestTokenOptions?: RequestTokenOptionsType
  ) => {
    const requestTokenWithStatus = requestTokenOptions ?
      await Incognia.generateRequestTokenWithStatus(requestTokenOptions) : 
      await Incognia.generateRequestTokenWithStatus();
    const token = requestTokenWithStatus.token ?? 'null';
    const status = requestTokenWithStatus.status.toUpperCase();

    console.log(`RequestTokenWithStatus ${status}: ${token}`);
    if (display === RequestTokenResultDisplay.Snackbar) {
      this.showSnackbar(`Request token with status ${status} was generated`);
    } else {
      Alert.alert(`RequestTokenWithStatus ${status}`, token);
    }
  };

  requestPermissions = async () => {
    if (Platform.OS === 'ios') {
      await requestMultiple([
        PERMISSIONS.IOS.LOCATION_ALWAYS,
        PERMISSIONS.IOS.APP_TRACKING_TRANSPARENCY,
      ]);
    } else if (Platform.OS === 'android') {
      const result = await request(PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION);
      if (result === RESULTS.GRANTED) {
        await request(PERMISSIONS.ANDROID.ACCESS_BACKGROUND_LOCATION);
      }
    }
  };

  componentWillUnmount() {
    this.appMounted = false;
    if (this.postInitFrame !== undefined) {
      cancelAnimationFrame(this.postInitFrame);
    }
    if (this.snackbarTimeout) {
      clearTimeout(this.snackbarTimeout);
    }
  }

  componentDidMount() {
    this.appMounted = true;
    //* Toggle to switch between init with files or init with options
    // Incognia.initSdk();

    Incognia.initSdkWithOptions({
      androidOptions: {
        appId: 'ANDROID_APP_ID',
        logEnabled: true,
        locationEnabled: true,
        installedAppsCollectionEnabled: false,
        requestTokenMaxLength: 8000,
      },
      iosOptions: {
        appId: 'IOS_APP_ID',
        logEnabled: true,
        locationEnabled: true,
        urlSchemesCheckEnabled: true,
        deviceCheckTokenEnabled: true,
      },
    });

    this.postInitFrame = requestAnimationFrame(() => {
      this.requestPermissions();
      this.generateRequestTokenWithStatus(RequestTokenResultDisplay.Snackbar, {
        androidRequestTokenOptions: {
          ensureDataCollected: true,
        },
      });
    });
  }

  render() {
    return (
      <SafeAreaView style={styles.container}>
        <ScrollView>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Set account id"
              onPress={() => Incognia.setAccountId('rn-account-id')}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Clear account id"
              onPress={() => Incognia.clearAccountId()}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Report business unit id"
              onPress={() => Incognia.reportBusinessUnitId('food')}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Generate Request Token"
              onPress={async () => {
                let requestToken = await Incognia.generateRequestToken();
                Alert.alert('RequestToken', requestToken);
                console.log('RequestToken: ', requestToken);
              }}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Generate Request Token With Status"
              onPress={() =>
                this.generateRequestTokenWithStatus(
                  RequestTokenResultDisplay.AlertDialog
                )
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Set Request Token Options"
              onPress={this.showRequestTokenOptionsDialog}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Generate Request Token With Status Using Options"
              onPress={() =>
                this.generateRequestTokenWithStatus(
                  RequestTokenResultDisplay.AlertDialog,
                  this.state.requestTokenOptions
                )
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Set location enabled"
              onPress={() => Incognia.setLocationEnabled(true)}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Set location disabled"
              onPress={() => Incognia.setLocationEnabled(false)}
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Send custom event"
              onPress={() =>
                Incognia.sendCustomEvent({
                  accountId: 'rn-custom-account-id',
                  externalId: 'rn-custom-external-id',
                  address: {
                    locale: 'en-US',
                    countryName: 'United States',
                    countryCode: 'US',
                    state: 'New York',
                    city: 'New York City',
                    neighborhood: 'Manhattan',
                    number: '350',
                    street: 'Fifth Avenue',
                    postalCode: '10118',
                    latitude: 40.748817,
                    longitude: -73.985428,
                  },
                  tag: 'rn-tag',
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                  status: 'rn-success',
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Send onboarding event"
              onPress={() =>
                Incognia.sendOnboardingEvent({
                  accountId: 'rn-onboardung-account-id',
                  externalId: 'rn-onboarding-external-id',
                  address: {
                    locale: 'en-US',
                    countryName: 'United States',
                    countryCode: 'US',
                    state: 'New York',
                    city: 'New York City',
                    neighborhood: 'Manhattan',
                    number: '350',
                    street: 'Fifth Avenue',
                    postalCode: '10118',
                    latitude: 40.748817,
                    longitude: -73.985428,
                  },
                  tag: 'rn-tag',
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                  status: 'rn-success',
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Send login event"
              onPress={() =>
                Incognia.sendLoginEvent({
                  accountId: 'rn-login-account-id',
                  externalId: 'rn-login-external-id',
                  location: {
                    latitude: 40.748817,
                    longitude: -73.985428,
                    timestamp: 16489293979,
                  },
                  tag: 'rn-tag',
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                  status: 'rn-success',
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Send payment event"
              onPress={() =>
                Incognia.sendPaymentEvent({
                  accountId: 'rn-payment-account-id',
                  externalId: 'rn-payment-external-id',
                  location: {
                    latitude: 40.748817,
                    longitude: -73.985428,
                    timestamp: 16489293979,
                  },
                  addresses: [
                    {
                      type: Incognia.PaymentAddressTypes.BILLING,
                      locale: 'en-US',
                      countryName: 'United States',
                      countryCode: 'US',
                      state: 'New York',
                      city: 'New York City',
                      neighborhood: 'Manhattan',
                      number: '350',
                      street: 'Fifth Avenue',
                      postalCode: '10118',
                      latitude: 40.748817,
                      longitude: -73.985428,
                    },
                    {
                      type: Incognia.PaymentAddressTypes.SHIPPING,
                      locale: 'en-US',
                      countryName: 'United States',
                      countryCode: 'US',
                      state: 'New York',
                      city: 'New York City',
                      neighborhood: 'Manhattan',
                      number: '350',
                      street: 'Fifth Avenue',
                      postalCode: '10118',
                      latitude: 40.748817,
                      longitude: -73.985428,
                    },
                  ],
                  paymentValue: {
                    amount: 100.0,
                    currency: 'USD',
                    installments: 1,
                    discountAmount: 10.0,
                  },
                  paymentCoupon: {
                    type: Incognia.PaymentCouponTypes.PERCENT_OFF,
                    value: 10,
                    maxDiscount: 10.0,
                    id: 'rn-coupon-id',
                    name: 'rn-coupon-name',
                  },
                  paymentMethods: [
                    {
                      type: Incognia.PaymentMethodTypes.CREDIT_CARD,
                      identifier: 'rn-credit-card-id',
                      brand: Incognia.PaymentMethodBrands.VISA,
                      creditCardInfo: {
                        bin: '123456',
                        lastFourDigits: '7890',
                        expiryMonth: '11',
                        expiryYear: '2023',
                      },
                    },
                    {
                      type: Incognia.PaymentMethodTypes.DEBIT_CARD,
                      identifier: 'rn-debit-card-id',
                      brand: Incognia.PaymentMethodBrands.MASTERCARD,
                      debitCardInfo: {
                        bin: '123456',
                        lastFourDigits: '7890',
                        expiryMonth: '11',
                        expiryYear: '2023',
                      },
                    },
                  ],
                  storeId: 'rn-store-id',
                  tag: 'rn-tag',
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                  status: 'rn-success',
                })
              }
            />
          </View>
        </ScrollView>
        <RequestTokenOptionsDialog
          onCancel={this.hideRequestTokenOptionsDialog}
          onSave={this.saveRequestTokenOptions}
          requestTokenOptions={this.state.requestTokenOptions}
          visible={this.state.requestTokenOptionsDialogVisible}
        />
        {this.state.snackbarMessage ? (
          <View style={styles.snackbar}>
            <Text style={styles.snackbarText}>
              {this.state.snackbarMessage}
            </Text>
          </View>
        ) : null}
      </SafeAreaView>
    );
  }
}

const color = '#4D2C4C';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: '#F5FCFF',
  },
  buttonContainer: {
    margin: 10,
  },
  snackbar: {
    backgroundColor: '#323232',
    bottom: 16,
    left: 16,
    paddingHorizontal: 16,
    paddingVertical: 14,
    position: 'absolute',
    right: 16,
  },
  snackbarText: {
    color: '#FFFFFF',
  },
});
