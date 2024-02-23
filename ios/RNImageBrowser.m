//
//  RNImageBrowser.m
//  Base64
//
//  Created by zhangzy on 2019/12/11.
//

#import "RNImageBrowser.h"
#import "YBImageBrowser.h"
#import "RCTUIManager.h"

@implementation RNImageBrowser

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(show:(NSArray *)list index:(int)index) {

    NSMutableArray *items = @[].mutableCopy;
    for(int i = 0; i < list.count; i++) {
        YBIBImageData *data = [YBIBImageData new];
        NSDictionary *dict = list[i];
        data.imageURL = [dict objectForKey:@"image"];
        if([dict objectForKey:@"tag"]) {
            data.projectiveView = [self.bridge.uiManager viewForReactTag:[dict objectForKey:@"tag"]];
        }
        [items addObject:data];
    }
    
    YBImageBrowser *browser = [YBImageBrowser new];
    browser.dataSourceArray = items;
    browser.currentPage = index;
    [browser show];
}

@end
