//
//  AppDelegate.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 14..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import UIKit

import OAuthSwift
import Firebase

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
  var window: UIWindow?
  
  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
    
    #if DEBUG
      removeUserDefaults()
    #endif
    
    FirebaseApp.configure()
    
    application.registerForRemoteNotifications()
    requestNotificationAuthorization(application)
    postDeviceToken()
    
    LoginViewController.register()
    
    return true
  }
  
  func application(_ app: UIApplication, open url: URL, options: [UIApplicationOpenURLOptionsKey: Any] = [:]) -> Bool {
    if url.host == "oauth-callback" {
      OAuthSwift.handle(url: url)
    }
    
    return true
  }
  
  fileprivate func removeUserDefaults() {
    let appDomain = Bundle.main.bundleIdentifier
    UserDefaults.standard.removePersistentDomain(forName: appDomain!)
  }
}
