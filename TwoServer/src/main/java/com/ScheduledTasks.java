package com;
import com.entity.Users;
import com.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@EnableScheduling
@Component
@Log
public class ScheduledTasks
{
    final int days=365;
    @Autowired
    UserService userService;
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteInactiveUser()
    {
        log.severe("!!!Поиск и Удаление неактивных юзеров!!!");
        List<Users> lists= userService.getAll();
        for (Users user:lists)
        {
            log.severe(user.getLogin() + " " + ChronoUnit.DAYS.between(user.getLastdate(), LocalDate.now()));
            if (ChronoUnit.DAYS.between(user.getLastdate(), LocalDate.now())>days)
            {
                userService.delete(user.getId().intValue());
            }
        }
    }
}
