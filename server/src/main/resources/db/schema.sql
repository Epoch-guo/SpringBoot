-- 创建提交表
CREATE TABLE IF NOT EXISTS `submission` (
  `submission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提交ID',
  `contest_id` bigint(20) NOT NULL COMMENT '比赛ID',
  `student_id` bigint(20) NOT NULL COMMENT '学生ID',
  `content` text COMMENT '提交内容',
  `attachment_path` varchar(255) DEFAULT NULL COMMENT '附件路径',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态（0-待审核，1-已通过，2-已拒绝）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`submission_id`),
  KEY `idx_contest_id` (`contest_id`),
  KEY `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提交表';

-- 创建评分表
CREATE TABLE IF NOT EXISTS `score` (
  `score_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `submission_id` bigint(20) NOT NULL COMMENT '提交ID',
  `teacher_id` bigint(20) NOT NULL COMMENT '评分教师ID',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `comment` text COMMENT '评语',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`score_id`),
  UNIQUE KEY `idx_submission_id` (`submission_id`),
  KEY `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评分表'; 