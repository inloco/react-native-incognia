//
//  ICGIncogniaTrial.h
//  Incognia
//
//  Created by Douglas Soares on 01/12/20.
//  Copyright © 2020 Incognia. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ICGIncogniaTrialDependencies.h"

@interface ICGIncogniaTrial : NSObject

/**
 * Records a signup without any other information.
 */
+ (void)trackSignupSent;

/**
 * Records a signup with its associated id.
 */
+ (void)trackSignupSentWithId:(nullable NSString *)signupId;

/**
 * Records a signup with its associated id and address.
 */
+ (void)trackSignupSentWithId:(nullable NSString *)signupId address:(nullable ICGUserAddress *)userAddress;

/**
 * Records a signup with its associated address.
 */
+ (void)trackSignupSentWithAddress:(nullable ICGUserAddress *)userAddress;

/**
 * Records a signup with its associated id, address and custom event properties that may help our analysis.
 */
+ (void)trackSignupSentWithId:(nullable NSString *)signupId eventProperties:(nullable ICGEventProperties *)eventProperties address:(nullable ICGUserAddress *)userAddress;

/**
 * Records a login with its associated id and the accountId that belongs to the authenticated user.
 */
+ (void)trackLoginSucceededWithId:(nullable NSString *)loginId accountId:(nonnull NSString *)accountId;

/**
 * Records a login with the accountId that belongs to the authenticated user.
 */
+ (void)trackLoginSucceededWithAccountId:(nonnull NSString *)accountId;

/**
 * Records a login with its associated id, the accountId that belongs to the authenticated user and custom event properties that may help our analysis.
 */
+ (void)trackLoginSucceededWithId:(nullable NSString *)loginId accountId:(nonnull NSString *)accountId eventProperties:(nullable ICGEventProperties *)eventProperties;

/**
 * Records a payment with its associated id, the accountId that belongs to the user performing the action and a set of associated addresses.
 */
+ (void)trackPaymentSentWithId:(nullable NSString *)transactionId
          transactionAddresses:(nullable NSSet<ICGTransactionAddress *> *)addresses;

/**
 * Records a payment with the accountId that belongs to the user performing the action and a set of associated addresses.
 */
+ (void)trackPaymentSentWithTransactionAddresses:(nullable NSSet<ICGTransactionAddress *> *)addresses;

/**
 * Records a payment with the accountId that belongs to the user performing the action.
 */
+ (void)trackPaymentSent;

/**
 * Records a payment with its associated id, the accountId that belongs to the user performing the action.
 */
+ (void)trackPaymentSentWithId:(nullable NSString *)transactionId;

/**
 * Records a payment with its associated id, the accountId that belongs to the user performing the action, a set of associated addresses and custom event properties that may help our analysis.
 */
+ (void)trackPaymentSentWithId:(nullable NSString *)transactionId
               eventProperties:(nullable ICGEventProperties *)eventProperties
          transactionAddresses:(nullable NSSet<ICGTransactionAddress *> *)addresses;

@end
