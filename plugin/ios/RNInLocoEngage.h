
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#if __has_include(<InLocoSDK/InLoco.h>) // from Pod
#import <InLocoSDK/InLoco.h>
#else
#import "ILMInLoco.h"
#import "ILMInLocoEvents.h"
#import "ILMInLocoDemo.h"
#endif

#if __has_include(<InLocoSDKEngage/InLocoEngage.h>) // from Pod
#import <InLocoSDKEngage/InLocoEngage.h>
#else
#import "ILMInLocoPush.h"
#import "ILMInLocoAddressValidation.h"
#endif

#if __has_include(<InLocoSDKLocation/InLocoLocation.h>) // from Pod
#import <InLocoSDKLocation/InLocoLocation.h>
#else
#import "ILMInLocoVisits.h"
#endif

@interface RNInLocoEngage : NSObject <RCTBridgeModule>

@end
  
