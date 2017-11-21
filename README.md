
# 便捷快速使用redis-cluster
### 支持JDK,String,Jackson,FastJson四种序列化缓存对象的方式

支持Java 8

使用方法如下：

pom.xml 依赖添加

    <dependency>
        <groupId>com.nice</groupId>
        <artifactId>nice-redis-cluster</artifactId>
        <version>1.0.0-RELEASE</version>
    </dependency>

xxx.properties 添加

#redis settings
redis.minIdle=10
redis.maxIdle=50
redis.maxTotal=100
redis.maxWaitMillis=3000
redis.testOnBorrow=true
redis.password=password

#redis cluster
redis.nodes=ip:port1,ip:port2,ip:port3,ip:port4,ip:port5,ip:port6
redis.max-redirects=3

<br/>

添加完以上配置后，就可以在Spring对象中注入，使用redis template了，如下所示：

    @Autowired
	private RedisTemplateJdk redisTemplateJdk;


如果是web项目，需要用redis-cluster共享session，则添加如下配置

pom.xml 依赖添加

    <dependency>
        <groupId>org.springframework.session</groupId>
        <artifactId>spring-session</artifactId>
    </dependency>

applicationContext.xml 里添加配置

    <!-- 不启用事件通知机制 -->
	<util:constant static-field="org.springframework.session.data.redis.config.ConfigureRedisAction.NO_OP"/>
	<!-- spring session 管理 -->
	<bean class="org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration">
		<property name="maxInactiveIntervalInSeconds" value="7200"/>
	</bean>
