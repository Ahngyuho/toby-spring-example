import dev.agh.dao.userdao.DaoFactory;
import dev.agh.dao.userdao.UserDaoJdbc;
import dev.agh.domain.Level;
import dev.agh.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


public class UserDaoTest {
    private UserDaoJdbc dao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        dao = context.getBean("userDao", UserDaoJdbc.class);
        dao.deleteAll();

        this.user1 = new User("1", "user1", "user1", Level.BASIC,1,0);
        this.user2 = new User("2", "user2", "user2",Level.SLIVER,55,10);
        this.user3 = new User("3", "user3", "user3", Level.GOLD, 100, 40);
    }

    @Test
    public void addAndGet() {
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        User getUser1 = dao.get(user1.getId());
        checkSameUser(user1,getUser1);

        dao.add(user2);
        User getUser2 = dao.get(user2.getId());
        checkSameUser(user2,getUser2);
    }

    @Test
    public void count() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);

    }

    @Test
    public void getUserFailure() throws SQLException {
        assertThat(dao.getCount()).isEqualTo(0);

        Throwable t = catchThrowable(() -> dao.get("unknown_id"));
        assertThat(t).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));

    }

    @Test
    public void update() {
        dao.deleteAll();
        dao.add(user1);

        user1.setName("updatedUser1");
        user1.setPassword("update");
        user1.setLevel(Level.GOLD);
        user1.setLogin(100);
        user1.setRecommend(999);
        dao.update(user1);

        User user1Update = dao.get(user1.getId());
        checkSameUser(user1,user1Update);
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }
}
