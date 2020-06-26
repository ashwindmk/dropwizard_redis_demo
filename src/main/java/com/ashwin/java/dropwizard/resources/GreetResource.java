package com.ashwin.java.dropwizard.resources;

import com.ashwin.java.dropwizard.model.Greeting;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/greet")
@Produces(MediaType.APPLICATION_JSON)
public class GreetResource {
    private static Log LOG = LogFactory.getLog(GreetResource.class);

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    private JedisPool masterRedisPool, readRedisPool;

    public GreetResource(String template, String defaultName, JedisPool master, JedisPool read) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();

        masterRedisPool = master;
        readRedisPool = read;
    }

    @GET
    @Timed
    public Greeting sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        long id = getNextId();
        return new Greeting(id, value);
    }

    private long getNextId() {
        // Using AtomicLong
        //return counter.incrementAndGet();

        // Using Redis (Jedis)
        long prevId = getId();
        setId(prevId + 1L);
        return getId();
    }

    private long getId() {
        try (Jedis jedis = readRedisPool.getResource()) {
            String str = jedis.get("id");
            if (str == null) {
                str = "0";
            }
            return Long.parseLong(str);
        } catch (Exception e) {
            LOG.error("Unable to get id", e);
            throw new RuntimeException(e);
        }
    }

    private void setId(long id) {
        try (Jedis jedis = masterRedisPool.getResource()) {
            jedis.setex("id", 300, String.valueOf(id));
        } catch (Exception e) {
            LOG.error("Unable to set id", e);
        }
    }
}
