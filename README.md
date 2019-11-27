# java-ssm-authsystem

#### 介绍

项目用到了 mybaties、spring、vue、element的权限后台管理系统，项目没有做完

#### 用到的jar包

```
<properties>
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	<maven.compiler.source>1.8</maven.compiler.source>
	<maven.compiler.target>1.8</maven.compiler.target>

	<spring.version>4.3.10.RELEASE</spring.version>
	<druid.version>1.0.20</druid.version>
	<mybatis.version>3.4.0</mybatis.version>
	<mybatis-spring.version>1.3.0</mybatis-spring.version>
	<mysql-connector-java.version>5.1.30</mysql-connector-java.version>
	<lombok.version>1.16.20</lombok.version>
	<logback-core.version>1.1.8</logback-core.version>
	<jedis.version>2.8.1</jedis.version>
	<pagehelper.version>5.1.2</pagehelper.version>
</properties>
```
+ spring核心包
```
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-beans</artifactId>
	<version>${spring.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
	<version>${spring.version}</version>
</dependency>
```
+ springmvc + springweb
```
<!-- sprin mvc + spring web-->
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-web</artifactId>
	<version>${spring.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webmvc</artifactId>
	<version>${spring.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-jdbc</artifactId>
	<version>${spring.version}</version>
</dependency>
```
+ jsp 与 jstl 标签库
```
<!-- jsp api -->
<dependency>
	<groupId>org.apache.tomcat</groupId>
	<artifactId>jsp-api</artifactId>
	<version>6.0.36</version>
</dependency>
<!-- jsp标签库 -->
<dependency>
	<groupId>jstl</groupId>
	<artifactId>jstl</artifactId>
	<version>1.2</version>
</dependency>
```
+ mybaties包
```
<!-- mybaties -->
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis</artifactId>
	<version>${mybatis.version}</version>
</dependency>
<dependency>
	<groupId>org.mybatis</groupId>
	<artifactId>mybatis-spring</artifactId>
	<version>${mybatis-spring.version}</version>
</dependency>
```
+ druid 数据库 JDBC 与 mysql驱动
```
<!-- druid -->
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>${druid.version}</version>
</dependency>
<!-- mysql驱动 -->
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>${mysql-connector-java.version}</version>
</dependency>
```
+ lombok包（主要用于）
Lombok是一个Java库，能自动插入编辑器并构建工具，简化Java开发。通过添加注解的方式，不需要为类编写getter或eques方法，同时可以自动化日志变量
```
<!-- lombok -->
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>${lombok.version}</version>
</dependency>
```
+ logback、slf4j日志
```
<!-- logback -->
<dependency>
	<groupId>ch.qos.logback</groupId>
	<artifactId>logback-core</artifactId>
	<version>${logback-core.version}</version>
</dependency>
<dependency>
	<groupId>ch.qos.logback</groupId>
	<artifactId>logback-classic</artifactId>
	<version>${logback-core.version}</version>
</dependency>
<dependency>
	<groupId>org.slf4j</groupId>
	<artifactId>slf4j-api</artifactId>
	<version>1.7.22</version>
</dependency>
```
+ validation、hibernate参数验证
```
<!-- validator -->
<dependency>
	<groupId>javax.validation</groupId>
	<artifactId>validation-api</artifactId>
	<version>1.1.0.Final</version>
</dependency>
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-validator</artifactId>
	<version>5.2.4.Final</version>
</dependency>
```
+ jackson 包
用于ssm框架输出json数据，ObjectMapper还可以将一个json字符串与类对象进行转换，参考 util/JsonMapper.java
```
<!-- jackson 工具：将一个json字符串转化为一个类对象-->
<dependency>
	<groupId>org.codehaus.jackson</groupId>
	<artifactId>jackson-core-asl</artifactId>
	<version>1.9.13</version>
</dependency>
<dependency>
	<groupId>org.codehaus.jackson</groupId>
	<artifactId>jackson-mapper-asl</artifactId>
	<version>1.9.13</version>
</dependency>
<!-- Jackson guava工具
  作用：
    一、将字符串分割 并转为List类型
        List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
    二、高级存储结构 ，Multimap
        Multimap实际存储结构为 key ,List<DeptLevelDto>,专门用来存储value为集合的类型
        level -> [dept1,dept2,....]
        以相同的key进行分组，levelDeptMap的value实际存储是一个DeptLevelDto类型的集合
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
    三、非空校验
        Preconditions.checkNotNull(sysAcl,"权限点为空");
        检验某个对象，某个集合是否为空，为空的时候系统会抛出异常，异常信息我们可以自定义，如上权限点为空就是异常产生时的message
-->
<dependency>
	<groupId>com.fasterxml.jackson.datatype</groupId>
	<artifactId>jackson-datatype-guava</artifactId>
	<version>2.5.3</version>
</dependency>
```
##### 工具
如：StringUtils、CollectionUtils、Preconditions
```
<!-- tools 集合工具-->
<dependency>
	<groupId>commons-collections</groupId>
	<artifactId>commons-collections</artifactId>
	<version>3.2.2</version>
</dependency>
<dependency>
	<groupId>commons-codec</groupId>
	<artifactId>commons-codec</artifactId>
	<version>1.10</version>
</dependency>
<!-- 工具包：如字符串处理 Splitter 
	List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
	List<Integer> strList = strList.stream().map(strItem -> Integer.parseInt(strItem)).collect(Collectors.toList());
-->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-lang3</artifactId>
	<version>3.5</version>
</dependency>
<!-- email -->
<dependency>
	<groupId>org.apache.commons</groupId>
	<artifactId>commons-email</artifactId>
	<version>1.4</version>
</dependency>
<!-- redis -->
<dependency>
	<groupId>redis.clients</groupId>
	<artifactId>jedis</artifactId>
	<version>${jedis.version}</version>
	<type>jar</type>
</dependency>
<!-- pageHelper分页 -->
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
	<version>${pagehelper.version}</version>
</dependency>
```