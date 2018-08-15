
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
  
  func getToken() -> Observable<(String, String, User)> {
    return Observable.create({ (observer) -> Disposable in
      let callbackUrl = URL(string: "PushApp://oauth-callback/github")!
      self.githubOAuth.authorize(withCallbackURL: callbackUrl, scope: "user, repo", state: "state", success: { (credential, response, parameters) in
        let oauthToken = credential.oauthToken
        let refreshToken = credential.oauthRefreshToken
        
        Alamofire.request("https://api.github.com/user?access_token=\(oauthToken)")
          .response(completionHandler: { (result) in
            guard let data = result.data,
              let user = try? JSONDecoder().decode(User.self, from: data) else { return }
            
            observer.onNext((oauthToken, refreshToken, user))
            observer.onCompleted()
          })
      }, failure: { error in
        observer.onError(error)
      })
      return Disposables.create { }
    })
  }
  
  func fetchPushEvent(_ completion: @escaping ((PushEvent)->Void)) {
    guard let loginId = App.preferenceManager.loginId else { return }
    Alamofire.request("http://13.209.88.99/api/commit/recent?login=\(loginId)")
      .response(completionHandler: { (result) in
        guard let data = result.data,
          let pushEvent = try? JSONDecoder().decode(PushEvent.self, from: data) else {
          return
        }
      
        completion(pushEvent)
    })
  }
  
  func updateDeviceToken(_ loginId: String, _ token: String) {
    let parameters = ["id": loginId, "token": token]
    Router.setToken.buildRequest(parameters: parameters, headers: Router.postedHeaders)
  }
}
