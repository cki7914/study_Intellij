package com.green.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

public class DBConnectionTest2 {
    public static void main(String[] args) throws Exception {
//        // 스키마의 이름(springbasic)이 다른 경우 알맞게 변경
//        String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
//
//        // ** DB의 userid와 pwd를 알맞게 변경
//        String DB_USER = "green";
//        String DB_PASSWORD = "1234";
//        String DB_DRIVER = "oracle.jdbc.OracleDriver";
//
//        // DriverManagerDataSource : spring JDBC가 제공하는 클래스
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName(DB_DRIVER);
//        ds.setUrl(DB_URL);
//        ds.setUsername(DB_USER);
//        ds.setPassword(DB_PASSWORD);

        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // **데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);
        //assertTrue(conn != null);
    } // main()
}
