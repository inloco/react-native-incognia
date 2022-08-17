//
//  ICGCheckIn.h
//  Incognia-iOS-SDK-Location
//
//  Created by Douglas Soares on 16/03/20.
//  Copyright © 2020 Incognia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ICGUserAddress.h"

@interface ICGCheckIn : NSObject

@property (nonatomic, strong, nullable) NSString *placeName;
@property (nonatomic, strong, nullable) NSString *placeId;
@property (nonatomic, strong, nullable) ICGUserAddress *userAddress;
@property (nonatomic, strong, nullable) NSDictionary <NSString *, NSString *> *extras;

@end
