package dev.agh.dao.userdao;

import dev.agh.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao dao = new UserDao(new DConnectionMaker());
        User user = new User();
        user.setId("whiteship");
        user.setName("myName");
        user.setPassword("test");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());

        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "등록 조회");
    }
}
