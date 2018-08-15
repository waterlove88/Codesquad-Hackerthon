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

extension AppDelegate {
  func requestNotificationAuthorization(_ application: UIApplication) {
    if #available(iOS 10.0, *) {
      UNUserNotificationCenter.current().delegate = self
      let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
      UNUserNotificationCenter.current().requestAuthorization(options: authOptions, completionHandler: { _, _ in })
    } else {
      let settings: UIUserNotificationSettings = UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
      application.registerUserNotificationSettings(settings)
    }
  }
  
  func printNotificationUserInfo(_ launchOptions: [UIApplicationLaunchOptionsKey: Any]?) {
    if let userInfo = launchOptions?[UIApplicationLaunchOptionsKey.remoteNotification] {
      print("[RemoteNotification] applicationState: \(applicationStateString) didFinishLaunchingWithOptions for iOS9: \(userInfo)")
      // TODO: - Handle background notification
    }
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

@available(iOS 10, *)
extension AppDelegate: UNUserNotificationCenterDelegate {
  // iOS10+, called when presenting notification in foreground
  func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
    let userInfo = notification.request.content.userInfo
    print("[UserNotificationCenter] applicationState: \(applicationStateString) willPresentNotification: \(userInfo)")
    
    // TODO: - Handle foreground notification
    completionHandler([.alert])
  }
  
  // iOS10+, called when received response (default open, dismiss or custom action) for a notification
  func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
    let userInfo = response.notification.request.content.userInfo
    print("[UserNotificationCenter] applicationState: \(applicationStateString) didReceiveResponse: \(userInfo)")
    
    //TODO: - Handle background notification
    completionHandler()
  }
}

extension AppDelegate: MessagingDelegate {
  func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
    print("[RemoteNotification] didRefreshRegistrationToken: \(fcmToken)")
  }
  
  // iOS9, called when presenting notification in foreground
  func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) {
    print("[RemoteNotification] applicationState: \(applicationStateString) didReceiveRemoteNotification for iOS9: \(userInfo)")
    if UIApplication.shared.applicationState == .active {
      // TODO: - Handle foreground notification
    } else {
      // TODO: - Handle background notification
    }
  }
}
