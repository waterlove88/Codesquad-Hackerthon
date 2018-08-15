//
//  AppDelegate + Push.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation
import UserNotifications

import Firebase

@available(iOS 10.0, *)
extension AppDelegate {
  func requestNotificationAuthorization(_ application: UIApplication) {
    UNUserNotificationCenter.current().delegate = self
    let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
    UNUserNotificationCenter.current().requestAuthorization(options: authOptions,completionHandler: { _, _ in })
  }
  
  func postDeviceToken() {
    InstanceID.instanceID().instanceID(handler: { (result, error) in
      if let result = result {
        NotificationCenter.default.post(name: .postDeviceToken, object: nil, userInfo: ["deviceToken": result.token])
      } else if let error = error {
        print(error.localizedDescription)
      }
    })
  }
  
  fileprivate var applicationStateString: String {
    if UIApplication.shared.applicationState == .active {
      return "active"
    } else if UIApplication.shared.applicationState == .background {
      return "background"
    }else {
      return "inactive"
    }
  }
}

// MARK: - iOS10+, Handle notification
@available(iOS 10, *)
extension AppDelegate: UNUserNotificationCenterDelegate {
  func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
    let userInfo = notification.request.content.userInfo
    print("UserNotificationCenter] applicationState: \(applicationStateString) willPresentNotification: \(userInfo)")
    
    completionHandler([.alert, .sound, .badge])
  }
  
  func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
    let userInfo = response.notification.request.content.userInfo
    print("UserNotificationCenter] applicationState: \(applicationStateString) didReceiveResponse: \(userInfo)")
    
    completionHandler()
  }
}

extension Notification.Name {
  static let postDeviceToken = Notification.Name("postDeviceToken")
}
