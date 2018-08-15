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
    case loginId
    case name
    case email
  }
  
  fileprivate var tokenSubject: BehaviorSubject<String?> = BehaviorSubject(value: UserDefaults.standard.string(forKey: Constants.tokenKey.rawValue))
  fileprivate var nameSubject: BehaviorSubject<String?> = BehaviorSubject(value: UserDefaults.standard.string(forKey: Constants.name.rawValue))
  
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
  
  var loginId: String? {
    get {
      let loginId = UserDefaults.standard.string(forKey: Constants.loginId.rawValue)
      return loginId
    }
    set {
      UserDefaults.standard.set(newValue, forKey: Constants.loginId.rawValue)
    }
  }
  
  var name: String? {
    get {
      let name = UserDefaults.standard.string(forKey: Constants.name.rawValue)
      return name
    }
    set {
      UserDefaults.standard.set(newValue, forKey: Constants.name.rawValue)
      nameSubject.onNext(newValue)
    }
  }
  
  var email: String? {
    get {
      let email = UserDefaults.standard.string(forKey: Constants.email.rawValue)
      return email
    }
    set {
      UserDefaults.standard.set(newValue, forKey: Constants.email.rawValue)
    }
  }
}

extension PreferenceManager: ReactiveCompatible {}

extension Reactive where Base: PreferenceManager {
  var token: Observable<String?> {
    return self.base.tokenSubject.asObservable()
  }
  
  var name: Observable<String?> {
    return self.base.nameSubject.asObservable()
  }
}
