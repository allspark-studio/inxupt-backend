-- ----------------------------
-- Table structure for administrator
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(25) NOT NULL DEFAULT 'admin' COMMENT '管理员昵称',
  `password` varchar(50) NOT NULL DEFAULT '123456' COMMENT '密码',
  `authority` varchar(20) NOT NULL DEFAULT 'ROLE_vip1' COMMENT '管理员权限等级',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `administrator_name_uindex` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='管理员信息表';

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '主标签',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for club
-- ----------------------------
DROP TABLE IF EXISTS `club`;
CREATE TABLE `club` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '关联的认证账号id',
  `college` varchar(50) DEFAULT NULL COMMENT '所属学院',
  `name` varchar(50) NOT NULL COMMENT '社团名字',
  `avatar` varchar(256) NOT NULL COMMENT '图标',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '类型  1社团  2实验室',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `body` text NOT NULL COMMENT '评论正文',
  `media_urls` varchar(2047) DEFAULT NULL COMMENT '图片或视频url',
  `author_id` int(11) NOT NULL COMMENT '评论者id',
  `post_id` int(11) NOT NULL COMMENT '文章id',
  `root_id` int(11) NOT NULL DEFAULT '0' COMMENT '一级评论id  0本身为一级评论',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父评论id  0本身为一级评论',
  `reply_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '父评论id 0为一级评论',
  `privately` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0非私密评论  1私密评论',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '评论状态  0可见  2作者删除  3违规删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `heat` int(11) NOT NULL DEFAULT '0' COMMENT '热度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7930 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for daily_statistics
-- ----------------------------
DROP TABLE IF EXISTS `daily_statistics`;
CREATE TABLE `daily_statistics` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` varchar(20) NOT NULL DEFAULT '' COMMENT '统计日期yyyy-MM-dd',
  `register_user_num` int(11) NOT NULL DEFAULT '0' COMMENT '当天注册用户量',
  `active_user_num` int(11) NOT NULL DEFAULT '0' COMMENT '当天活跃用户量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for event_remind
-- ----------------------------
DROP TABLE IF EXISTS `event_remind`;
CREATE TABLE `event_remind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action` int(1) NOT NULL COMMENT '事件类型  1点赞帖子 2点赞评论  3投币帖子 4投币评论  5收藏帖子  6评论帖子  7回复评论  8关注',
  `source_id` int(11) DEFAULT NULL COMMENT '事件源id，如评论id，文章id',
  `source_content` varchar(1023) DEFAULT NULL COMMENT '事件源的内容，如回复的内容，回复的评论',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '是否已读  0未读 1已读',
  `sender_id` int(11) NOT NULL COMMENT '操作者的id',
  `receive_id` int(11) NOT NULL,
  `remind_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reply_content` varchar(1023) DEFAULT NULL,
  `post_type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=57684 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `body` varchar(1023) NOT NULL COMMENT '意见反馈正文',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `followed_user_id` int(11) NOT NULL COMMENT '关注的人的id',
  `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未关注  1已关注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`followed_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1863 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户关注（多对多）表';

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '菜品名称',
  `price` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '价格',
  `image` varchar(255) DEFAULT NULL COMMENT '菜品图片',
  `description` varchar(255) DEFAULT NULL COMMENT '菜品描述',
  `like_num` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数量',
  `window_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=783 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `body` text COMMENT '帖子正文',
  `author_id` int(11) NOT NULL COMMENT '发帖人id',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '贴子状态  0可见  1违规删除  2作者删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布日期',
  `edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改日期',
  `type` int(1) NOT NULL DEFAULT '1' COMMENT '1帖子  2学术文章',
  `post_media_urls` varchar(2047) DEFAULT NULL COMMENT '帖子图片或视频url',
  `article_title` varchar(50) DEFAULT NULL COMMENT '文章标题，帖子默认为null',
  `article_text` text COMMENT '富文本的纯文本',
  `at_ids` varchar(255) DEFAULT NULL,
  `article_cover` varchar(255) DEFAULT NULL COMMENT '文章的封面',
  `tags` varchar(255) DEFAULT NULL COMMENT '自定义标签  用;分隔',
  `heat` int(11) NOT NULL DEFAULT '0' COMMENT '热度',
  `top_order` int(1) NOT NULL DEFAULT '0' COMMENT '置顶顺序，0为不置顶',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2311 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for post_category
-- ----------------------------
DROP TABLE IF EXISTS `post_category`;
CREATE TABLE `post_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`post_id`,`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2549 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '举报者的id',
  `type` int(1) NOT NULL DEFAULT '1' COMMENT '被举报类型  1帖子  2评论  3用户  4圈子',
  `item_id` int(11) NOT NULL,
  `item_author_id` int(11) NOT NULL COMMENT '被举报者id',
  `reasons` varchar(255) NOT NULL COMMENT '举报原因 用;分隔',
  `detail` varchar(1023) DEFAULT NULL COMMENT '举报详情  可以为空',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for restaurant
-- ----------------------------
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '餐厅名字',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for restaurant_swiper
-- ----------------------------
DROP TABLE IF EXISTS `restaurant_swiper`;
CREATE TABLE `restaurant_swiper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `restaurant_id` int(11) NOT NULL,
  `sort_order` int(1) NOT NULL DEFAULT '0',
  `img_url` varchar(255) NOT NULL COMMENT '图片url',
  `window_id` int(11) DEFAULT NULL COMMENT '对应的窗口的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for search_word
