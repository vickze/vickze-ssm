package io.vickze.shiro.cache;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.vickze.util.JsonUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author vick.zeng
 * @email zengyukang@hey900.com
 * @date 2018-01-30 16:01
 */
@Component
public class RedisSessionDAO extends AbstractSessionDAO {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JedisPool jedisPool;

    private final String prefix = "shiro:session:";


    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        storeSession(sessionId, session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Jedis jedis = jedisPool.getResource();
        Session session = KryoSerializer.deserialize(jedis.get(genKey(sessionId)));
        jedis.close();
        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        Jedis jedis = jedisPool.getResource();

        byte[] key = genKey(session.getId());
        jedis.set(key, genValue(session));
        jedis.pexpire(key, session.getTimeout());

        jedis.close();
    }

    @Override
    public void delete(Session session) {
        if (session == null) {
            throw new NullPointerException("session argument cannot be null.");
        }
        Serializable id = session.getId();
        if (id != null) {
            Jedis jedis = jedisPool.getResource();
            jedis.del(genKey(session.getId()));
            jedis.close();
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> values = new HashSet<>();
        Jedis jedis = jedisPool.getResource();
        Set<String> keys = jedis.keys(prefix + "*");

        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                values.add(KryoSerializer.deserialize(jedis.get(key.getBytes())));
            }
            jedis.close();
            return Collections.unmodifiableCollection(values);
        } else {
            jedis.close();
            return Collections.emptySet();
        }
    }

    protected Session storeSession(Serializable id, Session session) {
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        Jedis jedis = jedisPool.getResource();
        byte[] key = genKey(id);
        if (!"OK".equals(jedis.set(key, genValue(session)))) {
            return null;
        }
        jedis.pexpire(key, session.getTimeout());
        jedis.close();
        return session;
    }

    private byte[] genValue(Session session) {
        return KryoSerializer.serialize(session);
    }

    private byte[] genKey(Serializable id) {
        return (prefix + id).getBytes();
    }
}
