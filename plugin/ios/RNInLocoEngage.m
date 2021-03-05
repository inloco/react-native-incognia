
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
    [ILMInLoco initSdk];
}

RCT_EXPORT_METHOD(initSdkWithOptions:(NSDictionary *)optionsDict) {
    ILMOptions *options = [[ILMOptions alloc] init];
    [options setApplicationId:[optionsDict objectForKey:OPTIONS_APP_ID]];
    [options setLogEnabled:[[optionsDict objectForKey:OPTIONS_LOGS_ENABLED] boolValue]];
    [options setDevelopmentDevices:[optionsDict objectForKey:OPTIONS_DEVELOPMENT_DEVICES]];
    [options setLocationEnabled:[[optionsDict objectForKey:OPTIONS_VISITS_ENABLED] boolValue]];
    [options setScreenTrackingEnabled:[[optionsDict objectForKey:OPTIONS_SCREEN_TRACKING_ENABLED] boolValue]];
    [options setUserPrivacyConsentRequired:[[optionsDict objectForKey:OPTIONS_REQUIRES_USER_PRIVACY_CONSENT] boolValue]];

    [ILMInLoco initSdkWithOptions:options];
}

RCT_EXPORT_METHOD(setUser:(NSString *)userId) {
    [ILMInLoco setUserId:userId];
}

RCT_EXPORT_METHOD(clearUser) {
    [ILMInLoco clearUserId];
}

RCT_EXPORT_METHOD(requestPrivacyConsent:(NSDictionary *)dialogOptionsDict withConsentTypes:(NSArray<NSString *> *)consentTypes withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    ILMConsentDialogOptions *dialogOptions = [[ILMConsentDialogOptions alloc] init];
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

    [ILMInLoco requestPrivacyConsentWithOptions:dialogOptions andConsentBlock:^(ILMConsentResult *result) {
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
    [ILMInLoco giveUserPrivacyConsent:consentGiven];
}

RCT_EXPORT_METHOD(giveUserPrivacyConsentForTypes:(NSArray<NSString *> *)consentTypes) {
    [ILMInLoco giveUserPrivacyConsentForTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(setAllowedConsentTypes:(NSArray<NSString *> *)consentTypes) {
    [ILMInLoco setAllowedConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(allowConsentTypes:(NSArray<NSString *> *)consentTypes) {
    [ILMInLoco allowConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(denyConsentTypes:(NSArray<NSString *> *)consentTypes) {
    [ILMInLoco denyConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(checkPrivacyConsentMissing:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [ILMInLoco checkPrivacyConsentMissing:^(BOOL consentMissing) {
        if (resolve) {
            resolve([NSNumber numberWithBool:consentMissing]);
        }
    }];
}

RCT_EXPORT_METHOD(checkConsent:(NSArray<NSString *> *)consentTypes withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [ILMInLoco checkConsentForTypes:[NSSet setWithArray:consentTypes]
                          withBlock:^(ILMConsentResult *result) {
                              NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
                              [dict setValue:@([result hasFinished]) forKey:@"hasFinished"];
                              [dict setValue:@([result areAllConsentTypesGiven]) forKey:@"areAllConsentTypesGiven"];
                              [dict setValue:@([result isWaitingConsent]) forKey:@"isWaitingConsent"];
                              if (resolve) {
                                  resolve(dict);
                              }
                          }];
}

RCT_EXPORT_METHOD(setUserAddress:(NSDictionary *)addressDict) {
    ILMUserAddress *userAddress = [self convertToUserAddress:addressDict];
    [ILMInLocoAddressValidation setUserAddress:userAddress];
}

RCT_EXPORT_METHOD(clearUserAddress) {
    [ILMInLocoAddressValidation clearUserAddress];
}

RCT_EXPORT_METHOD(setPushProvider:(NSString *)name andToken:(NSString *)token) {
    ILMPushProvider *pushProvider = [[ILMPushProvider alloc] initWithName:name token:token];
    [ILMInLocoPush setPushProvider:pushProvider];
}

RCT_EXPORT_METHOD(setPushNotificationsEnabled:(BOOL)enabled) {
    [ILMInLocoPush setPushNotificationsEnabled:enabled];
}

RCT_EXPORT_METHOD(trackEvent:(NSString *)name properties:(NSDictionary *)properties) {
    [ILMInLocoEvents trackEvent:name properties:properties];
}

RCT_EXPORT_METHOD(trackLocalizedEvent:(NSString *)name properties:(NSDictionary *)properties) {
    [ILMInLocoVisits trackLocalizedEvent:name properties:properties];
}

RCT_EXPORT_METHOD(registerCheckIn:(NSString *)placeName placeId:(NSString *)placeId properties:(NSDictionary *)properties address:(NSDictionary *)addressDict) {
    ILMUserAddress *userAddress = [self convertToUserAddress:addressDict];
    ILMCheckIn *checkin = [[ILMCheckIn alloc] init];
    [checkin setPlaceName:placeName];
    [checkin setPlaceId:placeId];
    [checkin setUserAddress:userAddress];
    [checkin setExtras:properties];

    [ILMInLocoVisits registerCheckIn:checkin];
}

RCT_EXPORT_METHOD(didReceiveNotificationResponse:(NSDictionary *)userInfo) {
    ILMPushMessage *message = [[ILMPushMessage alloc] initWithDictionary:userInfo];
    [ILMInLocoPush didReceiveNotificationResponse:message];
}

RCT_EXPORT_METHOD(didReceiveNotificationResponse:(NSDictionary *)userInfo withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    ILMPushMessage *message = [[ILMPushMessage alloc] initWithDictionary:userInfo];
    [ILMInLocoPush didReceiveNotificationResponse:message completionBlock:^{
        resolve(nil);
    }];
}

RCT_EXPORT_METHOD(didFinishLaunchingWithMessage:(NSDictionary *)userInfo) {
    ILMPushMessage *message = [[ILMPushMessage alloc] initWithDictionary:userInfo];
    [ILMInLocoPush appDidFinishLaunchingWithMessage:message];
}

RCT_EXPORT_METHOD(getInstallationId:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [ILMInLoco getInstallationId:^(NSString *installationId) {
        if (resolve && installationId) {
            resolve(installationId);
        } else {
            reject(nil, @"InlocoException: Error while getting installation id", nil);
        }
    }];
}

RCT_EXPORT_METHOD(trackSignUp:(NSString *)signUpId address:(NSDictionary *)addressDict) {
    ILMUserAddress *userAddress = [self convertToUserAddress:addressDict];
    [ILMInLocoDemo trackSignUp:signUpId address:userAddress];
}

RCT_EXPORT_METHOD(trackLogin:(NSString *)accountId) {
    [ILMInLocoDemo trackLogin:accountId];
}

- (ILMUserAddress *)convertToUserAddress:(NSDictionary *)addressDict
{
    if (addressDict == nil) {
        return nil;
    }

    ILMUserAddress *userAddress = [[ILMUserAddress alloc] init];

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
