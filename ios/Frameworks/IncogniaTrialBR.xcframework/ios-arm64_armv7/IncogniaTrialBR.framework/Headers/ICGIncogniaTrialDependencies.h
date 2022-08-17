#if __has_include(<IncogniaCore/IncogniaCore.h>)
#import <IncogniaCore/IncogniaCore.h>
#elif __has_include(<IncogniaCoreBR/IncogniaCoreBR.h>)
#import <IncogniaCoreBR/IncogniaCoreBR.h>
#else
#import "ICGError.h"
#import "ICGOptions.h"
#import "ICGConsentTypes.h"
#import "ICGConsentResult.h"
#import "ICGConsentDialogOptions.h"
#import "ICGUserAddress.h"
#import "ICGCheckIn.h"
#import "ICGEventProperties.h"
#import "ICGTransactionAddress.h"
#endif

#if __has_include(<Incognia/Incognia.h>)
#import <Incognia/Incognia.h>
#elif __has_include(<IncogniaBR/IncogniaBR.h>)
#import <IncogniaBR/IncogniaBR.h>
#else
#import "ICGIncogniaConsentTypes.h"
#endif
