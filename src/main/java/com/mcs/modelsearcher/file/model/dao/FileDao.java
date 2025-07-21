package com.mcs.modelsearcher.file.model.dao;

import org.apache.ibatis.session.SqlSession;

public class FileDao {
    public int insertPath(SqlSession session, String path) {
        return session.insert("filePath.insertPath", path);
    }
}
