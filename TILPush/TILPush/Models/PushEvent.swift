//
//  PushEvent.swift
//  TILPush
//
//  Created by yuaming on 2018. 8. 15..
//  Copyright © 2018년 yuaming. All rights reserved.
//

import Foundation

struct PushEvent: Codable {
  var commits: [Commit]?
  var createdAt: Date?
  
  enum CodingKeys: String, CodingKey {
    case commits
    case createdAt
  }
}
