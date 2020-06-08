# SWEDashboard 周报管理系统

## 功能

### 周报模块
同学可以在系统提供的模板上查看、编辑自己的周报；为了优化撰写周报的体验，系统可以拉取最近一周的报告内容作为对照。

### 组织及成员管理模块
PI 可以增删查改小组及管理相应小组的成员。 

### 通知模块：
系统在后台维护了三个定时任务来支持通知功能。在周一早七点的时候给所有同学发送生成本周的周报草稿通知邮件； 在周日晚七点的时候会给未及时完成周报的同学发通知邮件； 在周一早八点半的时候会给PI推送上周组内周报的统计信息(未完成人数及名单)。目前仅支持邮件推送。

## 技术选型

基于 Spring Boot + Mybatis + Mybatis Plus搭建的平台。

以 Spring Boot 为基础框架， Mybatis plus为数据访问层, Apache Shiro 为权限授权层， Ehcahe 对常用数据进行缓存，基于 Bootstrap 构建的 Admin LTE 作为前端框架。

1.后端
核心框架：Spring Boot
安全框架：Apache Shiro
视图框架：Spring MVC
服务端验证：Hibernate Validator
任务调度：Quartz
持久层框架：Mybatis、Mybatis plus
数据库连接池：Alibaba Druid
缓存框架：Ehcache
日志管理：SLF4J、Log4j
工具类：Apache Commons、Jackson、Xstream

2.前端
JS框架：jQuery
CSS框架：Twitter Bootstrap
数据表格：bootstrap table
对话框：layer
树结构控件：jQuery zTree
日期控件： datepicker 
