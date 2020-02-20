//
//  RNUpdate.m
//  RNUtil
//
//  Created by zhangzy on 2019/12/6.
//

#import "RNUpdate.h"

@implementation RNUpdate

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_REMAP_METHOD(check,
                 appid: (NSString *)appid
                 showDialog: (BOOL *)show
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    NSString *urlStr    = [NSString stringWithFormat:@"https://itunes.apple.com/lookup?id=%@", appid];
    NSURL *url          = [NSURL URLWithString:urlStr];
    NSURLRequest *request   = [NSURLRequest requestWithURL:url];
    
    NSURLSession *sharedSession = [NSURLSession sharedSession];
    
    NSURLSessionDataTask *dataTask = [sharedSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if (data && (error == nil)) {
            // 网络访问成功
            NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:nil];
            
            NSString *version = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
            
            NSArray *arr = [jsonDict objectForKey:@"results"];
            if([arr count] > 0) {
                if([[version stringByReplacingOccurrencesOfString:@"." withString:@""] integerValue] < [[[arr[0] objectForKey:@"version"] stringByReplacingOccurrencesOfString:@"." withString:@""] integerValue]) {
                    
                    if (show) {
                        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"升级提示" message:[arr[0] objectForKey:@"releaseNotes"] preferredStyle:UIAlertControllerStyleAlert];
                        [alert addAction:[UIAlertAction actionWithTitle:@"前往" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action) {
                            NSString *trackViewUrl = arr[0][@"trackViewUrl"]; // AppStore 上软件的地址
                            if (trackViewUrl) {
                                NSURL *appStoreURL = [NSURL URLWithString:trackViewUrl];
                                if ([[UIApplication sharedApplication] canOpenURL:appStoreURL]) {
                                    [[UIApplication sharedApplication] openURL:appStoreURL];
                                }
                            }
                        }]];
                        [alert addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
                        if(UIApplication.sharedApplication.keyWindow.rootViewController != nil) {
                            [UIApplication.sharedApplication.keyWindow.rootViewController presentViewController:alert animated:NO completion:nil];
                        }
                    }
                    
                    resolve(@[]);
                } else {
                    reject(@"999", @"当前是最新版", nil);
                }
            } else {
                reject(@"999", @"当前是最新版", nil);
            }
            
        } else {
            // 网络访问失败
            reject(@"999", error.domain, error);
        }
    }];
    [dataTask resume];
}

@end
