//
//  RNImageBrowser.swift
//  RNUtil
//
//  Created by zhangzy on 2024/3/28.
//

import Foundation
import JXPhotoBrowser
import SDWebImage

@objc(RNImageBrowser)
class RNImageBrowser: NSObject, RCTBridgeModule {

    @objc var viewRegistry_DEPRECATED: RCTViewRegistry!
    static func moduleName() -> String! {
        return "RNImageBrowser";
    }
    static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
    @objc(show:index:)
    func show(images: NSArray, index: Int) -> Void {
    // Date is ready to use!
        DispatchQueue.main.async {
            let browser = JXPhotoBrowser()
            browser.numberOfItems = {
                images.count
            }
            browser.pageIndex = index
            browser.reloadCellAtIndex = { context in
                let browserCell = context.cell as? JXPhotoBrowserImageCell
                
                if let dict = images[context.index] as? [String: AnyObject] {
                    browserCell?.imageView.sd_setImage(with: URL(string: dict["image"] as? String ?? ""), placeholderImage: nil, options: [], completed: { (_, _, _, _) in
                        browserCell?.setNeedsLayout()
                    })
                }
            }
            browser.pageIndicator = JXPhotoBrowserNumberPageIndicator()

            browser.transitionAnimator = JXPhotoBrowserZoomAnimator(previousView: { index -> UIView? in
                if let dict = images[index] as? [String: AnyObject] {
                    return self.viewRegistry_DEPRECATED.view(forReactTag: dict["tag"] as? NSNumber )
                }
                return nil
            })
            browser.show()
        }
        
    }

}
