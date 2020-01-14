//
//  RNQiNiu.m
//  Base64
//
//  Created by zhangzy on 2019/12/6.
//

#import "RNQiNiu.h"

@implementation RNQiNiu

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(upload,
                 files: (NSDictionary *)images
                 token: (NSString *)token
                 dir: (NSString *)dir
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    
    __block NSInteger successCount = 0;
    __block NSMutableDictionary *imagePaths = [[NSMutableDictionary alloc] init];
    
    [images enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        
        if([obj isEqual:@""]) {
            reject(@"999", @"上传图片不能为空", nil);
            return;
        }
        UIImage *image = [[UIImage alloc] initWithContentsOfFile:[obj objectForKey:@"uri"]];
        
        NSData *data;
        if (UIImagePNGRepresentation(image) == nil) {
            data = UIImageJPEGRepresentation(image, 0.6);
        } else {
            data = UIImagePNGRepresentation(image);
        }
        
        [self uploadForQiniu:data key:[NSString stringWithFormat:@"%@%@%d", dir, [NSString stringWithFormat:@"%.f", [[NSDate date] timeIntervalSince1970]*1000], (int)(10000 + (arc4random() % 90000))] token:token success:^(id responseObject) {
            
            successCount++;
            
            NSString *path = [NSString stringWithFormat:@"%@", responseObject];
            [imagePaths setValue:path forKey:key];
            
            if (images.count == successCount) {
                resolve(imagePaths);
            }
            
        } failure:^(NSError *error) {
            
            reject(@"999", error.domain, error);
            
        }];
        
    }];
    
}

- (void)uploadForQiniu:(NSData *)data key:(NSString *)key token:(NSString *)token  success:(void (^)(id responseObject)) successBlock failure:(void (^)(NSError *error)) failureBlock {
    
    QNUploadManager *uploadManage = [[QNUploadManager alloc] init];
    
    [uploadManage putData:data key:key token:token complete:^(QNResponseInfo *info, NSString *key, NSDictionary *resp) {
        
        
        if (info.statusCode == 200) {
            
            successBlock(key);
            
        } else {
            
            failureBlock(info.error);
            
        }
        
    } option:nil];
    
}

@end
