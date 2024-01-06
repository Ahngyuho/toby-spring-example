package dev.agh.service.user;

import dev.agh.dao.userdao.UserDao;
import dev.agh.domain.Level;
import dev.agh.domain.User;

import java.util.List;

public class UserService {
    private static final int MIN_LOGOUT_FRO_SILVER = 50;
    private static final int MIN_RECOMMEND_FRO_GOLD = 30;
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) upgradeLevel(user);
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGOUT_FRO_SILVER);
            case SLIVER:
                return (user.getRecommend() >= MIN_RECOMMEND_FRO_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Level : " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
