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

-- ----------------------------
--  Table structure for email_template
-- ----------------------------
DROP TABLE
    IF EXISTS email_template;

CREATE TABLE email_template
(
    id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    code         VARCHAR(64)                           NOT NULL
        COMMENT '模板代码',
    name         VARCHAR(64)                           NOT NULL
        COMMENT '模板名称',
    template     LONGTEXT
        COMMENT '邮件模板',
    is_deleted   TINYINT                               NOT NULL DEFAULT 0
        COMMENT '逻辑删除',
    created_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '邮件模板表';
CREATE UNIQUE INDEX code_UNIQUE
    ON email_template (code);

-- ----------------------------
--  Table structure for email
-- ----------------------------
DROP TABLE
    IF EXISTS email;

CREATE TABLE email
(
    id            BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    template_code VARCHAR(64)                           NOT NULL
        COMMENT '模板代码',
    template_name VARCHAR(128)                          NOT NULL
        COMMENT '模板名称',
    subject       VARCHAR(128)                          NOT NULL DEFAULT ''
        COMMENT '标题',
    from_email    VARCHAR(128)                          NOT NULL DEFAULT ''
        COMMENT '发送方',
    to_email      VARCHAR(128)                          NOT NULL DEFAULT ''
        COMMENT '接收方',
    params        VARCHAR(1024)                         NOT NULL DEFAULT ''
        COMMENT '参数',
    content       LONGTEXT
        COMMENT '邮件内容',
    is_deleted    TINYINT                               NOT NULL DEFAULT 0
        COMMENT '逻辑删除',
    created_time  TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time  TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '邮件表';
CREATE INDEX ix_template_code
    ON email (template_code);

-- ----------------------------
--  Table structure for group
-- ----------------------------
DROP TABLE
    IF EXISTS `group`;

CREATE TABLE `group`
(
    id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    group_path       VARCHAR(20)                           NOT NULL
        COMMENT '组路径',
    group_name       VARCHAR(20)                           NOT NULL
        COMMENT '组名称',
    description      VARCHAR(256)                          NOT NULL DEFAULT ''
        COMMENT '描述',
    group_avatar     VARCHAR(64)                           NOT NULL
        COMMENT '组头像',
    visibility_level TINYINT                               NOT NULL
        COMMENT '可见级别',
    is_deleted       TINYINT                               NOT NULL DEFAULT 0
        COMMENT '逻辑删除',
    created_time     TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time     TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '组表';
CREATE UNIQUE INDEX group_path_UNIQUE
    ON `group` (group_path);

-- ----------------------------
--  Table structure for group_user
-- ----------------------------
DROP TABLE
    IF EXISTS group_user;

CREATE TABLE group_user
(
    id              BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    group_id        BIGINT(20)                            NOT NULL
        COMMENT '组ID',
    user_id         BIGINT(20)                            NOT NULL
        COMMENT '用户ID',
    access          TINYINT                               NOT NULL DEFAULT 0
        COMMENT '权限',
    expiration_date DATE                                  NULL
        COMMENT '失效日期',
    created_time    TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time    TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '组成员表';
CREATE UNIQUE INDEX group_user_UNIQUE
    ON group_user (group_id, user_id);
CREATE INDEX ix_user
    ON group_user (user_id);

-- ----------------------------
--  Table structure for project
-- ----------------------------
DROP TABLE
    IF EXISTS project;

CREATE TABLE project
(
    id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    project_path     VARCHAR(20)                           NOT NULL
        COMMENT '项目路径',
    project_name     VARCHAR(20)                           NOT NULL
        COMMENT '项目名称',
    description      VARCHAR(256)                          NOT NULL DEFAULT ''
        COMMENT '描述',
    namespace        VARCHAR(20)                           NOT NULL
        COMMENT '命名空间',
    visibility_level TINYINT                               NOT NULL
        COMMENT '可见级别',
    is_deleted       TINYINT                               NOT NULL DEFAULT 0
        COMMENT '逻辑删除',
    created_time     TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time     TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '项目表';
CREATE UNIQUE INDEX namespace_path_UNIQUE
    ON project (namespace, project_path);

-- ----------------------------
--  Table structure for project_user
-- ----------------------------
DROP TABLE
    IF EXISTS project_user;

CREATE TABLE project_user
(
    id              BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    project_id        BIGINT(20)                            NOT NULL
        COMMENT '项目ID',
    user_id         BIGINT(20)                            NOT NULL
        COMMENT '用户ID',
    access          TINYINT                               NOT NULL DEFAULT 0
        COMMENT '权限',
    expiration_date DATE                                  NULL
        COMMENT '失效日期',
    created_time    TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time    TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '项目成员表';
CREATE UNIQUE INDEX project_user_UNIQUE
    ON project_user (project_id, user_id);
CREATE INDEX ix_user
    ON project_user (user_id);

#====================初始数据====================#

-- ----------------------------
--  data for user
-- ----------------------------
INSERT INTO user
(username, full_name, email, password, salt, sign_up_ip, projects_limit, can_create_group, access_level)
-- password: root2020
VALUES ('root', 'Administrator', 'root@kangyonggan.com', 'f30fd033ee440ce9d47248531379b97526b3dfc8', 'fdee074755d53471',
        '127.0.0.1', 100, 1, 'Admin');

-- ----------------------------
--  data for email_template
-- ----------------------------
INSERT INTO email_template
    (code, name, template)
VALUES ('reset_password', 'Reset password', 'Reset password verification code is %s, expire in %s minutes.');

