package model;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

/**
 * Created by mihkel on 4.07.2018.
 */
@Configuration
public class WebSecurityConfigurerAdapterWFT extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel()
                .requestMatchers(new RequestHeaderRequestMatcher("x-forwarded-proto", "https"))
                .requiresSecure();
    }
}
