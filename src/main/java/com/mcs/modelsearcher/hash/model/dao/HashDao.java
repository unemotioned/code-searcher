package com.mcs.modelsearcher.hash.model.dao;

import com.mcs.modelsearcher.hash.model.vo.SheetHash;
import org.apache.ibatis.session.SqlSession;

public class HashDao {
    public String selectHash(SqlSession session, String sheetName) {
        return session.selectOne("sheetHash.selectHash", sheetName);
    }

    public int insertHash(SqlSession session, SheetHash sheetHash) {
        return session.insert("sheetHash.insertHash", sheetHash);
    }

    public int updateHash(SqlSession session, SheetHash sheetHash) {
        return session.update("sheetHash.updateHash", sheetHash);
    }
}
