
#import "RNInLocoEngage.h"

#define OPTIONS_APP_ID                        @"appId"
#define OPTIONS_LOGS_ENABLED                  @"logsEnabled"
#define OPTIONS_DEVELOPMENT_DEVICES           @"developmentDevices"
#define OPTIONS_VISITS_ENABLED                @"visitsEnabled"
#define OPTIONS_REQUIRES_USER_PRIVACY_CONSENT @"userPrivacyConsentRequired"
#define OPTIONS_SCREEN_TRACKING_ENABLED       @"screenTrackingEnabled"

#define OPTIONS_CONSENT_DIALOG_TITLE          @"consentDialogTitle"
#define OPTIONS_CONSENT_DIALOG_MESSAGE        @"consentDialogMessage"
#define OPTIONS_CONSENT_DIALOG_ACCEPT_TEXT    @"consentDialogAcceptText"
#define OPTIONS_CONSENT_DIALOG_DENY_TEXT      @"consentDialogDenyText"

#define ADDRESS_LOCALE_KEY                    @"locale"
#define ADDRESS_COUNTRY_NAME_KEY              @"countryName"
#define ADDRESS_COUNTRY_CODE_KEY              @"countryCode"
#define ADDRESS_ADMIN_AREA_KEY                @"adminArea"
#define ADDRESS_SUBADMIN_AREA_KEY             @"subAdminArea"
#define ADDRESS_LOCALITY_KEY                  @"locality"
#define ADDRESS_SUB_LOCALITY_KEY              @"subLocality"
#define ADDRESS_THOROUGHFARE_KEY              @"thoroughfare"
#define ADDRESS_SUB_THOROUGHFARE_KEY          @"subThoroughfare"
#define ADDRESS_POSTAL_CODE_KEY               @"postalCode"
#define ADDRESS_LATITUDE_KEY                  @"latitude"
#define ADDRESS_LONGITUDE_KEY                 @"longitude"
#define ADDRESS_LINE_KEY                      @"addressLine"

@implementation RNInLocoEngage

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(initSdk) {
    [ICGIncognia initSdk];
}

RCT_EXPORT_METHOD(initSdkWithOptions:(NSDictionary *)optionsDict) {
    ICGOptions *options = [[ICGOptions alloc] init];
    [options setApplicationId:[optionsDict objectForKey:OPTIONS_APP_ID]];
    [options setLogEnabled:[[optionsDict objectForKey:OPTIONS_LOGS_ENABLED] boolValue]];
    [options setDevelopmentDevices:[optionsDict objectForKey:OPTIONS_DEVELOPMENT_DEVICES]];
    [options setLocationEnabled:[[optionsDict objectForKey:OPTIONS_VISITS_ENABLED] boolValue]];
    [options setScreenTrackingEnabled:[[optionsDict objectForKey:OPTIONS_SCREEN_TRACKING_ENABLED] boolValue]];
    [options setUserPrivacyConsentRequired:[[optionsDict objectForKey:OPTIONS_REQUIRES_USER_PRIVACY_CONSENT] boolValue]];

    [ICGIncognia initSdkWithOptions:options];
}

RCT_EXPORT_METHOD(setUser:(NSString *)userId) {
    [ICGIncognia setUserId:userId];
}

RCT_EXPORT_METHOD(clearUser) {
    [ICGIncognia clearUserId];
}

RCT_EXPORT_METHOD(requestPrivacyConsent:(NSDictionary *)dialogOptionsDict withConsentTypes:(NSArray<NSString *> *)consentTypes withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    ICGConsentDialogOptions *dialogOptions = [[ICGConsentDialogOptions alloc] init];
    if ([dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_TITLE]) {
        [dialogOptions setTitle:[dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_TITLE]];
    }
    if ([dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_MESSAGE]) {
        [dialogOptions setMessage:[dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_MESSAGE]];
    }
    if ([dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_ACCEPT_TEXT]) {
        [dialogOptions setAcceptText:[dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_ACCEPT_TEXT]];
    }
    if ([dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_DENY_TEXT]) {
        [dialogOptions setDenyText:[dialogOptionsDict objectForKey:OPTIONS_CONSENT_DIALOG_DENY_TEXT]];
    }
    [dialogOptions setConsentTypes:[NSSet setWithArray:consentTypes]];

    [ICGIncognia requestPrivacyConsentWithOptions:dialogOptions andConsentBlock:^(ICGConsentResult *result) {
        NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
        [dict setValue:@([result hasFinished]) forKey:@"hasFinished"];
        [dict setValue:@([result areAllConsentTypesGiven]) forKey:@"areAllConsentTypesGiven"];
        [dict setValue:@([result isWaitingConsent]) forKey:@"isWaitingConsent"];
        if (resolve) {
            resolve(dict);
        }
    }];
}

