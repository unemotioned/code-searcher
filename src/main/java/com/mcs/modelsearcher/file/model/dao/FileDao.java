package com.mcs.modelsearcher.file.model.dao;

import org.apache.ibatis.session.SqlSession;

public class FileDao {
    public String selectPath(SqlSession session) {
        return session.selectOne("filePath.selectPath");
    }

    public int deleteInvalidPath(SqlSession session) {
        return session.delete("filePath.deleteInvalidPath");
    }

    public int insertPath(SqlSession session, String path) {
        return session.insert("filePath.insertPath", path);
    }
}
