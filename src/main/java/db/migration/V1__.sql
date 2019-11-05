CREATE SEQUENCE hibernate_sequence;

CREATE TABLE yun_dict
(
  id       BIGSERIAL  NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(255),
  code CHARACTER VARYING(255),
  namespace CHARACTER VARYING(255),
  namespacecode CHARACTER VARYING(255),
  remark CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);


CREATE TABLE yun_Level
(
  id       BIGSERIAL  NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(255),
  uid CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);

--level
insert into yun_level(name,uid,created,modified,status,enabled)VALUES (
  '系统管理员','64757d1a-d818-43c1-88a5-71751b4ce505',now(),now(),true,true
);
insert into yun_level(name,uid,created,modified,status,enabled)VALUES (
  '运营','5ad9d884-5c3d-4624-9473-247b0e61121a',now(),now(),true,true
);
insert into yun_level(name,uid,created,modified,status,enabled)VALUES (
  '普通用户','7ad9d884-7c3d-4624-8473-147b0e61121a',now(),now(),true,true
);

CREATE TABLE yun_member
(
  id       BIGSERIAL  NOT NULL PRIMARY KEY,
  username CHARACTER VARYING(255),
  nickname CHARACTER VARYING(255),
  email    CHARACTER VARYING(255),
  mobile   CHARACTER VARYING(255),
  password CHARACTER VARYING(255) NOT NULL,
  remark CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);

INSERT INTO yun_member(username, nickname, email, mobile, password, created, modified, status, enabled)
VALUES ('admin', '系统管理员', 'turingunion@163.com',
        '15854103759', '$2a$12$8tPpwP3Lm1j2iKwxzs7z4.Hl.pxixGxvTHRLcmMa/o6byjbcaCGW6', now(), now(), TRUE, TRUE);

INSERT INTO yun_member (username, nickname, email, mobile, password, created, modified, status, enabled)
VALUES (
  'staff', '运营', '1111111@163.com',
  '18539173133', '$2a$12$8tPpwP3Lm1j2iKwxzs7z4.Hl.pxixGxvTHRLcmMa/o6byjbcaCGW6', now(), now(), TRUE, TRUE
);

INSERT INTO yun_member (username, nickname, email, mobile, password, created, modified, status, enabled)
VALUES (
  'customer', '普通用户', '1111111@163.com',
  '18539173134', '$2a$12$8tPpwP3Lm1j2iKwxzs7z4.Hl.pxixGxvTHRLcmMa/o6byjbcaCGW6', now(), now(), TRUE, TRUE
);

CREATE TABLE yun_member_level(
  member_id BIGINT NOT NULL REFERENCES yun_member(id),
  level_id BIGINT NOT NULL REFERENCES yun_member (id)
);

insert into yun_member_level(member_id,level_id)VALUES (
  (select id from yun_member where username = 'admin'),
  (select id from yun_level where uid = '64757d1a-d818-43c1-88a5-71751b4ce505')
);

insert into yun_member_level(member_id,level_id)VALUES (
  (select id from yun_member where username = 'staff'),
  (select id from yun_level where uid = '5ad9d884-5c3d-4624-9473-247b0e61121a')
);

insert into yun_member_level(member_id,level_id)VALUES (
  (select id from yun_member where username = 'customer'),
  (select id from yun_level where uid = '7ad9d884-7c3d-4624-8473-147b0e61121a')
);

CREATE TABLE yun_reset_code(
  id BIGSERIAL NOT NULL PRIMARY KEY,
  member_id  BIGINT REFERENCES yun_member (id),
  code CHARACTER VARYING(255) NOT NULL ,
  valid boolean,
  processed boolean,
  token CHARACTER VARYING(255),
  created     TIMESTAMP WITHOUT TIME ZONE,
  status      BOOLEAN
);