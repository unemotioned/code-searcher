package com.mcs.codesearcher.hash.model.service;

import com.mcs.codesearcher.common.SqlSessionTemplate;
import com.mcs.codesearcher.hash.model.dao.HashDao;
import com.mcs.codesearcher.hash.model.vo.SheetHash;
import org.apache.ibatis.session.SqlSession;

public class HashService {

    HashDao dao;

    public HashService() {
        dao = new HashDao();
    }

    public String selectHash(String sheetName) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        String refHash = dao.selectHash(session, sheetName);
        session.close();
        return refHash;
    }

    public int insertHash(SheetHash sheetHash) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.insertHash(session, sheetHash);
        session.close();
        return result;
    }

    public int updateHash(SheetHash sheetHash) {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.updateHash(session, sheetHash);
        session.close();
        return result;
    }

    public int fakeHash() {
        SqlSession session = SqlSessionTemplate.getSqlSession();
        int result = dao.fakeHash(session);
        session.close();
        return result;
    }
}
