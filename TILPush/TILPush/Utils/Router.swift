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
  case setToken
}

extension Router {
  static let baseURLString = "http://13.209.88.99"

  var path: String {
    switch self {
    case .setToken:
      return "/batch/setToken"
    }
  }

  var url: URL? {
    guard let url = try? Router.baseURLString.asURL() else { return nil }
    return url.appendingPathComponent(path)
  }
  
  static var postedHeaders: HTTPHeaders {
    var headers: HTTPHeaders = [:]
    headers["Content-Type"] = "application/x-www-form-urlencoded"
    return headers
  }
  
  func buildRequest(parameters: Parameters, headers: HTTPHeaders? = nil) {
    guard let url = url else { return }
    
    Alamofire.request(url,
                      method: .post,
                      parameters: parameters,
                      encoding: URLEncoding.methodDependent,
                      headers: headers)
  }
}
