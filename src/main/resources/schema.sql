DROP DATABASE IF EXISTS gitlab;
CREATE DATABASE gitlab DEFAULT CHARACTER SET utf8mb4;
USE gitlab;

-- ----------------------------
--  Table structure for user
-- ----------------------------
DROP TABLE
    IF EXISTS user;

CREATE TABLE user
(
    id                BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    username          VARCHAR(20)                           NOT NULL
        COMMENT '用户名',
    full_name         VARCHAR(20)                           NOT NULL
        COMMENT '全名',
    email             VARCHAR(128)                          NOT NULL
        COMMENT '电子邮箱',
    password          VARCHAR(64)                           NOT NULL
        COMMENT '密码',
    salt              VARCHAR(64)                           NOT NULL
        COMMENT '密码盐',
    sign_up_ip        VARCHAR(20)                           NOT NULL
        COMMENT '注册IP',
    sign_up_time      TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '注册时间',
    projects_limit    INTEGER                               NOT NULL DEFAULT 0
        COMMENT '项目限定',
    can_create_group  TINYINT                               NOT NULL DEFAULT 0
        COMMENT '是否可以创建组',
    access_level      VARCHAR(8)                            NOT NULL DEFAULT 'Regular'
        COMMENT '权限级别',
    avatar            VARCHAR(64)                           NOT NULL DEFAULT ''
        COMMENT '头像',
    last_sign_in_ip   VARCHAR(20)                           NOT NULL DEFAULT ''
        COMMENT '最后登录IP',
    last_sign_in_time TIMESTAMP                             NULL
        COMMENT '最后登录时间',
    is_deleted        TINYINT                               NOT NULL DEFAULT 0
        COMMENT '逻辑删除',
    created_time      TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time      TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '用户表';
CREATE UNIQUE INDEX username_UNIQUE
    ON user (username);
CREATE UNIQUE INDEX email_UNIQUE
    ON user (email);

-- ----------------------------
--  Table structure for sign_in_log
-- ----------------------------
DROP TABLE
    IF EXISTS sign_in_log;

CREATE TABLE sign_in_log
(
    id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    user_id      BIGINT(20)                            NOT NULL
        COMMENT '用户ID',
    sign_in_ip   VARCHAR(20)                           NOT NULL
        COMMENT '登录IP',
    sign_in_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '登录时间'
)
    COMMENT '登录日志表';

#====================初始数据====================#

-- ----------------------------
--  data for tb_user
-- ----------------------------
INSERT INTO user
(id, username, full_name, email, password, salt, sign_up_ip, projects_limit, can_create_group, access_level)
-- password: root2020
VALUES (1, 'root', '管理员', 'root@kangyonggan.com', 'f30fd033ee440ce9d47248531379b97526b3dfc8', 'fdee074755d53471',
        '127.0.0.1', 100, 1, 'Admin');
