//
//  LoginViewController.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 14..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import UIKit

class LoginViewController: BaseViewController {
  @IBOutlet weak var enteredGithubButton: UIButton!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    bindEvent()
  }
}

fileprivate extension LoginViewController {
  func bindEvent() {
    enteredGithubButton.rx.tap.flatMap { _ in
        return App.api.getToken()
      }.subscribe(onNext: { tokenTuple in
        let (token, refershToken) = tokenTuple
        App.preferenceManager.token = token
        App.preferenceManager.refreshToken = refershToken
      }, onError: {[weak self] (error) in
        guard let `self` = self else { return }
        let alert  = UIAlertController(title: "Error", message: error.localizedDescription, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
      }).disposed(by: disposeBag)
    
//    App.preferenceManager.rx.token.filter {  $0 != nil }
//      .subscribe(onNext: { [weak self] _ in
//        self?.dismiss(animated: true, completion: nil)
//      }).disposed(by: disposeBag)
  }
}

