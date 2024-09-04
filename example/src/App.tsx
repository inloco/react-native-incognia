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
              title="Track event"
              onPress={() =>
                Incognia.trackEvent({
                  eventName: 'rn-event-name',
                  eventProperties: {
                    boolParam: true,
                    intParam: 1,
                    floatParam: 0.3,
                    stringParam: 'param',
                  },
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Track localized event"
              onPress={() =>
                Incognia.trackLocalizedEvent({
                  eventName: 'rn-localized-event-name',
                  eventProperties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Request privacy consent dialog"
              onPress={() =>
                Incognia.requestPrivacyConsent({
                  dialogTitle: 'Title text',
                  dialogMessage: 'Message text',
                  dialogAcceptText: 'Accept',
                  dialogDenyText: 'Deny',
                  consentTypes: Incognia.ConsentTypes.ALL,
                }).then((data) => {
                  Alert.alert(
                    'Privacy consent request result: ' + JSON.stringify(data)
                  );
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Check consent"
              onPress={() =>
                Incognia.checkConsent(Incognia.ConsentTypes.ALL).then(
                  (data) => {
                    Alert.alert(
                      'Privacy consent check result: ' + JSON.stringify(data)
                    );
                  }
                )
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Allow consent types"
              onPress={() =>
                Incognia.allowConsentTypes(Incognia.ConsentTypes.ALL)
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Deny consent types"
              onPress={() =>
                Incognia.denyConsentTypes(Incognia.ConsentTypes.ALL)
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Fetch installation id"
              onPress={async () =>
                Alert.alert(
                  'InstallationId: ' + (await Incognia.fetchInstallationId())
                )
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Generate Request Token"
              onPress={async () =>
                Alert.alert(
                  'RequestToken: ' + (await Incognia.generateRequestToken())
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
              title="Track signup sent"
              onPress={() =>
                Incognia.Trial.trackSignupSent({
                  signupId: 'rn-signup-id',
                  signupAddress: {
                    locale: 'pt-BR',
                    countryName: 'Brasil',
                    countryCode: 'BR',
                    adminArea: 'Pernambuco',
                    subAdminArea: 'Recife',
                    locality: 'Recife',
                    subLocality: 'Pina',
                    thoroughfare: 'Av. Engenheiro Antônio de Goes',
                    subThoroughfare: '300',
                    postalCode: '51110-100',
                    latitude: -8.088109,
                    longitude: -34.883838,
                  },
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Track login succeeded"
              onPress={() =>
                Incognia.Trial.trackLoginSucceeded({
                  accountId: 'rn-account-id',
                  loginId: 'rn-login-id',
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Track payment sent"
              onPress={() =>
                Incognia.Trial.trackPaymentSent({
                  transactionId: 'rn-transaction-id',
                  transactionAddresses: [
                    {
                      locale: 'pt-BR',
                      countryName: 'Brasil',
                      countryCode: 'BR',
                      adminArea: 'Pernambuco',
                      subAdminArea: 'Recife',
                      locality: 'Recife',
                      subLocality: 'Pina',
                      thoroughfare: 'Av. Engenheiro Antônio de Goes',
                      subThoroughfare: '300',
                      postalCode: '51110-100',
                      latitude: -8.088109,
                      longitude: -34.883838,
                      type: Incognia.Trial.TransactionAddressTypes.BILLING,
                      addressLine:
                        'Av. Engenheiro Antônio de Goes, 300, Pina, Recife, Pernambuco, Brasil',
                    },
                  ],
                  properties: {
                    boolParam: false,
                    intParam: 2,
                    floatParam: 0.5,
                    stringParam: 'param',
                  },
                })
              }
            />
          </View>
          <View style={styles.buttonContainer}>
            <Button
              color={color}
              title="Notify app in foreground"
              onPress={() => Incognia.notifyAppInForeground()}
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
