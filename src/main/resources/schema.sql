CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE authors
(
    id_author integer                NOT NULL,
    name      character varying(500) NOT NULL
);

CREATE SEQUENCE author_id_author_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE author_id_author_seq OWNED BY authors.id_author;

CREATE TABLE categories
(
    id_category integer NOT NULL,
    name        character varying(24)
);

CREATE SEQUENCE categories_id_category_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE categories_id_category_seq OWNED BY categories.id_category;


CREATE TABLE categories_quotes
(
    category_id integer,
    quote_id    integer
);


CREATE SEQUENCE categories_quotes_category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE SEQUENCE categories_quotes_quote_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE quotes
(
    id_quote   integer                 NOT NULL,
    quote_text character varying(4000) NOT NULL,
    author_id  integer                 NOT NULL
);

CREATE SEQUENCE quotes_id_quote_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ONLY authors
    ALTER COLUMN id_author SET DEFAULT nextval('author_id_author_seq'::regclass);

ALTER TABLE ONLY categories
    ALTER COLUMN id_category SET DEFAULT nextval('categories_id_category_seq'::regclass);


ALTER TABLE ONLY categories_quotes
    ALTER COLUMN category_id SET DEFAULT nextval('categories_quotes_category_id_seq'::regclass);

ALTER TABLE ONLY categories_quotes
    ALTER COLUMN quote_id SET DEFAULT nextval('categories_quotes_quote_id_seq'::regclass);

ALTER TABLE ONLY quotes
    ALTER COLUMN id_quote SET DEFAULT nextval('quotes_id_quote_seq'::regclass);


ALTER TABLE ONLY authors
    ADD CONSTRAINT id_author_pk PRIMARY KEY (id_author);

ALTER TABLE ONLY categories
    ADD CONSTRAINT id_category_pk PRIMARY KEY (id_category);

ALTER TABLE ONLY quotes
    ADD CONSTRAINT id_quote_pk PRIMARY KEY (id_quote);

CREATE INDEX idx_categories_name_gin ON categories USING gin (name gin_trgm_ops);
CREATE INDEX quote_text_trgm_idx ON quotes USING gin (quote_text gin_trgm_ops);

ALTER TABLE ONLY quotes
    ADD CONSTRAINT author_id_quotes_fk FOREIGN KEY (author_id) REFERENCES authors (id_author);

ALTER TABLE ONLY categories_quotes
    ADD CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id_category);

ALTER TABLE ONLY categories_quotes
    ADD CONSTRAINT quote_id_fk FOREIGN KEY (quote_id) REFERENCES quotes (id_quote);


-- Insert records into categories
INSERT INTO categories (id_category, name)
VALUES (1, 'courage'),
       (2, 'friendship'),
       (3, 'happiness'),
       (4, 'humor'),
       (5, 'inspirational'),
       (6, 'life'),
       (7, 'love'),
       (8, 'motivational'),
       (9, 'success'),
       (10, 'wisdom'),
       (11, 'wisdom1');

-- Insert records into authors
INSERT INTO authors (id_author, name)
VALUES (1, 'Author One'),
       (2, 'Author Two'),
       (3, 'Author Three'),
       (4, 'Author Four'),
       (5, 'Author Five'),
       (6, 'Author Six'),
       (7, 'Author Seven'),
       (8, 'Author Eight'),
       (9, 'Author Nine'),
       (10, 'Author Ten');

-- Insert records into quotes
INSERT INTO quotes (id_quote, quote_text, author_id)
VALUES (1, 'The only way to do great work is to love what you do.', 1),
       (2, 'Success is not the key to happiness. Happiness is the key to success.', 2),
       (3, 'Life is 10% what happens to us and 90% how we react to it.', 3),
       (4, 'Keep your face always toward the sunshine—and shadows will fall behind you.', 4),
       (5, 'It does not matter how slowly you go as long as you do not stop.', 5),
       (6, 'The best time to plant a tree was twenty years ago. The second best time is now.', 6),
       (7, 'What lies behind us and what lies before us are tiny matters compared to what lies within us.', 7),
       (8, 'Keep your face always toward the sunshine—and shadows will fall behind you.', 8),
       (9, 'To live will be an awfully big adventure.', 9),
       (10, 'The only limit to our realization of tomorrow will be our doubts of today.', 10),
       (11, 'In the end, we will remember not the words of our enemies, but the silence of our friends.', 1),
       (12, 'Keep your face always toward the sunshine—and shadows will fall behind you.', 2),
       (13, 'Success usually comes to those who are too busy to be looking for it.', 3),
       (14, 'Life is really simple, but we insist on making it complicated.', 4),
       (15, 'Do not wait to strike till the iron is hot, but make it hot by striking.', 5),
       (16, 'Everything you’ve ever wanted is on the other side of fear.', 6),
       (17, 'The journey of a thousand miles begins with one step.', 7),
       (18, 'You only fail when you stop trying.', 8),
       (19, 'Believe you can and you’re halfway there.', 9),
       (20, 'Act as if what you do makes a difference. It does.', 10);

-- Insert records into categories_quotes
INSERT INTO categories_quotes (category_id, quote_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5),
       (1, 6),
       (2, 7),
       (7, 8),
       (7, 9),
       (5, 10),
       (1, 11),
       (2, 12),
       (3, 13),
       (4, 14),
       (5, 15),
       (1, 16),
       (2, 17),
       (3, 18),
       (4, 19),
       (5, 20);