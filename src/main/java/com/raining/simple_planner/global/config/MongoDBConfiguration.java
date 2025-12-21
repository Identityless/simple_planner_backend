package com.raining.simple_planner.global.config;

import java.util.List;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.raining.simple_planner.domain.user.constant.Role;

// @Configuration
public class MongoDBConfiguration {
    
    // @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(List.of(
            new Converter<String, Role>() {
                @Override
                public Role convert(String source) {
                    if (source == null) return null;
                    String s = source.trim();

                    // 먼저 enum 이름과 일치하는지 시도
                    try {
                        return Role.valueOf(s);
                    } catch (IllegalArgumentException ignored) {
                        // enum 이름이 아닌 경우, roleName 또는 description으로 매칭 시도
                        for (Role r : Role.values()) {
                            if (r.getRoleName().equalsIgnoreCase(s) || r.getDescription().equalsIgnoreCase(s)) {
                                return r;
                            }
                        }
                        // 매칭되는 역할이 없으면 null을 반환하여 변환 에러를 피함
                        return null;
                    }
                }
            },
            new Converter<Role, String>() {
                @Override
                public String convert(Role source) {
                    return (source == null) ? null : source.getRoleName();
                }
            }
        ));
    }
}
