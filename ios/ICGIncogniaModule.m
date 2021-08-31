#import "ICGIncogniaModule.h"
#import "RCTConvert.h"

@import IncogniaBR;
@import IncogniaTrialBR;

#define OPTIONS_CONSENT_DIALOG_TITLE       @"dialogTitle"
#define OPTIONS_CONSENT_DIALOG_MESSAGE     @"dialogMessage"
#define OPTIONS_CONSENT_DIALOG_ACCEPT_TEXT @"dialogAcceptText"
#define OPTIONS_CONSENT_DIALOG_DENY_TEXT   @"dialogDenyText"
#define OPTIONS_CONSENT_TYPES              @"consentTypes"

#define EVENT_NAME                         @"eventName"
#define EVENT_PROPERTIES                   @"eventProperties"

#define SIGNUP_ID                          @"signupId"
#define SIGNUP_ADDRESS                     @"signupAddress"
#define SIGNUP_PROPERTIES                  @"properties"

#define LOGIN_ID                           @"loginId"
#define LOGIN_ACCOUNT_ID                   @"accountId"
#define LOGIN_PROPERTIES                   @"properties"

#define TRANSACTION_ID                     @"transactionId"
#define TRANSACTION_ADDRESSES              @"transactionAddresses"
#define TRANSACTION_PROPERTIES             @"properties"

#define ADDRESS_TYPE_BILLING               @"billing"
#define ADDRESS_TYPE_SHIPPING              @"shipping"
#define ADDRESS_TYPE_HOME                  @"home"

#define ADDRESS_LOCALE_KEY                 @"locale"
#define ADDRESS_COUNTRY_NAME_KEY           @"countryName"
#define ADDRESS_COUNTRY_CODE_KEY           @"countryCode"
#define ADDRESS_ADMIN_AREA_KEY             @"adminArea"
#define ADDRESS_SUBADMIN_AREA_KEY          @"subAdminArea"
#define ADDRESS_LOCALITY_KEY               @"locality"
#define ADDRESS_SUB_LOCALITY_KEY           @"subLocality"
#define ADDRESS_THOROUGHFARE_KEY           @"thoroughfare"
#define ADDRESS_SUB_THOROUGHFARE_KEY       @"subThoroughfare"
#define ADDRESS_POSTAL_CODE_KEY            @"postalCode"
#define ADDRESS_LATITUDE_KEY               @"latitude"
#define ADDRESS_LONGITUDE_KEY              @"longitude"
#define ADDRESS_LINE_KEY                   @"addressLine"

#define TRANSACTION_ADDRESS_TYPE           @"type"

@implementation ICGIncogniaModule

RCT_EXPORT_MODULE(IncogniaModule)

RCT_EXPORT_METHOD(setAccountId:(NSString *)accountId) {
    [ICGIncognia setAccountId:accountId];
}

RCT_EXPORT_METHOD(clearAccountId) {
    [ICGIncognia clearAccountId];
}

