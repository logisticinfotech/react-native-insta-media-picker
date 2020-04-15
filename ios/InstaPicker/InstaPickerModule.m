#import <React/RCTViewManager.h>
#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"
#import "React/RCTConvert.h"
#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import <AVKit/AVKit.h>
#import <Photos/Photos.h>
@import YPImagePicker;




@interface RCT_EXTERN_MODULE(InstaPickerManager, RCTViewManager)

+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(onNextPress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBackPress, RCTBubblingEventBlock)




@end
