DROP DATABASE IF EXISTS serverdb;
CREATE DATABASE serverdb DEFAULT CHARACTER SET utf8mb4;
USE gitlabdb;

-- ----------------------------
--  Table structure for tb_user
-- ----------------------------
DROP TABLE
    IF EXISTS tb_user;

CREATE TABLE tb_user
(
    id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
        COMMENT 'ID',
    account      VARCHAR(20)                           NOT NULL
        COMMENT '账号',
    name         VARCHAR(20)                           NOT NULL DEFAULT ''
        COMMENT '姓名',
    avatar       VARCHAR(64)                           NOT NULL DEFAULT ''
        COMMENT '头像',
    email        VARCHAR(128)                          NOT NULL DEFAULT ''
        COMMENT '电子邮箱',
    password     VARCHAR(64)                           NOT NULL DEFAULT ''
        COMMENT '密码',
    salt         VARCHAR(64)                           NOT NULL DEFAULT ''
        COMMENT '密码盐',
    ip_address   VARCHAR(20)                           NOT NULL DEFAULT ''
        COMMENT 'IP地址',
    is_deleted   TINYINT                               NOT NULL DEFAULT 0
        COMMENT '逻辑删除',
    created_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT '创建时间',
    updated_time TIMESTAMP                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        COMMENT '更新时间'
)
    COMMENT '用户表';
CREATE UNIQUE INDEX account_UNIQUE
    ON tb_user (account);

#====================初始数据====================#

-- ----------------------------
--  data for tb_user
-- ----------------------------
INSERT INTO tb_user
    (id, account, name, password, salt)
VALUES
    # 密码：admin
    (1, 'admin', '管理员', '30d74f79c9edaf704c1fc240f89a2225398d4652', 'b3123d00784defa8');
