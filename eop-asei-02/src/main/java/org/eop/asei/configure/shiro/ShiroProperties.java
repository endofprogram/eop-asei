package org.eop.asei.configure.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lixinjie
 * @since 2018-12-06
 */
@ConfigurationProperties(prefix = "eopasei.shiro")
public class ShiroProperties {

	private Integer sessionTimeout;
	private String sessionRedisKeyPrefix;
	public Integer getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public String getSessionRedisKeyPrefix() {
		return sessionRedisKeyPrefix;
	}
	public void setSessionRedisKeyPrefix(String sessionRedisKeyPrefix) {
		this.sessionRedisKeyPrefix = sessionRedisKeyPrefix;
	}
	
}
