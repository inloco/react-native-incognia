
import { NativeModules } from 'react-native';
import { Platform } from 'react-native';

const { RNInLocoEngage } = NativeModules;

const CONSENT_TYPES = {
	ADDRESS_VALIDATION: "address_validation",
  	ADVERTISEMENT: "advertisement",
  	ENGAGE: "engage",
  	EVENTS: "analytics",
 	INSTALLED_APPS: "installed_apps",
  	LOCATION: "location",
	CONTEXT_PROVIDER: "context_provider",
	COVID_19_AID: "covid_19_aid",

	ALL: [
		"address_validation",
		"advertisement",
		"engage",
		"analytics",
		"installed_apps",
		"location",
		"context_provider"
	],
	NONE: []
};

const init = () => {
	RNInLocoEngage.initSdk();
}

const initWithOptions = (options) => {
	if (!('appId' in options)) options.appId = null;
	if (!('logsEnabled' in options)) options.logsEnabled = false;
	if (!('developmentDevices' in options)) options.developmentDevices = [];
	if (!('userPrivacyConsentRequired' in options)) options.userPrivacyConsentRequired = false;
	if (!('visitsEnabled' in options)) options.visitsEnabled = true;
	if (!('backgroundWakeupEnabled' in options)) options.backgroundWakeupEnabled = true;
	if (!('screenTrackingEnabled' in options)) options.screenTrackingEnabled = false;
	RNInLocoEngage.initSdkWithOptions(options);
}

const setUser = (userId) => {
	RNInLocoEngage.setUser(userId);
}

const clearUser = () => {
	RNInLocoEngage.clearUser();
}

const trackEvent = (name, properties) => {
	for (var property in properties) {
		if (properties.hasOwnProperty(property) && properties[property] != null) {
			properties[property] = properties[property].toString();
		}
	}
	RNInLocoEngage.trackEvent(name, properties);
}

const trackLocalizedEvent = (name, properties) => {
	for (var property in properties) {
		if (properties.hasOwnProperty(property) && properties[property] != null) {
			properties[property] = properties[property].toString();
		}
	}
	RNInLocoEngage.trackLocalizedEvent(name, properties);
}

const registerCheckIn = (placeName, placeId, properties, address) => {
	for (var property in properties) {
		if (properties.hasOwnProperty(property) && properties[property] != null) {
			properties[property] = properties[property].toString();
		}
	}
	if (Platform.OS == 'android') {
		RNInLocoEngage.registerCheckIn(placeName, placeId, properties);
	} else if (Platform.OS == 'ios') {
		if (address != null && "locale" in address)  {
			address.locale = address.locale.replace("-", "_");
		} 
		RNInLocoEngage.registerCheckIn(placeName, placeId, properties, address);
	}
}
 
const setPushProvider = (provider) => {
	const name = provider.name || null;
	const token = provider.token || null;
	RNInLocoEngage.setPushProvider(name, token);
}

const setFirebasePushProvider = (fcmToken) => {
	if (fcmToken) {
        setPushProvider({
          name: "google_fcm",
          token: fcmToken
		});
    }
}

const setPushNotificationsEnabled = (enabled) => {
	RNInLocoEngage.setPushNotificationsEnabled(enabled);
}

const isInLocoEngageMessage = (message) => {
	return 'in_loco_data' in message.data;
}

const presentNotification = (message, notificationId, channelId) => {
	if (Platform.OS == 'android') {
		notificationId = notificationId || 1111111;
		RNInLocoEngage.presentNotification(message.data, channelId, notificationId);
	}
}

const onNotificationClicked = (notification) => {
	if (Platform.OS == 'ios' && notification != null && 'in_loco_data' in notification.data) {
		RNInLocoEngage.didReceiveNotificationResponse(notification.data);
	}
}

const onAppLaunchedWithNotification = (notification) => {
	if (Platform.OS == 'ios' && notification != null && 'in_loco_data' in notification.data) {
		RNInLocoEngage.didFinishLaunchingWithMessage(notification.data);
	}
}

const getUrl = (notification) => {
	if (Platform.OS == 'ios' && notification != null && 'in_loco_data' in notification.data) {
		const inLocoData = JSON.parse(notification.data['in_loco_data']);
		return inLocoData.actions.main_action[0];
	}
	return null;
}

const setUserAddress = (address) => {
	if (address != null && "subThoroughfare" in address) {
		address.subThoroughfare = String(address.subThoroughfare);
	}
	if (Platform.OS == 'ios' && address != null && "locale" in address) {
		address.locale = address.locale.replace("-", "_");
	}  
	RNInLocoEngage.setUserAddress(address);
}

const clearUserAddress = () => {
	RNInLocoEngage.clearUserAddress();
}

const requestPrivacyConsent = (consentDialogOptions, consentTypes) => {
	return RNInLocoEngage.requestPrivacyConsent(consentDialogOptions, consentTypes);
}

const giveUserPrivacyConsent = (consentGiven) => {
	RNInLocoEngage.giveUserPrivacyConsent(consentGiven);
}

const giveUserPrivacyConsentForTypes = (consentTypes) => {
	RNInLocoEngage.giveUserPrivacyConsentForTypes(consentTypes);
}

const allowConsentTypes = (consentTypes) => {
	RNInLocoEngage.allowConsentTypes(consentTypes);
}

const setAllowedConsentTypes = (consentTypes) => {
	RNInLocoEngage.setAllowedConsentTypes(consentTypes);
}

const checkPrivacyConsentMissing = () => {
	return RNInLocoEngage.checkPrivacyConsentMissing();
}

const checkConsent = (consentTypes) => {
	return RNInLocoEngage.checkConsent(consentTypes);
}

const denyConsentTypes = (consentTypes) => {
	RNInLocoEngage.denyConsentTypes(consentTypes);
}

const getInstallationId = () => {
	return RNInLocoEngage.getInstallationId();
}

const trackSignUp = (signUpId, address) => {
	if (address != null && "subThoroughfare" in address) {
		address.subThoroughfare = String(address.subThoroughfare);
	}
	if (Platform.OS == 'ios' && address != null && "locale" in address) {
		address.locale = address.locale.replace("-", "_");
	}  
	RNInLocoEngage.trackSignUp(signUpId, address);
}

const trackLogin = (accountId) => {
	RNInLocoEngage.trackLogin(accountId);
}

export default {
	init,
	initWithOptions,
	setUser,
	clearUser,
	trackEvent,
	trackLocalizedEvent,
	registerCheckIn, 
	setPushProvider,
	setFirebasePushProvider,
	setPushNotificationsEnabled,
	isInLocoEngageMessage,
	presentNotification,
	onNotificationClicked,
	onAppLaunchedWithNotification,
	getUrl,
	setUserAddress,
	clearUserAddress,
	requestPrivacyConsent,
	giveUserPrivacyConsent,
	giveUserPrivacyConsentForTypes,
	allowConsentTypes,
	setAllowedConsentTypes,
	checkPrivacyConsentMissing,
	checkConsent,
	denyConsentTypes,
	getInstallationId,
	trackSignUp,
	trackLogin,
	CONSENT_TYPES
};
