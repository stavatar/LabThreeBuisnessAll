package com.config.Security;


import com.Security.Filters.JaasStartFilter;

import com.Security.Jaas.JaasAuthorityGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.jaasapi.JaasApiIntegrationFilter;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.HashMap;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private JaasAuthorityGranter jaasAuthorityGranter;
    @Autowired
    private JaasStartFilter jwtFilter;


        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
        }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        JaasApiIntegrationFilter jaasApiIntegrationFilter=new JaasApiIntegrationFilter();
        jaasApiIntegrationFilter.setCreateEmptySubject(true);
                http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorize -> authorize.mvcMatchers("/user/**","/register/", "/auth/").permitAll())
                .authorizeRequests()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtFilter, AnonymousAuthenticationFilter.class);



    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    DefaultJaasAuthenticationProvider jaasAuthenticationProvider() {
        AppConfigurationEntry appConfig = new
                AppConfigurationEntry("com.Security.Jaas.JaasLoginModule",
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, new HashMap());

        InMemoryConfiguration memoryConfig = new InMemoryConfiguration(new
                AppConfigurationEntry[] { appConfig });

        DefaultJaasAuthenticationProvider def = new DefaultJaasAuthenticationProvider();
        def.setConfiguration(memoryConfig);
        def.setAuthorityGranters(new AuthorityGranter[] {jaasAuthorityGranter});


        return def;
    }
    //We are configuring jaasAuthenticationProvider as our global AuthenticationProvider
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jaasAuthenticationProvider());
    }
}
