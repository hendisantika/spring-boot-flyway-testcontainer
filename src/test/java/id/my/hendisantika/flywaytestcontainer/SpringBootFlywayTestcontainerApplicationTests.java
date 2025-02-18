package id.my.hendisantika.flywaytestcontainer;

import id.my.hendisantika.flywaytestcontainer.entity.Bookmark;
import id.my.hendisantika.flywaytestcontainer.entity.Category;
import id.my.hendisantika.flywaytestcontainer.repository.BookmarkRepository;
import id.my.hendisantika.flywaytestcontainer.repository.CategoryRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Transactional
class SpringBootFlywayTestcontainerApplicationTests {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.3-alpine3.21")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private BookmarkRepository bookmarkRepository;

//    @Container
//    @ServiceConnection
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.3-alpine3.21");
    @Autowired
    private CategoryRepository categoryRepository;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    //    @BeforeEach
//    void setUp() {
//        bookmarkRepository.deleteAll();
//        categoryRepository.deleteAll();
//        Category category = new Category();
//        category.setId(1L);
//        category.setName("Category 1");
//
//        List<Bookmark> bookmarks = List.of(new Bookmark(1L, "Bookmark Title", "www.google.com", "OK", category,
//                Instant.now(), Instant.now(), Instant.now()));
//        categoryRepository.save(category);
//        bookmarkRepository.saveAll(bookmarks);
//    }
    @BeforeEach
    void setUp() {
        // Clean up existing data
        bookmarkRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create and save a Category
        Category category = new Category();
        category.setName("Category 1");
        categoryRepository.save(category);

        // Create and save a Bookmark
        Instant now = Instant.now();
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle("Bookmark Title 1");
        bookmark.setUrl("www.google.com");
        bookmark.setStatus("OK");
        bookmark.setCategory(category);
        bookmark.setCreatedAt(now);
        bookmark.setUpdatedAt(now);

        bookmarkRepository.save(bookmark);
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldReturnBookmarkByTitle() {
        Category category = new Category();
        category.setName("Category 2");
        categoryRepository.save(category);

        // Create and save a Bookmark
        Instant now = Instant.now();
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle("Bookmark Title 2");
        bookmark.setUrl("www.google.com");
        bookmark.setStatus("OK");
        bookmark.setCategory(category);
        bookmark.setCreatedAt(now);
        bookmark.setUpdatedAt(now);

        bookmarkRepository.save(bookmark);
        Bookmark bookmark1 = bookmarkRepository.findByTitle("Bookmark Title 2").orElseThrow();
        assertEquals("Bookmark Title 2", bookmark1.getTitle(), "Post title should be 'Bookmark Title'");
    }

    @Test
    void checkExistingData() {
// Create and save a Category
        Category category = new Category();
        category.setName("Category 1");
        categoryRepository.save(category);

        // Create and save a Bookmark
        Instant now = Instant.now();
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle("Bookmark Title");
        bookmark.setUrl("www.google.com");
        bookmark.setStatus("OK");
        bookmark.setCategory(category);
        bookmark.setCreatedAt(now);
        bookmark.setUpdatedAt(now);

        bookmarkRepository.save(bookmark);

        // Assert that data was saved
        assertThat(categoryRepository.count()).isEqualTo(2);
        assertThat(bookmarkRepository.count()).isEqualTo(2);
        assertEquals("Bookmark Title", bookmark.getTitle(), "Post title should be 'Bookmark Title'");
    }

}
