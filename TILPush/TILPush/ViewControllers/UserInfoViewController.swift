//
//  UserInfoViewController.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 14..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import UIKit

class UserInfoViewController: BaseViewController {
  @IBOutlet weak var userLabel: UILabel!
  @IBOutlet weak var remainingTimeLabel: UILabel!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    bindUI()
    bindEvents()
  }
}

fileprivate extension UserInfoViewController {
  func bindUI() {
    navigationController?.navigationBar.isHidden = true
  }
  
  func bindEvents() {
    NotificationCenter.default.addObserver(self, selector: #selector(updateDeviceToken(_:)), name: .postDeviceToken, object: nil)
  }
  
  @objc func updateDeviceToken(_ notification: Notification) {
    if let userInfo = notification.userInfo,
      let deviceToken = userInfo["deviceToken"] as? String {
      App.api.updateDeviceToken("yuaming", deviceToken)
    }
  }
}
