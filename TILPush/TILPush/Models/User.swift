//
//  User.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation

struct User: Codable {
  var login: String = ""
  var name: String?
  var email: String?
  
  enum CodingKeys: String, CodingKey {
    case login
    case name
    case email
  }
}
