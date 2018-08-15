package com.developer.codesquad.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.developer.codesquad.domain.BatchRequest;
import com.developer.codesquad.domain.TokenRequest;

@Repository
public class BatchDao {
	@Autowired
    private SqlSessionTemplate sqlSession;

	public List<BatchRequest> sendPush() {
		return sqlSession.selectList("batchMapper.selectToken");
	}
	
	public int isPushAgree(TokenRequest tokenRequest) {
		return sqlSession.selectOne("batchMapper.isPushAgree", tokenRequest);
	}
	
	public int setToken(TokenRequest tokenRequest) {
		return sqlSession.update("batchMapper.updateToken", tokenRequest);
	}
}
