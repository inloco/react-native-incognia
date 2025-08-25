#import "ICGIncogniaModule.h"
#import "RCTConvert.h"

@import IncogniaBR;
@import IncogniaTrialBR;

#define EVENT_ACCOUNT_ID                   @"accountId"
#define EVENT_EXTERNAL_ID                  @"external_id"
#define EVENT_REACT_PROPERTIES             @"reactProperties"

@implementation ICGIncogniaModule

RCT_EXPORT_MODULE(IncogniaModule)

RCT_EXPORT_METHOD(setAccountId:(NSString *)accountId) {
    [ICGIncognia setAccountId:accountId];
}

RCT_EXPORT_METHOD(clearAccountId) {
    [ICGIncognia clearAccountId];
}

RCT_EXPORT_METHOD(generateRequestToken:(RCTPromiseResolveBlock)resolve withRejecter:(RCTPromiseRejectBlock)reject) {
    [ICGIncognia generateUniqueRequestToken:^(NSString *requestToken) {
        if (resolve && requestToken) {
            resolve(requestToken);
        } else {
            reject(nil, @"Incognia: error while generating a unique request token", nil);
        }
    }];
}

RCT_EXPORT_METHOD(setLocationEnabled:(BOOL)enabled) {
    [ICGIncognia setLocationEnabled:enabled];
}

RCT_EXPORT_METHOD(trackLocalizedEvent:(NSDictionary *)parameters) {
    NSString *eventName = @"ReactNative7CustomEvent";
    NSDictionary *eventProperties = [self objectOrNilForKey:parameters key:EVENT_REACT_PROPERTIES];

    [ICGIncognia trackLocalizedEvent:eventName properties:[self convertToEventProperties:eventProperties]];
}

RCT_EXPORT_METHOD(trackSignupSent:(NSDictionary *)parameters) {
    NSString *signupId = [self objectOrNilForKey:parameters key:EVENT_EXTERNAL_ID];
    ICGEventProperties *properties = [self convertToEventProperties:[self objectOrNilForKey:parameters key:EVENT_REACT_PROPERTIES]];

    [ICGIncogniaTrial trackSignupSentWithId:signupId eventProperties:properties address:nil];
}

RCT_EXPORT_METHOD(trackLoginSucceeded:(NSDictionary *)parameters) {
    NSString *loginId = [self objectOrNilForKey:parameters key:EVENT_EXTERNAL_ID];
    NSString *accountId = [self objectOrNilForKey:parameters key:EVENT_ACCOUNT_ID];
    ICGEventProperties *properties = [self convertToEventProperties:[self objectOrNilForKey:parameters key:EVENT_REACT_PROPERTIES]];

    [ICGIncogniaTrial trackLoginSucceededWithId:loginId accountId:accountId eventProperties:properties];
}

RCT_EXPORT_METHOD(trackPaymentSent:(NSDictionary *)parameters) {
    NSString *transactionId = [self objectOrNilForKey:parameters key:EVENT_EXTERNAL_ID];
    ICGEventProperties *properties = [self convertToEventProperties:[self objectOrNilForKey:parameters key:EVENT_REACT_PROPERTIES]];

    [ICGIncogniaTrial trackPaymentSentWithId:transactionId eventProperties:properties transactionAddresses:nil];
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (ICGEventProperties *)convertToEventProperties:(NSDictionary *)dict {
    ICGEventProperties *eventProperties;

    if (dict.count > 0) {
        eventProperties = [[ICGEventProperties alloc] init];
        [dict enumerateKeysAndObjectsUsingBlock:^(id _Nonnull key, id _Nonnull obj, BOOL *_Nonnull stop) {
            [eventProperties putString:(NSString *)obj forKey:key];
        }];
    }

    return eventProperties;
}

- (BOOL)isBoolNumber:(NSNumber *)num {
    CFTypeID boolID = CFBooleanGetTypeID(); // the type ID of CFBoolean
    CFTypeID numID = CFGetTypeID((__bridge CFTypeRef)(num)); // the type ID of num

    return numID == boolID;
}

- (id)objectOrNilForKey:(NSDictionary *) dict key:(id)key {
    id object = [dict objectForKey:key];
    if (object == [NSNull null]) {
        return nil;
    }
    return object;
}

@end
