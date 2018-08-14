//
//  Commit.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation

struct Commit: Codable {
  var message: String?
  var url: String?
  
  enum CodingKeys: String, CodingKey {
    case message
    case url
  }
}
