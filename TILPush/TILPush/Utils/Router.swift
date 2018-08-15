//
//  Router.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 14..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation
import RxSwift
import RxCocoa
import Alamofire
import RxAlamofire

enum Router {
  case user(String)
  case pushEvent(String)
  case setToken
}

extension Router {
  static let baseURLString = "http://13.209.88.99"
  
  var path: String {
    switch self {
    case .user(let token):
      return "/\(token)"
    case .pushEvent(let id):
      return "/\(id)"
    case .setToken:
      return "/batch/setToken"
    }
  }
  
  var url: URL? {
    guard let url = try? Router.baseURLString.asURL() else {
      return nil
    }
    
    return url.appendingPathComponent(path)
  }
  
  var method: HTTPMethod {
    switch self {
    case .user:
      return .get
    case .pushEvent:
      return .get
    case .setToken:
      return .post
    }
  }
  
  var parameterEncoding: ParameterEncoding {
    switch self {
    case .user:
      return URLEncoding.default
    case .pushEvent:
      return URLEncoding.default
    case .setToken:
      return URLEncoding.methodDependent
    }
  }
  
  static var postedHeaders: HTTPHeaders {
    var headers: HTTPHeaders = [:]
    headers["Content-Type"] = "application/x-www-form-urlencoded"
    return headers
  }
  
  static let manager: Alamofire.SessionManager = {
    let configuration = URLSessionConfiguration.default
    configuration.timeoutIntervalForRequest = 5
    configuration.timeoutIntervalForResource = 5
    configuration.httpCookieStorage = HTTPCookieStorage.shared
    configuration.urlCache = URLCache(memoryCapacity: 0, diskCapacity: 0, diskPath: nil)
    let manager = Alamofire.SessionManager(configuration: configuration)
    return manager
  }()
  
  func buildRequest() -> Observable<Data> {
    guard let url = url else { return Observable.empty() }
    
    return Router.manager.rx
      .request(method, url)
      .validate(statusCode: 200..<300)
      .data().observeOn(MainScheduler.instance)
  }
  
  func buildRequest(parameters: Parameters, headers: HTTPHeaders? = nil) {
    guard let url = url else { return }
    
    Alamofire.request(url,
                      method: method,
                      parameters: parameters,
                      encoding: parameterEncoding,
                      headers: headers)
  }
}
