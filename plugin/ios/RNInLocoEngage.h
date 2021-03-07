#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#if __has_include(<IncogniaCore/IncogniaCore.h>) // from Pod
#import <IncogniaCore/IncogniaCore.h>
#else
#import "ICGError.h"
#import "ICGOptions.h"
#import "ICGConsentTypes.h"
#import "ICGConsentResult.h"
#import "ICGConsentDialogOptions.h"
#import "ICGUserAddress.h"
#import "ICGTransactionAddress.h"
#import "ICGCheckIn.h"
#endif

#if __has_include(<Incognia/Incognia.h>) // from Pod
#import <Incognia/Incognia.h>
#else
#import "ICGIncognia.h"
#import "ICGIncogniaDemo.h"
#import "ICGError.h"
#import "ICGUserAddress.h"
#import "ICGTransactionAddress.h"
#import "ICGConsentTypes.h"
#endif

@interface RNInLocoEngage: NSObject <RCTBridgeModule>

@end
  