RCT_EXPORT_METHOD(allowConsentTypes:(NSArray<NSString *> *)consentTypes) {
    [ICGIncognia allowConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(denyConsentTypes:(NSArray<NSString *> *)consentTypes) {
    [ICGIncognia denyConsentTypes:[NSSet setWithArray:consentTypes]];
}

RCT_EXPORT_METHOD(setLocationEnabled:(BOOL)enabled) {
    [ICGIncognia setLocationEnabled:enabled];
}

RCT_EXPORT_METHOD(fetchInstallationId:(RCTPromiseResolveBlock)resolve withRejecter:(RCTPromiseRejectBlock)reject) {
    [ICGIncognia fetchInstallationId:^(NSString *installationId) {
        if (resolve && installationId) {
            resolve(installationId);
        } else {
            reject(nil, @"Incognia: error while fetching installation id", nil);
        }
    }];
}

RCT_EXPORT_METHOD(requestPrivacyConsent:(NSDictionary *)parameters withResolver:(RCTPromiseResolveBlock)resolve withRejecter:(RCTPromiseRejectBlock)reject) {
    ICGConsentDialogOptions *dialogOptions = [[ICGConsentDialogOptions alloc] init];

    if ([parameters objectForKey:OPTIONS_CONSENT_DIALOG_TITLE]) {
        [dialogOptions setTitle:[parameters objectForKey:OPTIONS_CONSENT_DIALOG_TITLE]];
    }

    if ([parameters objectForKey:OPTIONS_CONSENT_DIALOG_MESSAGE]) {
        [dialogOptions setMessage:[parameters objectForKey:OPTIONS_CONSENT_DIALOG_MESSAGE]];
    }

    if ([parameters objectForKey:OPTIONS_CONSENT_DIALOG_ACCEPT_TEXT]) {
        [dialogOptions setAcceptText:[parameters objectForKey:OPTIONS_CONSENT_DIALOG_ACCEPT_TEXT]];
    }

    if ([parameters objectForKey:OPTIONS_CONSENT_DIALOG_DENY_TEXT]) {
        [dialogOptions setDenyText:[parameters objectForKey:OPTIONS_CONSENT_DIALOG_DENY_TEXT]];
    }

    if ([parameters objectForKey:OPTIONS_CONSENT_TYPES]) {
        [dialogOptions setConsentTypes:[NSSet setWithArray:[parameters objectForKey:OPTIONS_CONSENT_TYPES]]];
    }

    [ICGIncognia requestPrivacyConsentWithOptions:dialogOptions completionBlock:^(ICGConsentResult *_Nullable result) {
        NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
        [dict setValue:@([result hasFinished]) forKey:@"hasFinished"];
        [dict setValue:@([result areAllConsentTypesGiven]) forKey:@"areAllConsentTypesGiven"];
        [dict setValue:@([result isWaitingConsent]) forKey:@"isWaitingConsent"];

        if (resolve) {
            resolve(dict);
        }
    }];
}

RCT_EXPORT_METHOD(checkConsent:(NSArray<NSString *> *)consentTypes withPromise:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
    [ICGIncognia checkConsentForTypes:[NSSet setWithArray:consentTypes] completionBlock:^(ICGConsentResult *_Nullable result) {
        NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
        [dict setValue:@([result hasFinished]) forKey:@"hasFinished"];
        [dict setValue:@([result areAllConsentTypesGiven]) forKey:@"areAllConsentTypesGiven"];
        [dict setValue:@([result isWaitingConsent]) forKey:@"isWaitingConsent"];

        if (resolve) {
            resolve(dict);
        }
    }];
}

RCT_EXPORT_METHOD(trackEvent:(NSDictionary *)parameters) {
    NSString *eventName = [parameters objectForKey:EVENT_NAME];
    NSDictionary *eventProperties = [parameters objectForKey:EVENT_PROPERTIES];

    [ICGIncognia trackEvent:eventName properties:[self convertToEventProperties:eventProperties]];
}

RCT_EXPORT_METHOD(trackLocalizedEvent:(NSDictionary *)parameters) {
    NSString *eventName = [parameters objectForKey:EVENT_NAME];
    NSDictionary *eventProperties = [parameters objectForKey:EVENT_PROPERTIES];

    [ICGIncognia trackLocalizedEvent:eventName properties:[self convertToEventProperties:eventProperties]];
}

RCT_EXPORT_METHOD(trackSignupSent:(NSDictionary *)parameters) {
    NSString *signupId = [parameters objectForKey:SIGNUP_ID];
    ICGUserAddress *userAddress = [self convertToUserAddress:[parameters objectForKey:SIGNUP_ADDRESS]];
    ICGEventProperties *properties = [self convertToEventProperties:[parameters objectForKey:SIGNUP_PROPERTIES]];

    [ICGIncogniaTrial trackSignupSentWithId:signupId eventProperties:properties address:userAddress];
}

RCT_EXPORT_METHOD(trackLoginSucceeded:(NSDictionary *)parameters) {
    NSString *loginId = [parameters objectForKey:LOGIN_ID];
    NSString *accountId = [parameters objectForKey:LOGIN_ACCOUNT_ID];
    ICGEventProperties *properties = [self convertToEventProperties:[parameters objectForKey:LOGIN_PROPERTIES]];

    [ICGIncogniaTrial trackLoginSucceededWithId:loginId accountId:accountId eventProperties:properties];
}

RCT_EXPORT_METHOD(trackPaymentSent:(NSDictionary *)parameters) {
    NSString *transactionId = [parameters objectForKey:TRANSACTION_ID];
    ICGEventProperties *properties = [self convertToEventProperties:[parameters objectForKey:TRANSACTION_PROPERTIES]];
    NSArray<NSDictionary *> *transactionAddressesArr = [parameters objectForKey:TRANSACTION_ADDRESSES];

    NSMutableSet<ICGTransactionAddress *> *transactionAddresses = [[NSMutableSet alloc] init];

    for (NSDictionary *transactionAddressDict in transactionAddressesArr) {
        ICGTransactionAddress *transactionAddress = [self convertToTransactionAddress:transactionAddressDict];

        if (transactionAddress != nil) {
            [transactionAddresses addObject:transactionAddress];
        }
    }

    [ICGIncogniaTrial trackPaymentSentWithId:transactionId eventProperties:properties transactionAddresses:transactionAddresses];
}

- (NSDictionary *)constantsToExport {
    NSMutableDictionary *constants = [[NSMutableDictionary alloc] init];

    constants[@"CONSENT_TYPE_ADDRESS_VALIDATION"] = ICG_CONSENT_TYPE_ADDRESS_VALIDATION;
    constants[@"CONSENT_TYPE_ADVERTISEMENT"] = ICG_CONSENT_TYPE_ADVERTISEMENT;
    constants[@"CONSENT_TYPE_ENGAGE"] = ICG_CONSENT_TYPE_ENGAGE;
    constants[@"CONSENT_TYPE_EVENTS"] = ICG_CONSENT_TYPE_EVENTS;
    constants[@"CONSENT_TYPE_LOCATION"] = ICG_CONSENT_TYPE_LOCATION;
    constants[@"CONSENT_TYPE_CONTEXT_PROVIDER"] = ICG_CONSENT_TYPE_CONTEXT_PROVIDER;
    constants[@"CONSENT_TYPES_ALL"] = [ICG_CONSENT_SET_ALL allObjects];
    constants[@"CONSENT_TYPES_NONE"] = [[NSArray alloc] init];

    return constants;
}

- (void)fillAddress:(ICGUserAddress *)userAddress addressDict:(NSDictionary *)addressDict {
    if (addressDict == nil) {
        return;
    }

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
        [userAddress setSubThoroughfare:[RCTConvert NSString:[addressDict objectForKey:ADDRESS_SUB_THOROUGHFARE_KEY]]];
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
}

- (ICGUserAddress *)convertToUserAddress:(NSDictionary *)addressDict {
    if (addressDict == nil) {
        return nil;
    }

    ICGUserAddress *userAddress = [[ICGUserAddress alloc] init];

    [self fillAddress:userAddress addressDict:addressDict];

    return userAddress;
}

- (ICGTransactionAddress *)convertToTransactionAddress:(NSDictionary *)addressDict {
    if (addressDict == nil) {
        return nil;
    }

    NSString *transactionAddressType = [addressDict objectForKey:TRANSACTION_ADDRESS_TYPE] != nil ? [addressDict objectForKey:TRANSACTION_ADDRESS_TYPE] : nil;
    ICGTransactionAddress *transactionAddress = [[ICGTransactionAddress alloc] initWithType:transactionAddressType];

    [self fillAddress:transactionAddress addressDict:addressDict];

    return transactionAddress;
}

- (ICGEventProperties *)convertToEventProperties:(NSDictionary *)dict {
    ICGEventProperties *eventProperties;

    if (dict.count > 0) {
        eventProperties = [[ICGEventProperties alloc] init];
        [dict enumerateKeysAndObjectsUsingBlock:^(id _Nonnull key, id _Nonnull obj, BOOL *_Nonnull stop) {
            if ([obj isKindOfClass:NSString.class]) {
                [eventProperties putString:(NSString *)obj forKey:key];
            } else if ([obj isKindOfClass:NSNumber.class]) {
                NSNumber *numberValue = (NSNumber *)obj;

                if ([self isBoolNumber:numberValue]) {
                    [eventProperties putBool:numberValue.boolValue forKey:key];
                } else {
                    [eventProperties putNumber:numberValue forKey:key];
                }
            }
        }];
    }

    return eventProperties;
}

- (BOOL)isBoolNumber:(NSNumber *)num {
    CFTypeID boolID = CFBooleanGetTypeID(); // the type ID of CFBoolean
    CFTypeID numID = CFGetTypeID((__bridge CFTypeRef)(num)); // the type ID of num

    return numID == boolID;
}

@end