RCT_EXPORT_METHOD(giveUserPrivacyConsent:(BOOL)consentGiven) {
    //[ICGIncognia giveUserPrivacyConsent:consentGiven];
}

RCT_EXPORT_METHOD(giveUserPrivacyConsentForTypes:(NSArray<NSString *> *)consentTypes) {
    //[ICGIncognia giveUserPrivacyConsentForTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(setAllowedConsentTypes:(NSArray<NSString *> *)consentTypes) {
    //[ICGIncognia setAllowedConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(allowConsentTypes:(NSArray<NSString *> *)consentTypes) {
    //[ICGIncognia allowConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(denyConsentTypes:(NSArray<NSString *> *)consentTypes) {
    //[ICGIncognia denyConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(checkPrivacyConsentMissing:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
//    [ICGIncognia checkPrivacyConsentMissing:^(BOOL consentMissing) {
//        if (resolve) {
//            resolve([NSNumber numberWithBool:consentMissing]);
//        }
//    }];
    if (resolve) {
        resolve([NSNumber numberWithBool:YES]);
    }
}

RCT_EXPORT_METHOD(checkConsent:(NSArray<NSString *> *)consentTypes withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
//    [ICGIncognia checkConsentForTypes:[NSSet setWithArray:consentTypes]
//                          withBlock:^(ICGConsentResult *result) {
//                              NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
//                              [dict setValue:@([result hasFinished]) forKey:@"hasFinished"];
//                              [dict setValue:@([result areAllConsentTypesGiven]) forKey:@"areAllConsentTypesGiven"];
//                              [dict setValue:@([result isWaitingConsent]) forKey:@"isWaitingConsent"];
//                              if (resolve) {
//                                  resolve(dict);
//                              }
//                          }];
    if (resolve) {
        resolve(nil);
    }
}

RCT_EXPORT_METHOD(setUserAddress:(NSDictionary *)addressDict) {
    ICGUserAddress *userAddress = [self convertToUserAddress:addressDict];
    [ICGIncognia setUserAddress:userAddress];
}

RCT_EXPORT_METHOD(clearUserAddress) {
    [ICGIncognia clearUserAddress];
}

RCT_EXPORT_METHOD(setPushProvider:(NSString *)name andToken:(NSString *)token) {
//    ICGPushProvider *pushProvider = [[ICGPushProvider alloc] initWithName:name token:token];
//    [ICGIncogniaPush setPushProvider:pushProvider];
}

RCT_EXPORT_METHOD(setPushNotificationsEnabled:(BOOL)enabled) {
//    [ICGIncogniaPush setPushNotificationsEnabled:enabled];
}

RCT_EXPORT_METHOD(trackEvent:(NSString *)name properties:(NSDictionary *)properties) {
    [ICGIncognia trackEvent:name properties:properties];
}

RCT_EXPORT_METHOD(trackLocalizedEvent:(NSString *)name properties:(NSDictionary *)properties) {
    [ICGIncognia trackLocalizedEvent:name properties:properties];
}

RCT_EXPORT_METHOD(registerCheckIn:(NSString *)placeName placeId:(NSString *)placeId properties:(NSDictionary *)properties address:(NSDictionary *)addressDict) {
    ICGUserAddress *userAddress = [self convertToUserAddress:addressDict];
    ICGCheckIn *checkin = [[ICGCheckIn alloc] init];
    [checkin setPlaceName:placeName];
    [checkin setPlaceId:placeId];
    [checkin setUserAddress:userAddress];
    [checkin setExtras:properties];

    [ICGIncognia registerCheckIn:checkin];
}

