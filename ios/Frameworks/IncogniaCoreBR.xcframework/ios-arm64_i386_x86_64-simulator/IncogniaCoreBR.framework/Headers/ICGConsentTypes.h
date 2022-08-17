//
//  ICGConsentTypes.h
//  Incognia-iOS-SDK-Common
//
//  Created by Larissa Passos on 08/07/19.
//  Copyright © 2019 Incognia. All rights reserved.
//

#ifndef ICGConsentTypes_h
#define ICGConsentTypes_h

// Consent Types
#define ICG_CONSENT_TYPE_ADDRESS_VALIDATION @"address_validation"
#define ICG_CONSENT_TYPE_ADVERTISEMENT      @"advertisement"
#define ICG_CONSENT_TYPE_ENGAGE             @"engage"
#define ICG_CONSENT_TYPE_EVENTS             @"analytics"
#define ICG_CONSENT_TYPE_LOCATION           @"location"
#define ICG_CONSENT_TYPE_CONTEXT_PROVIDER   @"context_provider"

// Consent sets
#define ICG_CONSENT_SET_ALL                 [NSSet setWithArray:@[ICG_CONSENT_TYPE_ADDRESS_VALIDATION, ICG_CONSENT_TYPE_ADVERTISEMENT, ICG_CONSENT_TYPE_ENGAGE, ICG_CONSENT_TYPE_EVENTS, ICG_CONSENT_TYPE_LOCATION, ICG_CONSENT_TYPE_CONTEXT_PROVIDER]]
#define ICG_CONSENT_SET_NONE                [[NSSet alloc] init]

#endif /* ICGConsentTypes_h */
