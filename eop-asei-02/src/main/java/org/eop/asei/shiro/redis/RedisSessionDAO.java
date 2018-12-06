package org.eop.asei.shiro.redis;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>基于redis的session存储
 * @author lixinjie
 * @since 2018-12-06
 */
public class RedisSessionDAO extends AbstractSessionDAO {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	private Integer sessionTimeout;
	private String sessionRedisKeyPrefix;
	
	public RedisSessionDAO(Integer sessionTimeout, String sessionRedisKeyPrefix) {
		super();
		this.sessionTimeout = sessionTimeout;
		this.sessionRedisKeyPrefix = sessionRedisKeyPrefix;
	}
	
	/**
	 * session是SessionFactory新创建的，它的Id是null，
	 * SessionDAO生成一个Id，赋值给session，并把session
	 * 持久化到redis中
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		storeSession(sessionId, session);
		return sessionId;
	}
	
	private void storeSession(Serializable id, Session session) {
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        redisTemplate.opsForValue().set(getSessionRedisKey(id), session,
        	sessionTimeout, TimeUnit.SECONDS);
    }
	
	private String getSessionRedisKey(Serializable id) {
		return sessionRedisKeyPrefix + id;
	}
	
	/**
	 * 从redis中获取session，如果存在则刷新过期时间
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = (Session)redisTemplate.opsForValue().get(getSessionRedisKey(sessionId));
		if (session != null) {
			redisTemplate.expire(getSessionRedisKey(sessionId), sessionTimeout, TimeUnit.SECONDS);
		}
		return session;
	}
	
	/**
	 * session超时或手动停止也会触发该更新方法，只有当session是合法时，
	 * 才会重新存储到redis中
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		if (((ValidatingSession)session).isValid()) {
			storeSession(session.getId(), session);
		}
	}

	/**
	 * 当session超时或手动停止时，从redis中删除它
	 */
	@Override
	public void delete(Session session) {
		redisTemplate.delete(getSessionRedisKey(session.getId()));
	}

	/**
	 * 定时任务周期性检查活动session是否超时，超时的将会被删除
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Session> getActiveSessions() {
		Set<Object> keys = redisTemplate.keys(getSessionRedisKey("*"));
		List<Session> sessions = (List<Session>)(Object)redisTemplate.opsForValue().multiGet(keys);
		return sessions;
	}

	
}
