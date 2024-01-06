package dev.agh.service.user;

import dev.agh.domain.Level;
import dev.agh.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.catchThrowable;

public class UserTest {
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void upgradeLevel() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() == null) continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @Test
    public void cannotUpgrade() {
        Level[] levels = Level.values();
        for (Level level : levels) {
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
            Throwable t = catchThrowable(() -> user.upgradeLevel());
            assertThat(t).isInstanceOf(IllegalStateException.class);
        }
    }
}
