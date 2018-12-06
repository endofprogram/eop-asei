package org.eop.asei.configure.shiro;

import org.apache.shiro.realm.Realm;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixinjie
 * @since 2018-12-02
 */
@Configuration
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroConfiguration {

	private ShiroProperties shiroProperties;
	
	public ShiroConfiguration(ShiroProperties shiroProperties) {
		this.shiroProperties = shiroProperties;
	}
	
	public Realm realm() {
		return null;
	}
}
