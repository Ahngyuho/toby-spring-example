import dev.agh.dao.userdao.DaoFactory;
import dev.agh.dao.userdao.UserDao;
import dev.agh.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {
    @Test
    public void addAndGet() throws ClassNotFoundException, SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("gyu");
        user.setName("park");
        user.setPassword("test");

        dao.add(user);

        User user2 = dao.get(user.getId());

        Assertions.assertEquals(user.getName(), user2.getName());
        Assertions.assertEquals(user.getPassword(), user2.getName());
    }
}
