//
//  RNICImagePicker.m
//
//  Created by zhangzy on 2019/12/6.
//

#import "RNICImagePicker.h"


@implementation RNICImagePicker {
    NSMutableArray *_selectedAssets;
    RCTPromiseResolveBlock _resolve;
    RCTPromiseRejectBlock _reject;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(open:(NSDictionary *)params SelectedAssets:(NSArray *)selectedAssets CallBack:(RCTResponseSenderBlock)callback) {

    int max = 1;
    float quality = 1.0;
    float cropWidth = 200;
    float cropHeight = 200;

    CGRect rect = [[UIScreen mainScreen] bounds];
    CGSize size = rect.size;
    CGFloat width = size.width;
    CGFloat height = size.height;

    if(params[@"max"]) {
        max = [params[@"max"] intValue];
    }

    if(params[@"quality"]) {
        quality = [params[@"quality"] floatValue];
    }

    if(params[@"cropWidth"]) {
        cropWidth = [params[@"cropWidth"] floatValue];
    }

    if(params[@"cropHeight"]) {
        cropHeight = [params[@"cropHeight"] floatValue];
    }

    TZImagePickerController *imagePickerVc = [[TZImagePickerController alloc] initWithMaxImagesCount:max delegate:nil];

    imagePickerVc.allowCrop = params[@"crop"] || NO;
    imagePickerVc.allowPickingGif = NO;
    imagePickerVc.allowPickingVideo = NO;

    if(params[@"cropWidth"] && params[@"cropHeight"]) {
        imagePickerVc.cropRect = CGRectMake((width - cropWidth)/2, (height - cropHeight)/2, cropWidth, cropHeight);
    }

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
            if(isSelectOriginalPhoto) {

                [[TZImageManager manager] requestImageDataForAsset:obj completion:^(NSData *imageData, NSString *dataUTI, UIImageOrientation orientation, NSDictionary *info) {
                   [photoList addObject:[self handleOriginalPhotoData:imageData phAsset:obj quality:quality base64:params[@"base64"]]];
                   if ([photoList count] == [assets count]) {
                       callback(@[photoList]);
                   }
                } progressHandler:^(double progress, NSError *error, BOOL *stop, NSDictionary *info) {

                }];

            } else {
                [photoList addObject:[self handleCropImage:photos[idx] phAsset:obj quality:quality base64:params[@"base64"]]];
                if ([photoList count] == [assets count]) {
                    callback(@[photoList]);
                }
            }

        }];
    }];
    [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:imagePickerVc animated:YES completion:nil];
}

- (NSDictionary *)handleOriginalPhotoData:(NSData *)data phAsset:(PHAsset *)phAsset quality:(CGFloat)quality base64:(BOOL)base64{
    [self createDir];

    NSMutableDictionary *photo  = [NSMutableDictionary dictionary];
    NSString *filename = [NSString stringWithFormat:@"%@%@", [[NSUUID UUID] UUIDString], [phAsset valueForKey:@"filename"]];
    NSString *fileExtension    = [filename pathExtension];
    UIImage *image = nil;
    NSData *writeData = nil;
    NSMutableString *filePath = [NSMutableString string];

    BOOL isPNG = [fileExtension hasSuffix:@"PNG"] || [fileExtension hasSuffix:@"png"];

    writeData = data;

    if (isPNG) {
        [filePath appendString:[NSString stringWithFormat:@"%@ICImageCaches/%@", NSTemporaryDirectory(), filename]];
    } else {
        [filePath appendString:[NSString stringWithFormat:@"%@ICImageCaches/%@.jpg", NSTemporaryDirectory(), [filename stringByDeletingPathExtension]]];
    }

    [writeData writeToFile:filePath atomically:YES];

    photo[@"uri"]       = filePath;
    photo[@"width"]     = @(image.size.width);
    photo[@"height"]    = @(image.size.height);
    NSInteger size      = [[NSFileManager defaultManager] attributesOfItemAtPath:filePath error:nil].fileSize;
    photo[@"size"]      = @(size);
    photo[@"mediaType"] = @(phAsset.mediaType);
    if (base64) {
        photo[@"base64"] = [NSString stringWithFormat:@"data:image/jpeg;base64,%@", [writeData base64EncodedStringWithOptions:0]];
    }

    return photo;
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

RCT_REMAP_METHOD(save,
                 image: (NSString *)images
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{

    _resolve = resolve;
    _reject = reject;
    NSData *imageData;
    if([images hasPrefix:@"http"]) {
        imageData = [NSData dataWithContentsOfURL: [NSURL URLWithString:images]];
    } else {
        imageData = [[NSData alloc] initWithBase64EncodedString:images options:NSDataBase64DecodingIgnoreUnknownCharacters];
    }
    UIImage *image = [UIImage imageWithData:imageData];
    UIImageWriteToSavedPhotosAlbum(image, self, @selector(image:didFinishSavingWithError:contextInfo:), nil);
}


- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo:(void *)contextInfo {
    if(error) {
        if(_reject) {
            if(error.code == -3310) {
                _reject(@"999", @"没有权限", error);
            } else {
                _reject(@"999", error.domain, error);

            }
        }
    } else {
        if (_resolve) {
            _resolve(@"成功");
        }
    }
}

@end
