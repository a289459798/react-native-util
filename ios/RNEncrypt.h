//
//  RNEncrypt.h
//  Base64
//
//  Created by zhangzy on 2019/12/6.
//

#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import "GTMBase64.h"
#import <CommonCrypto/CommonDigest.h>

@interface RNEncrypt : NSObject <RCTBridgeModule>

@end
