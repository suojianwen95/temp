-- 软件类别
CREATE TABLE yun_category(
  id BIGSERIAL  NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(255),

  remark CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);

--参数
insert into yun_category(id,name,created,modified,status,enabled)VALUES (
  1,'HIS系统',now(),now(),true,true
);
insert into yun_category(id,name,created,modified,status,enabled)VALUES (
  2,'LIS系统',now(),now(),true,true
);
insert into yun_category(id,name,created,modified,status,enabled)VALUES (
  3,'PACS系统',now(),now(),true,true
);
insert into yun_category(id,name,created,modified,status,enabled)VALUES (
  4,'体检系统',now(),now(),true,true
);
insert into yun_category(id,name,created,modified,status,enabled)VALUES (
  5,'EMR系统',now(),now(),true,true
);
insert into yun_category(id,name,created,modified,status,enabled)VALUES (
  6,'公卫系统',now(),now(),true,true
);

-- 参数设置
CREATE TABLE yun_option(
  id BIGSERIAL  NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(255),
  value CHARACTER VARYING(255),

  remark CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);

--参数
insert into yun_option(id,name,value,created,modified,status,enabled)VALUES (
  1,'附件存放路径','data',now(),now(),true,true
);

-- 软件信息
CREATE TABLE yun_soft(
  id BIGSERIAL  NOT NULL PRIMARY KEY,
  name CHARACTER VARYING(200) NOT NULL,
  summary CHARACTER VARYING(255) NOT NULL,
  content TEXT,
  attach CHARACTER VARYING(255),
  module CHARACTER VARYING(100),

  url CHARACTER VARYING(255),
  developer CHARACTER VARYING(100),
  site CHARACTER VARYING(255),
  version CHARACTER VARYING(100),
  platform CHARACTER VARYING(255),
  starNumber INTEGER,
  score INTEGER,
  contact CHARACTER VARYING(100),
  telphone CHARACTER VARYING(100),
  category_id BIGINT REFERENCES yun_category (id),
  member_id BIGINT REFERENCES yun_member (id),

  sequence INTEGER,
  remark CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);

-- 软件评论
CREATE TABLE yun_discuss(
  id BIGSERIAL  NOT NULL PRIMARY KEY,
  member_id  BIGINT REFERENCES yun_member (id),
  sotf_id  BIGINT REFERENCES yun_soft (id),

  content TEXT,

  remark CHARACTER VARYING(255),
  created  TIMESTAMP WITHOUT TIME ZONE,
  modified TIMESTAMP WITHOUT TIME ZONE,
  status   BOOLEAN,
  enabled  BOOLEAN
);
