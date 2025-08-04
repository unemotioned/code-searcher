package com.mcs.codesearcher.common;

import com.ibatis.common.resources.Resources;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Statement;
import java.util.Properties;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionTemplate {

  private static SqlSessionFactory sqlSessionFactory;

  static {
    try {
      // Resolve full DB path
      String dbPath = Paths.get(System.getProperty("user.home"), ".code-searcher", "sqlite.db")
          .toString();

      // Inject it into mybatis-config.xml as a property
      Properties props = new Properties();
      props.setProperty("dbPath", dbPath);

      // Load config with injected properties
      InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);

    } catch (Exception e) {
      System.out.println("SqlSessionTemplate.SqlSessionFactory: " + e.getMessage());
    }
  }

  public static SqlSession getSqlSession() {
    SqlSession session = sqlSessionFactory.openSession(true);
    try (Statement stmt = session.getConnection().createStatement()) {
      stmt.execute("PRAGMA foreign_keys = ON;");
    } catch (Exception e) {
      System.out.println(
          "SqlSessionTemplate.getSqlSession - enabling foreign keys: " + e.getMessage());
    }
    return session;
  }
}
