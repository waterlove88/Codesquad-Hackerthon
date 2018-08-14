//
//  API.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation
import Alamofire
import RxSwift
import OAuthSwift

struct API {
  let githubOAuth: OAuth2Swift = OAuth2Swift(
    consumerKey: "72af16d7c1cc7ce8eca3",
    consumerSecret: "b26f2cdc1a59d4931b3c29f7695ac598d4026b34",
    authorizeUrl: "https://github.com/login/oauth/authorize",
    accessTokenUrl: "https://github.com/login/oauth/access_token",
    responseType: "code"
  )
  
  var decoder: JSONDecoder {
    let decoder = JSONDecoder()
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    decoder.dateDecodingStrategy = .formatted(formatter)
    return decoder
  }
  
  func getToken() -> Observable<(String, String)> {
    return Observable.create({ (observer) -> Disposable in
      let callbackUrl = URL(string: "PushApp://oauth-callback/github")!
      self.githubOAuth.authorize(withCallbackURL: callbackUrl, scope: "user, repo", state: "state", success: { (credential, response, parameters) in
        let oauthToken = credential.oauthToken
        let refreshToken = credential.oauthRefreshToken
        observer.onNext((oauthToken, refreshToken))
        observer.onCompleted()
      }, failure: { error in
        observer.onError(error)
      })
      return Disposables.create { }
    })
  }
}