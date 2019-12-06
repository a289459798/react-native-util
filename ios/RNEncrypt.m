//
//  RNEncrypt.m
//  Base64
//
//  Created by zhangzy on 2019/12/6.
//

#import "RNEncrypt.h"

@implementation RNEncrypt

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(string2GBK:(NSString *)str CallBack:(RCTResponseSenderBlock)callback)
{
    NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSString *retStr = [[NSString alloc] initWithData:[str dataUsingEncoding:NSISOLatin1StringEncoding] encoding:enc];
    if([retStr length] > 0) {
        callback(@[retStr]);
    } else {
        callback(@[str]);
    }
}

RCT_REMAP_METHOD(decode,
                 encodeStr: (NSString *)str
                 key: (NSString *)key
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    
    key = [[self md5:key] lowercaseString];
    NSMutableData *code = [NSMutableData dataWithCapacity:0];
    
    NSData *data = [GTMBase64 decodeString:str];
    const char *a = [data bytes];
    const char *b = [key UTF8String];
    size_t lenA = data.length;
    size_t lenB = strlen(b);
    for (NSUInteger i=0; i<lenA; i++) {
        NSUInteger k = i % lenB;
        
        if (k >= key.length) {
            break; // 数组越界检查
        }
        
        char c = a[i] ^ b[k];
        [code appendBytes:&c length:1];
    }
    
    NSString *baseStr = [[NSString alloc] initWithData:code encoding:NSUTF8StringEncoding];
    resolve(baseStr);
    
}

RCT_REMAP_METHOD(encode,
                 decodeStr: (NSString *)str
                 key: (NSString *)key
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    key = [[self md5:key] lowercaseString];
    
    NSMutableData *code = [NSMutableData dataWithCapacity:0];
    const char *a = [str UTF8String];
    const char *b = [key UTF8String];
    
    size_t lenA = strlen(a);
    size_t lenB = strlen(b);
    for (NSUInteger i=0; i<lenA; i++) {
        NSUInteger k = i % lenB;
        
        if (k >= key.length) {
            break; // 数组越界检查
        }
        
        char c = a[i] ^ b[k];
        [code appendBytes:&c length:1];
    }
    
    
    NSString *baseStr = [GTMBase64 stringByEncodingData:code];
    resolve(baseStr);
    
}

- (NSString *) md5:(NSString *)str {
    
    if (!str) {
        return str;
    }
    
    const char *cStr = [str UTF8String];
    unsigned char result[16];
    CC_MD5( cStr, strlen(cStr), result );
    return [NSString stringWithFormat:@"%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X",
            result[0], result[1], result[2], result[3],
            result[4], result[5], result[6], result[7],
            result[8], result[9], result[10], result[11],
            result[12], result[13], result[14], result[15]];
}


@end
