//
//  LoginViewController.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 14..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

class LoginViewController: BaseViewController {
  @IBOutlet weak var enteredGithubButton: UIButton!
  
  override func viewDidLoad() {
    super.viewDidLoad()
        
    bindEvent()
  }
}

fileprivate extension LoginViewController {
  func bindEvent() {
    NotificationCenter.default.addObserver(self, selector: #selector(self.updateDeviceToken(_:)), name: .postDeviceToken, object: nil)
    
    enteredGithubButton.rx.tap.flatMap { _ in
        return App.api.getToken()
      }.subscribe(onNext: { arguments in
        let (token, refershToken, user) = arguments
        App.preferenceManager.token = token
        App.preferenceManager.refreshToken = refershToken
        App.preferenceManager.loginId = user.login
        App.preferenceManager.name = user.name
        App.preferenceManager.email = user.email
        App.appDelegate.postDeviceToken()
      }, onError: {[weak self] (error) in
        guard let `self` = self else { return }
        let alert  = UIAlertController(title: "Error", message: error.localizedDescription, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
      }).disposed(by: disposeBag)
    
    App.preferenceManager.rx.token.filter {  $0 != nil }
      .subscribe(onNext: { [weak self] _ in
        self?.dismiss(animated: true, completion: nil)
      }).disposed(by: disposeBag)
  }
  
  @objc func updateDeviceToken(_ notification: Notification) {
    if let userInfo = notification.userInfo,
      let deviceToken = userInfo["deviceToken"] as? String,
      let id = App.preferenceManager.loginId {
      App.api.updateDeviceToken(id, deviceToken)
    }
  }
}

extension LoginViewController {
  static func register() {
    _ = App.preferenceManager.rx.token
      .filter { $0 == nil}
      .delay(0.2, scheduler: MainScheduler.instance)
      .subscribe(onNext: { _ in
        let loginViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "LoginViewController")
        App.appDelegate.window?.rootViewController?.present(loginViewController, animated: true, completion: nil)
      })
  }
}
