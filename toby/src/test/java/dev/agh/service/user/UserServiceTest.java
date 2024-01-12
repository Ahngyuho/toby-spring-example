package dev.agh.service.user;

import dev.agh.dao.userdao.UserDao;
import dev.agh.domain.Level;
import dev.agh.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserServiceTest {
    private static final int MIN_LOGOUT_FRO_SILVER = 50;
    private static final int MIN_RECOMMEND_FRO_GOLD = 30;
    UserDao userDao;
    UserService userService;

    List<User> users;
    DataSource dataSource;

    @BeforeEach
    public void setup() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        userDao = context.getBean("userDao", UserDao.class);
        userService = context.getBean("userService", UserService.class);

        userDao.deleteAll();
        this.dataSource = context.getBean("dataSource", DataSource.class);

        users = Arrays.asList(
                new User("test1", "test1", "p1", Level.BASIC, MIN_LOGOUT_FRO_SILVER - 1, 0),
                new User("test2", "test2", "p2", Level.BASIC, MIN_LOGOUT_FRO_SILVER, 0),
                new User("test3", "test3", "p3", Level.SLIVER, 60, MIN_RECOMMEND_FRO_GOLD - 1),
                new User("test4", "test4", "p4", Level.SLIVER, 60, MIN_RECOMMEND_FRO_GOLD),
                new User("test5", "test5", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel()).isEqualTo(userWithLevelRead.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded){
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }else assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
    }

    @Test
    public void upgradeAllOrNothing() throws Exception{
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setDataSource(this.dataSource);
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        try{
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        checkLevelUpgraded(users.get(1), false);
    }

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

}