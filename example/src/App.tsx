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
} from 'react-native';

import Incognia from 'react-native-incognia';

export default class App extends React.Component {
  componentDidMount() {
    //* Toggle to switch between init with files or init with options
    Incognia.initSdk();
    /*/ 
    Incognia.initSdkWithOptions({
      androidOptions: {
        appId: '7dd3c618-280e-42b7-bd39-1ac302663ed1',
        logEnabled: false,
        locationEnabled: true,
        // installedAppsCollectionEnabled: false,
      },
      iosOptions: {
        appId: '7dd3c618-280e-42b7-bd39-1ac302663ed1',
        logEnabled: false,
        locationEnabled: true,
        // urlSchemesCheckEnabled: false,
      },
    });
    //*/

    if (Platform.OS === 'ios') {
      requestMultiple([
        PERMISSIONS.IOS.LOCATION_ALWAYS,
        PERMISSIONS.IOS.APP_TRACKING_TRANSPARENCY,
      ]);
    } else if (Platform.OS === 'android') {
      request(PERMISSIONS.ANDROID.ACCESS_FINE_LOCATION).then((result) => {
        if (result === RESULTS.GRANTED) {
          request(PERMISSIONS.ANDROID.ACCESS_BACKGROUND_LOCATION);
        }
      });
    }
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
});
