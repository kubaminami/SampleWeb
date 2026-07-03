DELETE FROM orders;

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
VALUES ('商品C', 3000, '商品Cの説明です。', 'Furniture', '/images/game_gaming_chair.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品D', 4000, '商品Dの説明です。', 'Furniture', '/images/work_desk.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品E', 5000, '商品Eの説明です。', 'Furniture', '/images/kagu_rocking_chair.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品F', 6000, '商品Fの説明です。', 'Books', '/images/entertainment_comic.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品G', 7000, '商品Gの説明です。', 'Sports', '/images/sports_badminton_shuttle.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品H', 8000, '商品Hの説明です。', 'Sports', '/images/stopwatch.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品I', 9000, '商品Iの説明です。', 'Electronics', '/images/kaden_soujiki_cordless_stick.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品J', 10000, '商品Jの説明です。', 'Clothing', '/images/kodomofuku_boy.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品K', 11000, '商品Kの説明です。', 'Clothing', '/images/fashion_tsuugakubou_cap.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品L', 12000, '商品Lの説明です。', 'Clothing', '/images/shoes_anzen_gutsu.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品M', 13000, '商品Mの説明です。', 'Sports', '/images/sports_softball__ball.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品N', 14000, '商品Nの説明です。', 'Electronics', '/images/kaden_hanenashi_senpuki_kaze.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品O', 15000, '商品Oの説明です。', 'Electronics', '/images/kaden_tablet.png');

INSERT INTO product(name, price, description, category, img)
VALUES ('商品P', 16000, '商品Pの説明です。', 'Sports', '/images/tabletennis_racket.png');