//RCT_EXPORT_METHOD(didReceiveNotificationResponse:(NSDictionary *)userInfo) {
//    ICGPushMessage *message = [[ICGPushMessage alloc] initWithDictionary:userInfo];
//    [ICGIncogniaPush didReceiveNotificationResponse:message];
//}
//
//RCT_EXPORT_METHOD(didReceiveNotificationResponse:(NSDictionary *)userInfo withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
//    ICGPushMessage *message = [[ICGPushMessage alloc] initWithDictionary:userInfo];
//    [ICGIncogniaPush didReceiveNotificationResponse:message completionBlock:^{
//        resolve(nil);
//    }];
//}
//
//RCT_EXPORT_METHOD(didFinishLaunchingWithMessage:(NSDictionary *)userInfo) {
//    ICGPushMessage *message = [[ICGPushMessage alloc] initWithDictionary:userInfo];
//    [ICGIncogniaPush appDidFinishLaunchingWithMessage:message];
//}

RCT_EXPORT_METHOD(getInstallationId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [ICGIncognia getInstallationId:^(NSString *installationId) {
        if (resolve && installationId) {
            resolve(installationId);
        } else {
            reject(nil, @"InlocoException: Error while getting installation id", nil);
        }
    }];
}

RCT_EXPORT_METHOD(trackSignUp:(NSString *)signUpId address:(NSDictionary *)addressDict) {
    ICGUserAddress *userAddress = [self convertToUserAddress:addressDict];
    [ICGIncogniaDemo trackSignUp:signUpId address:userAddress];
}

RCT_EXPORT_METHOD(trackLogin:(NSString *)accountId) {
    [ICGIncogniaDemo trackLogin:accountId];
}

- (ICGUserAddress *)convertToUserAddress:(NSDictionary *)addressDict
{
    if (addressDict == nil) {
        return nil;
    }

    ICGUserAddress *userAddress = [[ICGUserAddress alloc] init];

    NSLocale *locale;
    if ([addressDict objectForKey:ADDRESS_LOCALE_KEY]) {
        locale = [[NSLocale alloc] initWithLocaleIdentifier:[addressDict objectForKey:ADDRESS_LOCALE_KEY]];
    } else {
        locale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US"];
    }
    [userAddress setLocale:locale];

    if ([addressDict objectForKey:ADDRESS_COUNTRY_NAME_KEY]) {
        [userAddress setCountryName:[addressDict objectForKey:ADDRESS_COUNTRY_NAME_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_COUNTRY_CODE_KEY]) {
        [userAddress setCountryCode:[addressDict objectForKey:ADDRESS_COUNTRY_CODE_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_ADMIN_AREA_KEY]) {
        [userAddress setAdminArea:[addressDict objectForKey:ADDRESS_ADMIN_AREA_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_SUBADMIN_AREA_KEY]) {
        [userAddress setSubAdminArea:[addressDict objectForKey:ADDRESS_SUBADMIN_AREA_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_LOCALITY_KEY]) {
        [userAddress setLocality:[addressDict objectForKey:ADDRESS_LOCALITY_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_SUB_LOCALITY_KEY]) {
        [userAddress setSubLocality:[addressDict objectForKey:ADDRESS_SUB_LOCALITY_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_THOROUGHFARE_KEY]) {
        [userAddress setThoroughfare:[addressDict objectForKey:ADDRESS_THOROUGHFARE_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_SUB_THOROUGHFARE_KEY]) {
        [userAddress setSubThoroughfare:[addressDict objectForKey:ADDRESS_SUB_THOROUGHFARE_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_POSTAL_CODE_KEY]) {
        [userAddress setPostalCode:[addressDict objectForKey:ADDRESS_POSTAL_CODE_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_LATITUDE_KEY]) {
        [userAddress setLatitude:[addressDict objectForKey:ADDRESS_LATITUDE_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_LONGITUDE_KEY]) {
        [userAddress setLongitude:[addressDict objectForKey:ADDRESS_LONGITUDE_KEY]];
    }
    if ([addressDict objectForKey:ADDRESS_LINE_KEY]) {
        [userAddress setAddressLine:[addressDict objectForKey:ADDRESS_LINE_KEY]];
    }

    return userAddress;
}

@end
