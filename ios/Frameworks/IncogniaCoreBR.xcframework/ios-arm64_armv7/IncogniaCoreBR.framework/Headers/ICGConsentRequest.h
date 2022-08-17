//
//  ICGConsentRequest.h
//  Incognia-iOS-SDK-Common
//
//  Created by Júlia Godoy on 06/04/20.
//  Copyright © 2020 Incognia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ICGConsentDialogOptions.h"
#import "ICGIncogniaInternal.h"

NS_ASSUME_NONNULL_BEGIN

@interface ICGConsentRequest : NSObject

- (void)showDialogWithOptions:(ICGConsentDialogOptions *)options andConsentBlock:(nullable ICGConsentBlock)block;

@end

NS_ASSUME_NONNULL_END
