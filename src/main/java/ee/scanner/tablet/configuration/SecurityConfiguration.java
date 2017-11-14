package ee.scanner.tablet.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Setup admin username and password and URL's which do not need authentication.
 * <p>
 * Setup login and logout URLs and logout authentication clearing.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.username}")
    private String adminUsername;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/public/", "/", "/exists").permitAll().anyRequest().authenticated();
        http.formLogin().loginPage("/login").defaultSuccessUrl("/", true).permitAll();
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true).logoutSuccessUrl("/login").permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(adminUsername).password(adminPassword).roles("ADMIN");
    }
}