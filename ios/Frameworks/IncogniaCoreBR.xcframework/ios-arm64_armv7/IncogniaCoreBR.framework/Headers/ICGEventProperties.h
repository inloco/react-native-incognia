//
//  ICGEventProperties.h
//  IncogniaSDK
//
//  Created by Pedro Atanasio on 09/02/21.
//  Copyright © 2021 Incognia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ICGEventProperties : NSObject

- (void)putString:(NSString *)value forKey:(NSString *)key;
- (void)putNumber:(NSNumber *)value forKey:(NSString *)key;
- (void)putInt:(int)value forKey:(NSString *)key;
- (void)putLong:(long)value forKey:(NSString *)key;
- (void)putDouble:(double)value forKey:(NSString *)key;
- (void)putBool:(BOOL)value forKey:(NSString *)key;
- (void)remove:(NSString *)key;
- (NSDictionary *)toDictionary;

@end

NS_ASSUME_NONNULL_END
