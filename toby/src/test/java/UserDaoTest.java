import dev.agh.dao.userdao.DaoFactory;
import dev.agh.dao.userdao.UserDao;
import dev.agh.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;


public class UserDaoTest {
    private static UserDao dao;

    @BeforeAll
    public static void setUp() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        dao = context.getBean("userDao", UserDao.class);
        dao.deleteAll();
    }
    @Test
    public void addAndGet() throws SQLException {

        assertThat(dao.getCount()).isEqualTo(0);

        User user = new User();
        user.setId("gyu");
        user.setName("park");
        user.setPassword("test");

        dao.add(user);
        assertThat(dao.getCount()).isEqualTo(1);

        User user2 = dao.get(user.getId());

        assertThat(user.getName()).isEqualTo(user2.getName());
        assertThat(user.getPassword()).isEqualTo(user2.getPassword());
    }

    @Test
    public void count() throws SQLException{
        User user1 = new User("1", "1", "1");
        User user2 = new User("2", "2", "2");
        User user3 = new User("3", "3", "3");

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
}
