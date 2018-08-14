//
//  PreferenceManager.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation
import RxSwift

final class PreferenceManager {
  enum Constants: String {
    case tokenKey
    case refreshTokenKey
  }
  
  fileprivate var tokenSubject: BehaviorSubject<String?> = BehaviorSubject(value: UserDefaults.standard.string(forKey: Constants.tokenKey.rawValue))
  
  var token: String? {
    get  {
      let token = UserDefaults.standard.string(forKey: Constants.tokenKey.rawValue)
      return token
    }
    set {
      UserDefaults.standard.set(newValue, forKey: Constants.tokenKey.rawValue)
      tokenSubject.onNext(newValue)
    }
  }
  
  var refreshToken: String? {
    get {
      let refreshToken = UserDefaults.standard.string(forKey: Constants.refreshTokenKey.rawValue)
      return refreshToken
    }
    set {
      UserDefaults.standard.set(newValue, forKey: Constants.refreshTokenKey.rawValue)
    }
  }
}

extension PreferenceManager: ReactiveCompatible {}

extension Reactive where Base: PreferenceManager {
  var  token: Observable<String?> {
    return self.base.tokenSubject.asObservable()
  }
}
