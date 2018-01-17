/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import library.assistant.database.Database;

/**
 *
 * @author ASUS
 */
public class MemberDAO {

    public void saveMember(Member member) throws SQLException {
        Connection conn = Database.getInstance().getConnection();
        String insertMemberSql = "insert into lbdb.members(id,name,mobile,address) value(?,?,?,?)";
        PreparedStatement stmt2 = conn.prepareStatement(insertMemberSql);
        stmt2.setString(1, member.getId());
        stmt2.setString(2, member.getName());
        stmt2.setString(3, member.getMobile());
        stmt2.setString(4, member.getAddress());
        stmt2.execute();
    }

    public Member getMember(String id) throws SQLException {

        Connection conn = Database.getInstance().getConnection();
        String SelectSql = "select * from lbdb.members where id=?";
        PreparedStatement stmt = conn.prepareStatement(SelectSql);
        stmt.setString(1, id);
        ResultSet result = stmt.executeQuery();
        Member member = null;
        if (result.next()) {
            String name = result.getString("name");
            String mobile = result.getString("mobile");
            String address = result.getString("address");
            member = new Member(id, name, mobile, address);

        }
        return member;

    }

    public List<Member> getMembers() throws SQLException {

        Connection conn = Database.getInstance().getConnection();
        List<Member> members = new ArrayList();
        String selectSql = "select * from lbdb.members";
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(selectSql);
        while (results.next()) {
            String id = results.getString("id");
            String name = results.getString("name");
            String mobile = results.getString("mobile");
            String address = results.getString("address");
            members.add(new Member(id, name, mobile, address));
        }
        return members;
    }

    public void updateMember() {

    }

    public void deleteMember(String id) throws SQLException {

        Connection conn = Database.getInstance().getConnection();
        String deleteSql = "delete from lbdb.members where id=?";
        PreparedStatement preStmt = conn.prepareStatement(deleteSql);
        preStmt.setString(1, id);
        preStmt.execute();

    }

}
