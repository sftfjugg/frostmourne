
## 实现简易示意图

<img src="https://gitee.com/tim_guai/frostmourne/raw/master/doc/img/alarm_design.png" />

## 项目结构

* `frostmourne-vue`：前端项目，使用`vue-element-template`实现，打包时会把生成的资源文件构建到`frostmourne-monitor`

* `frostmourne-monitor`：监控运行主体服务

## 主要技术栈

* `springboot 2.x`
* `element ui`
* `vue-admin-template`
* `xxl-job`
* `mybatis`
* `freemarker`
* `elasticsearch`
* `InfluxDB`
* `jjwt`
* `nashorn`

## ORM选型说明

项目的数据库`ORM`使用的是`mybatis`，一直以来`mybatis`的`xml-sql`深受国内开发欢迎，因为
它非常的灵活，能比较好的应付快速多变的的迭代需求。但是缺点也比较明显。

* 对于一些简单的查询来说，`xml`定义过于繁琐
* 太灵活了，稍不注意`sql`就会写得很复杂，后面维护艰难

mybatis最新推出了新的模块 [mybatis-dynamic-sql](https://github.com/mybatis/mybatis-dynamic-sql) ，代码即`sql`
的查询，大部分基于生成的代码可以直接写代码完成，不需要写任何`sql`。

为了兼顾方便和灵活，我同时在项目里引入了`mybatis-dynamic-sql`和`xml-sql`两种方式，让他们互补配合一起完成数据访问。
大部分(90%以上)查询直接用`mybatis-dynamic-sql`，对于一些少数需要灵活的稍复杂场景使用`xml-sql`来完成。既提高了
编码效率，又保留了原来的灵活强大的`xml-sql`。我们只需要按需选择使用。
