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

extension UserInfoViewController {
  func bindUI() {
    navigationController?.navigationBar.isHidden = true
  }
  
  func bindEvents() {
    App.preferenceManager.rx.name
      .filter { $0 != nil }
      .subscribe(onNext: { [weak self] (name) in
        guard let `self` = self else { return }
        self.userLabel.text = name
      }).disposed(by: disposeBag)
  }
}
