//
//  ICGTransactionAddress.h
//  Incognia-iOS-SDK-Inloco
//
//  Created by Lucas Cardoso on 04/02/21.
//  Copyright © 2021 Incognia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ICGUserAddress.h"

NS_ASSUME_NONNULL_BEGIN

@interface ICGTransactionAddress : ICGUserAddress

@property (nonatomic, strong) NSString *type;

- (instancetype)initWithType:(NSString *)type;

+ (ICGTransactionAddress *)makeBillingAddress;

+ (ICGTransactionAddress *)makeShippingAddress;

+ (ICGTransactionAddress *)makeHomeAddress;

@end

NS_ASSUME_NONNULL_END
