//
//  RNQiNiu.h
//  Base64
//
//  Created by zhangzy on 2019/12/6.
//

#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import "QiniuSDK.h"

@interface RNQiNiu : NSObject <RCTBridgeModule>

@end
