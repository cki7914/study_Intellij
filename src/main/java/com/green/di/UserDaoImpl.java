package com.green.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Repository // 컴포넌트를 스캔하는 방법 중 하나
public class UserDaoImpl implements UserDao {
    @Autowired
    DataSource ds;
    final int FAIL = 0;

    @Override
    public int deleteUser(String id) {
        int rowCnt = FAIL; //  insert, delete, update

        Connection conn = null;
        PreparedStatement psmt = null;

        String sql = "delete from user_info where id= ? ";

        try {
            conn = ds.getConnection();
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, id);
//        int rowCnt = psmt.executeUpdate(); //  insert, delete, update
//        return rowCnt;
            return psmt.executeUpdate(); //  insert, delete, update
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        } finally {
            // close()를 호출하다가 예외가 발생할 수 있으므로, try-catch로 감싸야함.
//            try { if(psmt!=null) psmt.close(); } catch (SQLException e) { e.printStackTrace();}
//            try { if(conn!=null)  conn.close();  } catch (SQLException e) { e.printStackTrace();}
            close(psmt, conn); //     private void close(AutoCloseable... acs) {
        }
    }

    @Override
    public User selectUser(String id) {
        User user = null;

        Connection conn = null;
        PreparedStatement psmt = null;
        ResultSet rs = null;

        String sql = "select * from user_info where id= ? ";

        try {
            conn = ds.getConnection();
            psmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
            psmt.setString(1, id);

            rs = psmt.executeQuery(); //  select

            if (rs.next()) {
                user = new User();
                user.setId(rs.getString(1));
                user.setPwd(rs.getString(2));
                user.setName(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setBirth(new Date(rs.getDate(5).getTime()));
                user.setSns(rs.getString(6));
                user.setReg_date(new Date(rs.getTimestamp(7).getTime()));
            }
        } catch (SQLException e) {
            return null;
        } finally {
            // close()를 호출하다가 예외가 발생할 수 있으므로, try-catch로 감싸야함.
            // close()의 호출순서는 생성된 순서의 역순
//            try { if(rs!=null)    rs.close();    } catch (SQLException e) { e.printStackTrace();}
//            try { if(psmt!=null) psmt.close(); } catch (SQLException e) { e.printStackTrace();}
//            try { if(conn!=null)  conn.close();  } catch (SQLException e) { e.printStackTrace();}
            close(rs, psmt, conn);  //     private void close(AutoCloseable... acs) {
        }

        return user;
    }

    // 사용자 정보를 user_info테이블에 저장하는 메서드
    @Override
    public int insertUser(User user) {
        int rowCnt = FAIL;

        Connection conn = null;
        PreparedStatement psmt = null;

//        insert into user_info (id, pwd, name, email, birth, sns, reg_date)
//        values ('asdf22', '1234', 'smith', 'aaa@aaa.com', '2022-01-01', 'facebook', sysdate);
        String sql = "insert into user_info values (?, ?, ?, ?,?,?, sysdate) ";

        try {
            conn = ds.getConnection();
            psmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
            psmt.setString(1, user.getId());
            psmt.setString(2, user.getPwd());
            psmt.setString(3, user.getName());
            psmt.setString(4, user.getEmail());
            psmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
            psmt.setString(6, user.getSns());

            return psmt.executeUpdate(); //  insert, delete, update;
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        } finally {
            close(psmt, conn);  //     private void close(AutoCloseable... acs) {
        }
    }

    // 매개변수로 받은 사용자 정보로 user_info테이블을 update하는 메서드
    @Override
    public int updateUser(User user) {
        int rowCnt = FAIL; //  insert, delete, update

//        Connection conn = null;
//        PreparedStatement psmt = null;

        String sql = "update user_info " +
                "set pwd = ?, name=?, email=?, birth =?, sns=?, reg_date=? " +
                "where id = ? ";

//        try-with-resources - since jdk7
        try (
                Connection conn = ds.getConnection();
                PreparedStatement psmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        ){
            psmt.setString(1, user.getPwd());
            psmt.setString(2, user.getName());
            psmt.setString(3, user.getEmail());
            psmt.setDate(4, new java.sql.Date(user.getBirth().getTime()));
            psmt.setString(5, user.getSns());
            psmt.setTimestamp(6, new java.sql.Timestamp(user.getReg_date().getTime()));
            psmt.setString(7, user.getId());

            rowCnt = psmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return FAIL;
        }

        return rowCnt;
    }

    public void deleteAll() throws Exception {
        Connection conn = ds.getConnection();

        String sql = "delete from user_info ";

        PreparedStatement psmt = conn.prepareStatement(sql); // SQL Injection공격, 성능향상
        psmt.executeUpdate(); //  insert, delete, update
    }

    private void close(AutoCloseable... acs) {
        for(AutoCloseable ac :acs)
            try { if(ac!=null) ac.close(); } catch(Exception e) { e.printStackTrace(); }
    }
}
