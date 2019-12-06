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

NS_ASSUME_NONNULL_BEGIN
#import "QiniuSDK.h"

@interface RNQiNiu : NSObject <RCTBridgeModule>

@end

NS_ASSUME_NONNULL_END
