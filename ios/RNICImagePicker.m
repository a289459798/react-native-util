//
//  RNICImagePicker.m
//
//  Created by zhangzy on 2019/12/6.
//

#import "RNICImagePicker.h"


@implementation RNICImagePicker {
    NSMutableArray *_selectedAssets;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(open:(NSDictionary *)params SelectedAssets:(NSArray *)selectedAssets CallBack:(RCTResponseSenderBlock)callback) {

    int max = 1;
    float quality = 1.0;
    
    if(params[@"max"]) {
        max = [params[@"max"] intValue];
    }
    
    if(params[@"quality"]) {
        quality = [params[@"quality"] floatValue];
    }
    
    TZImagePickerController *imagePickerVc = [[TZImagePickerController alloc] initWithMaxImagesCount:max delegate:nil];

    imagePickerVc.allowCrop = params[@"crop"];
    imagePickerVc.allowPickingGif = NO;
    imagePickerVc.allowPickingVideo = NO;
    if(selectedAssets && self->_selectedAssets) {
        [selectedAssets enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            NSLog(@"dd:%@", obj);
            if(obj == [NSNull null]) {
                [self->_selectedAssets removeObjectAtIndex:idx];
            }
        }];
        imagePickerVc.selectedAssets = self->_selectedAssets;
    }


    [imagePickerVc setDidFinishPickingPhotosWithInfosHandle:^(NSArray<UIImage *> *photos, NSArray *assets, BOOL isSelectOriginalPhoto, NSArray<NSDictionary *> *infos) {

        self->_selectedAssets = [[NSMutableArray alloc] initWithArray:assets];
        NSMutableArray *photoList = [NSMutableArray array];
        [assets enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            NSDictionary *photoDict = [self handleCropImage:photos[idx] phAsset:obj quality:quality base64:params[@"base64"]];
            [photoList addObject:photoDict];
        }];
        if(callback) {
            callback(@[photoList]);
        }
    }];
    [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:imagePickerVc animated:YES completion:nil];
}

- (NSDictionary *)handleCropImage:(UIImage *)image phAsset:(PHAsset *)phAsset quality:(CGFloat)quality base64:(BOOL)base64 {
    [self createDir];

    NSMutableDictionary *photo  = [NSMutableDictionary dictionary];
    NSString *filename = [NSString stringWithFormat:@"%@%@", [[NSUUID UUID] UUIDString], [phAsset valueForKey:@"filename"]];
    NSString *fileExtension    = [filename pathExtension];
    NSMutableString *filePath = [NSMutableString string];
    BOOL isPNG = [fileExtension hasSuffix:@"PNG"] || [fileExtension hasSuffix:@"png"];

    if (isPNG) {
        [filePath appendString:[NSString stringWithFormat:@"%@ICImageCaches/%@", NSTemporaryDirectory(), filename]];
    } else {
        [filePath appendString:[NSString stringWithFormat:@"%@ICImageCaches/%@.jpg", NSTemporaryDirectory(), [filename stringByDeletingPathExtension]]];
    }

    NSData *writeData = isPNG ? UIImagePNGRepresentation(image) : UIImageJPEGRepresentation(image, quality/1.0);
    [writeData writeToFile:filePath atomically:YES];

    photo[@"uri"]       = filePath;
    photo[@"width"]     = @(image.size.width);
    photo[@"height"]    = @(image.size.height);
    NSInteger size = [[NSFileManager defaultManager] attributesOfItemAtPath:filePath error:nil].fileSize;
    photo[@"size"] = @(size);
    photo[@"mediaType"] = @(phAsset.mediaType);
    if (base64) {
        photo[@"base64"] = [NSString stringWithFormat:@"data:image/jpeg;base64,%@", [writeData base64EncodedStringWithOptions:0]];
    }

    return photo;
}

- (BOOL)createDir {
    NSString * path = [NSString stringWithFormat:@"%@ICImageCaches", NSTemporaryDirectory()];;
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isDir;
    if  (![fileManager fileExistsAtPath:path isDirectory:&isDir]) {
        //先判断目录是否存在，不存在才创建
        BOOL res = [fileManager createDirectoryAtPath:path withIntermediateDirectories:YES attributes:nil error:nil];
        return res;
    } else return NO;
}

@end
