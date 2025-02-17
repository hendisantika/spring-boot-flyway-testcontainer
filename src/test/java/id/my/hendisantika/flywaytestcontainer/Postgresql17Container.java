package id.my.hendisantika.flywaytestcontainer;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-flyway-testcontainer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 17/02/25
 * Time: 15.37
 * To change this template use File | Settings | File Templates.
 */
public class Postgresql17Container extends PostgreSQLContainer<Postgresql17Container> {
    private static final String IMAGE_VERSION = "postgres:17.3-alpine3.21";
    private static Postgresql17Container container;

    private Postgresql17Container() {
        super(IMAGE_VERSION);
    }

    public static Postgresql17Container getInstance() {
        if (container == null) {
            container = new Postgresql17Container();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }
}
