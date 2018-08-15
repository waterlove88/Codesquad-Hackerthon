package com.developer.codesquad.mapper;

import com.developer.codesquad.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User findUser(User user);

    void insertUser(User param);
}
