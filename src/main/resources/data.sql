DELETE FROM category;

INSERT INTO category(name)
VALUES ('Books');

INSERT INTO category(name)
VALUES ('Furniture');

INSERT INTO category(name)
VALUES ('Sports');

INSERT INTO category(name)
VALUES ('Electronics');

INSERT INTO category(name)
VALUES ('Clothing');

INSERT INTO category(name)
VALUES ('Foods');

DELETE FROM product;

INSERT INTO product(name, price, description, category, img)
VALUES ('商品A', 1000, '商品Aの説明です。', 'Books', '/images/omake_zasshi_otona.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品B', 2000, '商品Bの説明です。', 'Books', '/images/omake_zasshi_kodomo.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品C', 3000, '商品Cの説明です。', 'furniture', '/images/game_gaming_chair.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品D', 4000, '商品Dの説明です。', 'furniture', '/images/work_desk.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品E', 5000, '商品Eの説明です。', 'furniture', '/images/kagu_rocking_chair.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品F', 6000, '商品Fの説明です。', 'Books', '/images/entertainment_comic.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品G', 7000, '商品Gの説明です。', 'Sports', '/images/sports_badminton_shuttle.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品H', 8000, '商品Hの説明です。', 'Sports', '/images/stopwatch.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品G', 9000, '商品Iの説明です。', 'Electronics', '/images/kaden_soujiki_cordless_stick.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品H', 10000, '商品Jの説明です。', 'Clothing', '/images/kodomofuku_boy.png');
