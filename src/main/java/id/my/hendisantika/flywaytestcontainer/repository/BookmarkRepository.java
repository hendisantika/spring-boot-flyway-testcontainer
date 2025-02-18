package id.my.hendisantika.flywaytestcontainer.repository;

import id.my.hendisantika.flywaytestcontainer.entity.Bookmark;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-flyway-testcontainer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 17/02/25
 * Time: 17.11
 * To change this template use File | Settings | File Templates.
 */
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByTitle(@Size(max = 200) @NotNull String title);
}
