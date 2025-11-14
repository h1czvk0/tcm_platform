-- ========================================
-- TCM Platform 数据库初始化脚本
-- 版本: 2.0 (修正版)
-- 日期: 2025-11-14
-- 说明: 修正了字段命名和数据类型问题
-- ========================================

DROP DATABASE IF EXISTS tcm_platform;
CREATE DATABASE tcm_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tcm_platform;

-- ========================================
-- 1. 管理员表
-- ========================================
CREATE TABLE administrator (
    admin_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    admin_name VARCHAR(50) NOT NULL UNIQUE COMMENT '管理员用户名',
    admin_password VARCHAR(255) NOT NULL COMMENT '管理员密码',
    admin_role VARCHAR(50) DEFAULT 'ADMIN' COMMENT '管理员角色',
    permission_level INT DEFAULT 1 COMMENT '权限等级',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ========================================
-- 2. 用户表
-- ========================================
CREATE TABLE user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar_url VARCHAR(500) COMMENT '头像URL',
    constitution_type VARCHAR(50) COMMENT '体质类型',
    health_status VARCHAR(50) COMMENT '健康状态',
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ========================================
-- 3. 中药表
-- ========================================
CREATE TABLE chinese_medicine (
    medicine_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '中药ID',
    medicine_name VARCHAR(50) NOT NULL COMMENT '中药名称',
    nature VARCHAR(20) COMMENT '性味（寒、热、温、凉、平）',
    flavor VARCHAR(50) COMMENT '味道',
    meridians VARCHAR(100) COMMENT '归经',
    functions TEXT COMMENT '功效',
    `usage` VARCHAR(200) COMMENT '用法用量',
    contraindications TEXT COMMENT '禁忌',
    image_url VARCHAR(500) COMMENT '中药图片URL',
    views_count INT DEFAULT 0 COMMENT '浏览次数',
    collection_count INT DEFAULT 0 COMMENT '收藏次数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='中药信息表';

-- ========================================
-- 4. 药膳表
-- ========================================
CREATE TABLE herbal_diet (
    diet_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '药膳ID',
    diet_name VARCHAR(100) NOT NULL COMMENT '药膳名称',
    description TEXT COMMENT '描述',
    ingredients TEXT COMMENT '食材',
    cooking_steps TEXT COMMENT '烹饪步骤',
    efficacy TEXT COMMENT '功效',
    suitable_crowd VARCHAR(200) COMMENT '适宜人群',
    unsuitable_crowd VARCHAR(200) COMMENT '不适宜人群',
    cooking_time INT COMMENT '烹饪时间（分钟）',
    difficulty_level VARCHAR(20) COMMENT '难度等级',
    season VARCHAR(50) COMMENT '适宜季节',
    image_url VARCHAR(500) COMMENT '图片URL',
    views_count INT DEFAULT 0 COMMENT '浏览次数',
    collection_count INT DEFAULT 0 COMMENT '收藏次数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='药膳表';

-- ========================================
-- 5. 知识题库表
-- ========================================
CREATE TABLE knowledge_question (
    question_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '题目ID',
    question_content TEXT NOT NULL COMMENT '题目内容',
    question_type VARCHAR(20) COMMENT '题目类型',
    category VARCHAR(50) COMMENT '分类',
    difficulty VARCHAR(20) COMMENT '难度',
    option_a VARCHAR(200) COMMENT '选项A',
    option_b VARCHAR(200) COMMENT '选项B',
    option_c VARCHAR(200) COMMENT '选项C',
    option_d VARCHAR(200) COMMENT '选项D',
    options TEXT COMMENT '选项（JSON格式，兼容旧数据）',
    correct_answer VARCHAR(500) COMMENT '正确答案',
    explanation TEXT COMMENT '解析',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识题库表';

-- ========================================
-- 6. 药膳收藏表
-- ========================================
CREATE TABLE diet_collection (
    collection_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    diet_id BIGINT NOT NULL COMMENT '药膳ID',
    collection_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE KEY uk_user_diet (user_id, diet_id),
    INDEX idx_user_id (user_id),
    INDEX idx_diet_id (diet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='药膳收藏表';

-- ========================================
-- 7. 中药收藏表
-- ========================================
CREATE TABLE medicine_collection (
    collection_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    medicine_id BIGINT NOT NULL COMMENT '中药ID',
    collection_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE KEY uk_user_medicine (user_id, medicine_id),
    INDEX idx_user_id (user_id),
    INDEX idx_medicine_id (medicine_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='中药收藏表';

-- ========================================
-- 初始化数据
-- ========================================

-- 插入管理员（密码：admin123）
INSERT INTO administrator (admin_name, admin_password, admin_role, permission_level, is_active) 
VALUES ('admin', MD5(CONCAT('admin123', 'tcm_platform_salt_2024')), 'SUPER_ADMIN', 10, TRUE);

-- 插入中药数据
INSERT INTO chinese_medicine (medicine_name, nature, flavor, meridians, functions, `usage`, contraindications) VALUES
('人参', '温', '甘、微苦', '脾、肺、心经', '大补元气，复脉固脱，补脾益肺，生津养血，安神益智', '煎服，3-9g', '实证、热证禁服'),
('黄芪', '温', '甘', '脾、肺经', '补气升阳，固表止汗，利水消肿，托毒排脓', '煎服，9-30g', '表实邪盛者慎用'),
('当归', '温', '甘、辛', '肝、心、脾经', '补血活血，调经止痛，润肠通便', '煎服，6-12g', '湿盛中满者慎用'),
('枸杞子', '平', '甘', '肝、肾经', '滋补肝肾，益精明目', '煎服，6-12g', '脾虚便溏者慎用'),
('甘草', '平', '甘', '心、肺、脾、胃经', '补脾益气，清热解毒，祛痰止咳，缓急止痛，调和诸药', '煎服，2-10g', '不宜与京大戟、芫花、甘遂同用');

-- 插入药膳数据
INSERT INTO herbal_diet (diet_name, description, efficacy, cooking_time, difficulty_level, season) VALUES
('当归生姜羊肉汤', '温补气血的经典药膳', '温中补血，祛寒止痛', 120, 'EASY', '冬季'),
('四物汤', '补血调经的经典方剂', '补血调经，适用于血虚', 40, 'EASY', '四季'),
('枸杞炖鸡', '滋补肝肾，益精明目', '补虚益精，温中健脾', 120, 'MEDIUM', '四季');

-- 插入题库数据
INSERT INTO knowledge_question (question_content, question_type, category, difficulty, option_a, option_b, option_c, option_d, correct_answer) VALUES
('人参的主要功效是什么？', 'SINGLE_CHOICE', '中药知识', 'EASY', '清热解毒', '大补元气', '活血化瘀', '利水渗湿', 'B'),
('以下哪个季节最适合食用当归生姜羊肉汤？', 'SINGLE_CHOICE', '药膳养生', 'EASY', '春季', '夏季', '秋季', '冬季', 'D'),
('中医体质分为几种类型？', 'SINGLE_CHOICE', '体质辨识', 'MEDIUM', '7种', '8种', '9种', '10种', 'C'),
('黄芪的性味是什么？', 'SINGLE_CHOICE', '中药知识', 'MEDIUM', '寒凉', '温热', '甘温', '苦寒', 'C');

-- ========================================
-- 创建索引
-- ========================================
CREATE INDEX idx_medicine_name ON chinese_medicine(medicine_name);
CREATE INDEX idx_diet_name ON herbal_diet(diet_name);
CREATE INDEX idx_question_category ON knowledge_question(category);
CREATE INDEX idx_question_difficulty ON knowledge_question(difficulty);
CREATE INDEX idx_user_username ON user(username);

-- ========================================
-- 验证数据
-- ========================================
SELECT '========== 数据初始化完成 ==========' AS info;
SELECT CONCAT('管理员数量: ', COUNT(*)) AS info FROM administrator;
SELECT CONCAT('中药数量: ', COUNT(*)) AS info FROM chinese_medicine;
SELECT CONCAT('药膳数量: ', COUNT(*)) AS info FROM herbal_diet;
SELECT CONCAT('题目数量: ', COUNT(*)) AS info FROM knowledge_question;
SELECT '=====================================' AS info;
