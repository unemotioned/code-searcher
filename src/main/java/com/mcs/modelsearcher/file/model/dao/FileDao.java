package com.mcs.modelsearcher.file.model.dao;

import org.apache.ibatis.session.SqlSession;

public class FileDao {
    public String selPath(SqlSession session) {
        return session.selectOne("filePath.selPath");
    }

    public int delInvalidPath(SqlSession session) {
        return session.delete("filePath.delInvalidPath");
    }

    public int insertPath(SqlSession session, String path) {
        return session.insert("filePath.insertPath", path);
    }
}
