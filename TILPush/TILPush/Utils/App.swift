//
//  App.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa

struct App {
  static let api: API = API()
  static let preferenceManager: PreferenceManager = PreferenceManager()
  static let appDelegate: AppDelegate = UIApplication.shared.delegate as! AppDelegate
}

extension App {
  static func showAlert(title: String?,
                        message: String?,
                        buttonTitle: String,
                        onView viewController: UIViewController?) -> Observable<Void> {
    return Observable<Void>.create({ observer -> Disposable in
      let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
      alert.addAction(UIAlertAction(title: buttonTitle, style: .cancel, handler: nil))
      viewController?.present(alert, animated: true, completion: nil)
      return Disposables.create {}
    })
  }
}
