package com.ashwin.java.dropwizard;

import com.ashwin.java.dropwizard.repository.RedisUserRepository;
import com.ashwin.java.dropwizard.repository.UserRepository;
import com.ashwin.java.dropwizard.resources.GreetResource;
import com.ashwin.java.dropwizard.resources.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import redis.clients.jedis.JedisPool;

public class RedisDemoApplication extends Application<RedisDemoConfiguration> {
    public static void main(String[] args) throws Exception {
        new RedisDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "redis-demo";
    }

    @Override
    public void initialize(Bootstrap<RedisDemoConfiguration> bootstrap) {
        // Do nothing
    }

    public void run(RedisDemoConfiguration config, Environment environment) throws Exception {
        final JedisPool masterRedis = buildAndRegisterRedisPool(config.getMasterRedis(), environment, "master-redis");
        final JedisPool readRedis = buildAndRegisterRedisPool(config.getMasterRedis(), environment, "read-redis");

        final GreetResource greetResource = new GreetResource(config.getTemplate(), config.getDefaultName(), masterRedis, readRedis);
        environment.jersey().register(greetResource);

        final UserRepository userRepository = new RedisUserRepository(masterRedis, readRedis, new ObjectMapper());
        final UserResource userResource = new UserResource(userRepository);
        environment.jersey().register(userResource);
    }

    private JedisPool buildAndRegisterRedisPool(RedisConfiguration configuration, Environment environment, String name) {
        final ManagedJedisPool managedJedisPool = new ManagedJedisPool(configuration);
        environment.lifecycle().manage(managedJedisPool);
        //environment.healthChecks().register(name, new RedisHealthCheck(managedJedisPool.getJedisPool()));
        return managedJedisPool.getJedisPool();
    }
}
