
# 便捷快速使用redis-cluster
### 支持JDK,String,Jackson,FastJson四种序列化缓存对象的方式

使用方法如下：

pom.xml 依赖添加

    <dependency>
        <groupId>com.nice</groupId>
        <artifactId>nice-redis-cluster</artifactId>
        <version>1.0.1-RELEASE</version>
    </dependency>

xxx.properties 添加

#redis 可选配置<br/>
redis.minIdle=10<br/>
redis.maxIdle=50<br/>
redis.maxTotal=100<br/>
redis.maxWaitMillis=3000<br/>
redis.testOnBorrow=true<br/>
redis.maxRedirects=3<br/>
redis.password=password<br/>

#redis 必填配置<br/>
redis.nodes=ip:port1,ip:port2,ip:port3,ip:port4,ip:port5,ip:port6<br/>

添加完以上配置后，就可以在Spring对象中注入，使用redis template了，如下所示：

    @Autowired
	private RedisTemplateJdk redisTemplateJdk;