-- ----------------------------
DROP TABLE IF EXISTS `search_word`;
CREATE TABLE `search_word` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '关键字',
  `heat` int(11) NOT NULL DEFAULT '0' COMMENT '热度',
  `state` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for system_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) NOT NULL COMMENT '标题',
  `content` varchar(2047) NOT NULL COMMENT '内容',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '发送给哪些用户  0全部用户  1单用户  other其他',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '是否已拉取  0未拉取  1已拉取',
  `receive_id` int(11) NOT NULL COMMENT '如果type为单用户，该字段为通知的用户id，否则为0',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `nickname` varchar(20) NOT NULL COMMENT '用户昵称',
  `password` varchar(50) DEFAULT NULL,
  `wechat_openid` varchar(128) DEFAULT NULL COMMENT '微信小程序用户唯一标识 openid',
  `description` varchar(60) DEFAULT NULL COMMENT '个性描述',
  `gender` int(10) NOT NULL DEFAULT '0' COMMENT '用户性别  0保密  1男  2女',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  `major` varchar(20) DEFAULT NULL COMMENT '专业',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像图片地址',
  `level` int(5) NOT NULL DEFAULT '1' COMMENT '等级',
  `experience` int(11) NOT NULL DEFAULT '0' COMMENT '经验值',
  `register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户注册日期',
  `insertable_coins` int(11) NOT NULL DEFAULT '0' COMMENT '可投币硬币数量',
  `exchangeable_coins` int(11) NOT NULL DEFAULT '0' COMMENT '用户可兑换硬币数量',
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '用户状态  0正常  1禁言状态  2注销状态  3违规删除状态',
  `background_url` varchar(255) DEFAULT NULL COMMENT '用户展示页背景图片地址',
  `custom_app_color` varchar(10) DEFAULT NULL COMMENT 'app主色调，用户可自定义',
  `last_active_time` varchar(20) DEFAULT NULL COMMENT '用户最后活跃时间 yyyy-MM-dd HH:mm:ss',
  `like_num` int(11) NOT NULL DEFAULT '0' COMMENT '用户获赞数',
  `account_auth` varchar(256) DEFAULT '' COMMENT '账号认证id列表,使用分号分隔',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=836 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_comment_coin
-- ----------------------------
DROP TABLE IF EXISTS `user_comment_coin`;
CREATE TABLE `user_comment_coin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0未点赞  1已点赞',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`comment_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=483 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `user_comment_like`;
CREATE TABLE `user_comment_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0未点赞  1已点赞',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`comment_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6616 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_food_like
-- ----------------------------
DROP TABLE IF EXISTS `user_food_like`;
CREATE TABLE `user_food_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `food_id` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`food_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=229 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_post_coin
-- ----------------------------
DROP TABLE IF EXISTS `user_post_coin`;
CREATE TABLE `user_post_coin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0未点赞  1已点赞',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12573 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_post_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_post_favorite`;
CREATE TABLE `user_post_favorite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '0未点赞  1已点赞',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2010 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_post_like
-- ----------------------------
DROP TABLE IF EXISTS `user_post_like`;
CREATE TABLE `user_post_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '0未点赞  1已点赞',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_index` (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32393 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user_system_notice
-- ----------------------------
DROP TABLE IF EXISTS `user_system_notice`;
CREATE TABLE `user_system_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `notice_id` int(11) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0' COMMENT '是否已读  0未读  1已读',
  `pull_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '拉取时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for version
-- ----------------------------
DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(11) NOT NULL COMMENT '版本号 x.x.x',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '版本发布时间',
  `description` varchar(1023) NOT NULL DEFAULT '' COMMENT '版本描述',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT '新版本下载链接',
  `force` int(1) NOT NULL DEFAULT '0' COMMENT '是否强制更新',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for window
-- ----------------------------
DROP TABLE IF EXISTS `window`;
CREATE TABLE `window` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '窗口名字',
  `phone` varchar(20) DEFAULT NULL COMMENT '订餐电话',
  `restaurant_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
