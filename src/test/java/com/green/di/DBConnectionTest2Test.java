package com.green.di;

import junit.framework.TestCase;
import oracle.jdbc.proxy.annotation.Pre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DBConnectionTest2Test extends TestCase {
    @Autowired
    DataSource ds;

    @Test
    public void testInsertUser() {
        User user = new User("g111", "1111", "aaa", "g111@naver.com", new Date(), "fb", new Date());

        deleteAll();
        int result = insertUser(user);

        assertTrue(result == 1);
    }

    @Test
    public void testSelectUser() {
        String id = "g111";

        User user = selectUser(id);

        assertTrue(user.getId().equals(id));
    }

    @Test
    public void testDeleteUser(){
        String id = "g111";
        int result = 0;

        deleteAll();
        result = deleteUser(id);
        assertTrue(result == 0);

        User user = new User("g111", "1111", "aaa", "g111@naver.com", new Date(), "fb", new Date());
        result = insertUser(user);
        assertTrue(result == 1);

        result = deleteUser(id);
        assertTrue(result == 1);

        assertTrue(selectUser(user.getId()) == null);
    }

    @Test
    public void testUpdateUser(){
        deleteAll();
        User user = new User("g222", "2222", "bbb", "bbb@naver.com", new Date(), "fb", new Date());
        int result = insertUser(user);
        assertTrue(result == 1);

        User user2 = new User("g222", "1234", "ccc", "ccc@google.com", new Date(), "insta", new Date());
        result = updateUser(user2);
        assertTrue(result == 1);

        assertTrue(selectUser("g222").getName().equals("ccc"));
    }

    private int updateUser(User user) {
        int result = 0;

        try{
            Connection conn = ds.getConnection();

            String sql = "update user_info set pwd=?, name=?, email=? birth=?, sns=? where id=?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, user.getPwd());
            psmt.setString(2, user.getName());
            psmt.setString(3, user.getEmail());
            psmt.setDate  (4, new java.sql.Date(user.getBirth().getTime()));
            psmt.setString(5, user.getSns());
            psmt.setString(6, user.getId());
            result = psmt.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private int deleteUser(String id) {
        int result = 0;

        try{
            Connection conn = ds.getConnection();

            String sql = "delete USER_INFO where id = ?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
            result = psmt.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    private User selectUser(String id) {
        User user = null;

        try{
            Connection conn = ds.getConnection();

            String sql = "select * from USER_INFO where id = ?";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
            ResultSet rs = psmt.executeQuery();

            if(rs.next()){
                user = new User();
                user.setId(rs.getString("id"));
                user.setPwd(rs.getString("pwd"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setBirth(rs.getDate("birth"));
                user.setSns(rs.getString("sns"));
                user.setReg_date(rs.getTimestamp("reg_date"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }

    private void deleteAll() {
        try{
            Connection conn = ds.getConnection();

            String sql = "delete from USER_INFO";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 사용자 정보를 user_info 테이블에 저장하는 매서드 생성
    public int insertUser(User user) {
        int result = 0;

        try {
            Connection conn = ds.getConnection();

//            insert into USER_INFO (ID, PWD, NAME, EMAIL, BIRTH, SNS, REG_DATE)
//            values ('g111', '1111', 'aaa', 'g111@naver.com', '2000-01-01', 'fb', sysdate);

//            String sql = " insert into USER_INFO (ID, PWD, NAME, EMAIL, BIRTH, SNS, REG_DATE) "
//                       + " values ('g111', '1111', 'aaa', 'g111@naver.com', '2000-01-01', 'fb', sysdate) ";

            String sql = "insert into USER_INFO values (?, ?, ?, ?, ?, ?, sysdate)";
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, user.getId());
            psmt.setString(2, user.getPwd());
            psmt.setString(3, user.getName());
            psmt.setString(4, user.getEmail());
            psmt.setDate  (5, new java.sql.Date(user.getBirth().getTime()));
            psmt.setString(6, user.getSns());
            result = psmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Test // 이 매서드 안의 내용을 테스트 한다는 뜻
    public void testmain() throws Exception {
//        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
//        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // **데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);

        assertTrue(conn == null);
    }
} // class