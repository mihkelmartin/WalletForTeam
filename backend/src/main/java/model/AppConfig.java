package model;

import aspects.MoneyCalculationAspect;
import com.mongodb.MongoClient;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import repository.*;
import service.*;

import java.util.Properties;

/**
 * Created by mihkel on 6.04.2018.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@ComponentScan("service")
public class AppConfig {

    @Value( "${mongoDB.mongoHost}" )
    private String mongoHost;
    @Value( "${mongoDB.mongoPort}" )
    private Integer mongoPort;
    @Value( "${mongoDB.authDBName}" )
    private String mongoAuthDBName;
    @Value( "${mongoDB.DBName}" )
    private String mongoDBName;
    @Value( "${mongoDB.username}" )
    private String mongoUsername;
    @Value( "${mongoDB.password}" )
    private String mongoPassword;

    public @Bean
    MongoDbFactory mongoDbSimpleFactory() throws Exception {
        return new SimpleMongoDbFactory(
                new MongoClient(new ServerAddress(mongoHost, mongoPort),
                        MongoCredential.createCredential(mongoUsername,mongoAuthDBName,
                                mongoPassword.toCharArray()),
                        new MongoClientOptions.Builder().build()),
                mongoDBName);
    }
    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    public @Bean
    MongoOperations mongoOperations() throws Exception {
        return new MongoTemplate(mongoDbSimpleFactory());
    }

    public @Bean
    LoginService loginService() throws Exception {
        return new LoginService();
    }

    public @Bean
    JavaMailSender javaMailSender() throws Exception {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername("walletforteam@gmail.com");
        sender.setPassword("murakas301polaarjoon");
        sender.setProtocol("smtp");

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.port", "25");
        mailProps.put("mail.smtps.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.starttls.required", "true");

        sender.setJavaMailProperties(mailProps);

        return sender;
    }

    public @Bean
    RestTemplateBuilder restTemplateBuilder() throws Exception {
        return new RestTemplateBuilder();
    }

    public @Bean
    EventDao eventDao() throws Exception {
        return new MongoDBEventDao();
    }

    public @Bean
    MemberDao memberDao() throws Exception {
        return new MongoDBMemberDao();
    }
    public @Bean
    TransactionDao transactionDao() throws Exception {
        return new MongoDBTransactionDao();
    }

    public @Bean
    TransactionItemDao transactionItemDao() throws Exception {
        return new MongoDBTransactionItemDao();
    }


    public @Bean
    EventService eventService() throws Exception {
        return new EventServiceImpl();
    }

    public @Bean
    MemberService memberService() throws Exception {
        return new MemberServiceImpl();
    }

    public @Bean
    TransactionService transactionService() throws Exception {
        return new TransactionServiceImpl();
    }

    public @Bean
    PaymentService paymentService() throws Exception {
        return new PaymentServiceImpl();
    }

    public @Bean
    MoneyCalculationAspect moneyCalculationAspect() {
        return new MoneyCalculationAspect();
    }

}