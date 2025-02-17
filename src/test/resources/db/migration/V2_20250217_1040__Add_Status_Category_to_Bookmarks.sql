CREATE SEQUENCE if NOT EXISTS category_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE categories
(
    id   bigint NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

ALTER TABLE bookmarks
    ADD category_id bigint;

ALTER TABLE bookmarks
    ADD status VARCHAR(255) DEFAULT 'DRAFT';

ALTER TABLE bookmarks
    ALTER COLUMN status SET NOT NULL;

ALTER TABLE bookmarks
    ADD CONSTRAINT fk_articles_on_category FOREIGN KEY (category_id) REFERENCES categories (id);
