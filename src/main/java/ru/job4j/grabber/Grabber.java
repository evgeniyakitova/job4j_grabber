package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    Properties cfg;

    public Grabber(Properties cfg) {
        this.cfg = cfg;
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parser = (Parse) map.get("parse");
            List<Post> posts = parser.list("https://www.sql.ru/forum/job-offers");
            for (Post post : posts) {
                store.save(post);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        try (InputStream in = Grabber.class.getClassLoader().getResourceAsStream("app.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Grabber grab = new Grabber(properties);
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        PsqlStore store = new PsqlStore(properties);
        grab.init(new SqlRuParse(new SqlRuDateTimeParser()), store, scheduler);
    }
}
