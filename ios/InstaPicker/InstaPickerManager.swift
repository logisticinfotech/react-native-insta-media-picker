import Foundation

@objc (InstaPickerManager)
class InstaPickerManager: RCTViewManager {

  override func view() -> UIView! {
    return InstaPicker()
  }
}
