//
//  ICGIncognia.h
//  Incognia
//
//  Created by Pedro Atanasio on 05/06/20.
//  Copyright © 2020 Incognia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ICGConsentResult.h"
#import "ICGOptions.h"
#import "ICGConsentDialogOptions.h"
#import "ICGUserAddress.h"
#import "ICGCheckIn.h"
#import "ICGEventProperties.h"

typedef void (^ICGFetchResultBlock)(UIBackgroundFetchResult);
typedef void (^ICGConsentBlock)(ICGConsentResult *_Nullable);
typedef void (^ICGStringBlock)(NSString *_Nullable);

@interface ICGIncognia : NSObject

/**
   Initializes the Incognia SDK with the options parameters.
 */
+ (void)initSdkWithOptions:(nonnull ICGOptions *)options;

/**
   Initializes the Incognia SDK with the IncogniaOptions.plist properties file. To know more, please refer to the Incognia documentation.
 */
+ (void)initSdk;

/**
   Allows background operations.

   Should be called inside [-application:performFetchWithCompletionHandler:].
 */
+ (void)performFetchWithCompletionBlock:(nullable ICGFetchResultBlock)fetchResultBlock;

/**
   Allows consents for the functionality types specified in consentTypes parametes (using ICG_CONSENT_TYPE_. constants).
   A call to this method does not modify other consent types not present in consentTypes.
 */
+ (void)allowConsentTypes:(nonnull NSSet<NSString *> *)consentTypes;

/**
   Denies consents for the functionality types specified in consentTypes parametes (using ICG_CONSENT_TYPE_. constants).
   A call to this method does not modify other consent types not present in consentTypes.
 */
+ (void)denyConsentTypes:(nonnull NSSet<NSString *> *)consentTypes;

/**
   Presents to the user a Dialog for consent request. If the user allows it, it will give the privacy consent for the consentTypes
   present on the given ICGConsentDialogOptions. Otherwise, it will deny consent for the given consentTypes.

   Returns through a block a NSDictionary containing the consent status for each consentType present on the consentTypes field ot the
   given ICGConsentDialogOptions.
 */
+ (void)requestPrivacyConsentWithOptions:(nonnull ICGConsentDialogOptions *)options completionBlock:(nullable ICGConsentBlock)block;

/**
   Presents to the user a Dialog for consent request. If the user allows it, it will give the privacy consent for the consentTypes
   present on the given ICGConsentDialogOptions. Otherwise, it will deny consent for the given consentTypes.
 */
+ (void)requestPrivacyConsentWithOptions:(nonnull ICGConsentDialogOptions *)options;

/**
   Returns through a block a NSDictionary containing the consent status for each consentType present on the given set.
 */
+ (void)checkConsentForTypes:(nonnull NSSet<NSString *> *)consentTypes completionBlock:(nullable ICGConsentBlock)block;

/**
   Asynchronously retrieves the current installation id.
 */
+ (void)fetchInstallationId:(nonnull ICGStringBlock)block;

/**
   Synchronously retrieves the current installation id. This method may return nil if the SDK has not yet been initialized or if an error occurs on initialization.
 */
+ (nullable NSString *)installationId;

/**
   Sets the current account id.
   This value is persisted.
 */
+ (void)setAccountId:(nonnull NSString *)accountId;

/**
   Clears the current persisted account id.
 */
+ (void)clearAccountId;

+ (void)setLocationEnabled:(BOOL)enabled;

/**
   Records an in-app event.
 */
+ (void)trackEvent:(nonnull NSString *)eventName;
+ (void)trackEvent:(nonnull NSString *)eventName properties:(nullable ICGEventProperties *)properties;

/**
   Records an in-app event  and its location.
 */
+ (void)trackLocalizedEvent:(nonnull NSString *)eventName;
+ (void)trackLocalizedEvent:(nonnull NSString *)eventName properties:(nullable ICGEventProperties *)properties;

/**
   Sets the user address.
   This value is persisted locally.
 */
+ (void)setUserAddress:(ICGUserAddress *)userAddress __deprecated_msg("This method is deprecated. Setting addresses should be done via Incognia's API. For further explanation, refer to our documentation.");

/**
   Clears the current persisted user address.
 */
+ (void)clearUserAddress __deprecated_msg("This method is deprecated. Setting addresses should be done via Incognia's API. For further explanation, refer to our documentation");

@end
