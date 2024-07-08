INSERT INTO article(user_id, title, content) VALUES(1, '가가가가', '1111');
INSERT INTO article(user_id, title, content) VALUES(1, '나나나나', '2222');
INSERT INTO article(user_id, title, content) VALUES(2, '다다다다', '3333');

INSERT INTO member(email, password) VALUES('a@naver.com', '1111');
INSERT INTO member(email, password) VALUES('b@daum.net', '2222');
INSERT INTO member(email, password) VALUES('c@google.com', '3333');

INSERT INTO notice( title, content) VALUES( '공지사항Ⅰ', '1111');
INSERT INTO notice( title, content) VALUES( '공지사항Ⅱ', '2222');
INSERT INTO notice( title, content) VALUES( '공지사항Ⅲ', '3333');

-- article 테이블에 데이터 추가
INSERT INTO article(user_id, title, content) VALUES(1, '당신의 인생 영화는?', '댓글 고');
INSERT INTO article(user_id, title, content) VALUES(2, '당신의 소울 푸드는?', '댓글 고고');
INSERT INTO article(user_id, title, content) VALUES(2, '당신의 취미는?', '댓글 고고고');

-- 4번 게시글의 댓글 추가
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(4, 1, 'Park', '굿 윌 헌팅');
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(4, 1, 'Kim', '아이 엠 샘');
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(4, 2, 'Choi', '쇼생크 탈출');

-- 5번 게시글의 댓글 추가
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(5, 1, 'Park', '치킨');
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(5, 1, 'Kim', '샤브샤브');
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(5, 2, 'Choi', '초밥');

-- 6번 게시글의 댓글 추가
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(6, 1, 'Park', '조깅');
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(6, 1, 'Kim', '유튜브 시청');
INSERT INTO comment(article_id, user_id, nickname, body) VALUES(6, 2, 'Choi', '독서');

-- 먼저 기존 외래 키 제약 조건 삭제
ALTER TABLE COMMENT DROP CONSTRAINT FK5YX0UPHGJC6IK6HB82KKW501Y;

-- 그런 다음 ON DELETE CASCADE 옵션을 사용하여 외래 키 제약 조건 다시 생성
ALTER TABLE COMMENT ADD CONSTRAINT FK5YX0UPHGJC6IK6HB82KKW501Y
FOREIGN KEY (ARTICLE_ID)
REFERENCES ARTICLE(ID)
ON DELETE CASCADE;


-- 먼저 기존 외래 키 제약 조건 삭제
ALTER TABLE ARTICLE DROP CONSTRAINT FKRFT39PH5FXH0V43DWQTSKJPVJ;

-- 그런 다음 ON DELETE CASCADE 옵션을 사용하여 외래 키 제약 조건 다시 생성
ALTER TABLE ARTICLE ADD CONSTRAINT FKRFT39PH5FXH0V43DWQTSKJPVJ
FOREIGN KEY (USER_ID)
REFERENCES USER_MEMBER(ID)
ON DELETE CASCADE;