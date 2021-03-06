package org.eindopdracht.configuration;

import org.eindopdracht.resource.exception.UnauthenticatedHandler;
import org.eindopdracht.resource.exception.UserAccessDeniedHandler;
import org.eindopdracht.security.JWTFilter;
import org.eindopdracht.security.JWTProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 * The annotation @{@link EnableWebSecurity} triggers Spring to load the {@link WebSecurityConfiguration},
 * which for one thing exports the springSecurityFilterChainBean, which the filter registered by {@link SecurityConfig}
 * delegates to.
 * That filter looks for classes implementing {@link WebSecurityConfigurer}.
 * {@link WebSecurityConfigurerAdapter} is a subclass of {@link WebSecurityConfigurer} and thus this class is as well.
 * Then this class gets used for configuring the security of your application.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;

    @Autowired
    private JWTProvider jwtProvider;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Here we define what we want to use for password encoding.
     *
     * @return The desired encoder implementation.
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * To expose an UserDetailService in our application,
     * we can just override this method from the superclass.
     * It will then create an UserDetailsService based on the
     * AuthenticationManagerBuilder which we're configuring in
     * {@link #configure(AuthenticationManagerBuilder)}.
     * <p>
     * We can also create our own UserDetailService if we so desire,
     * to customize how users are retrieved!
     *
     * @return The UserDetailService
     * @throws Exception
     */
    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    /**
     * We create the JWTProvider bean, which is used for generating and verifying JWT's.
     *
     * @param userDetailsService For retrieving the user while creating the JWT.
     * @param secretKey          The phrase used to encode the JWT in such a way only we can produce it.
     * @return
     */
    @Bean
    public JWTProvider jwtProvider(UserDetailsService userDetailsService, @Value("${secret.key}") String secretKey) {
        return new JWTProvider(userDetailsService, secretKey);
    }

    /**
     * We're configuring the {@link AuthenticationManagerBuilder} to tell it where to get the users from.
     * By default, Spring expects certain tables to be present if you simpy use {@link AuthenticationManagerBuilder#jdbcAuthentication()}
     * but you can customize that by giving your own queries, as shown in the method.
     * You still need to make sure the query returns the right columns.
     * You can also implement your own {@link UserDetailsService} where you'll retrieve users instead.
     *
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        JdbcUserDetailsManager userDetailsService = auth.jdbcAuthentication()
                .dataSource(dataSource)
                .getUserDetailsService();

        // Spring requires USERNAME, PASSWORD and ENABLED
        userDetailsService
                .setUsersByUsernameQuery("SELECT name, password, enabled FROM user WHERE name = ?");

        // Here Spring requires USERNAME and ROLE
        userDetailsService
                .setAuthoritiesByUsernameQuery("SELECT name, role FROM user WHERE name = ?");
    }

    /**
     * We expose the AuthenticationManager to our application, so we can use it to authenticate login requests
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * In this method we get a {@link HttpSecurity} object we can use for securing the access to our application.
     * The previous ones were focused on the inner workings, but this one is for deciding what happens
     * when a client sends us a message.
     * <p>
     * It's important to know that the top most configuration should be more specific than the bottom configuration.
     * If I say the url /admin is accessible by admins only and then say all the URLS are open for everyone,
     * that is interpreted as "/admin is secured, but everything else is open for business".
     * However, if I were to do that the other way around, my rule for /admin would be ignored.
     *
     * @param http The object we configure our security in
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();

        http
                .authorizeRequests()
                .antMatchers("/signup/**").permitAll()
                .antMatchers(HttpMethod.GET, "/consultation/**").permitAll()
                .antMatchers(HttpMethod.GET, "/content/**").permitAll()
                .antMatchers(HttpMethod.GET, "/contenttype/**").permitAll()
                .antMatchers(HttpMethod.GET, "/event/**").permitAll()
                .antMatchers(HttpMethod.GET, "/globalsettings/**").permitAll()
                .antMatchers(HttpMethod.GET, "/powerpoint/**").permitAll()
                .antMatchers(HttpMethod.GET, "/role/**").permitAll()
                .antMatchers(HttpMethod.GET, "/rssfeed/**").permitAll()
                .antMatchers(HttpMethod.GET, "/schedule/**").permitAll()
                .antMatchers(HttpMethod.GET, "/useravailability/**").permitAll();

        // First we configure it to allow authentication and authorization in REST
        // This is just a helper method made by me to split it up
        disableAuthOnSwagger(enableRESTAuthentication(http))
                // Now let's say which requests we want to authorize
                .authorizeRequests()
                // Every single request needs to be authorized... with the exception of /authorization,
                // which was defined in enableRESTAuthentication(), which was earlier and thus trumps this configuration.
                .anyRequest()
                // We require every user to have the role USER (this translates to 'ROLE_USER' in the database
                // and in other places)
                .hasRole("USER")
                // Alright, we're done, let's move on to the next part using and()
                .and()
                // We're disabling defenses against Cross-Site Request Forgery,
                // as the browser is not responsible for adding authentication information to the request
                // which is wat the CSRF exploit relies on.
                .csrf()
                .disable();

    }

    /**
     * I separated the configuration for security in REST to simplify it.
     * In this method we enable logging in and configure it for a REST API.
     *
     * @param http The {@link HttpSecurity} object we can use for configuration
     * @return
     * @throws Exception
     */
    private HttpSecurity enableRESTAuthentication(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/authenticate")
                .permitAll()
                // Configuring the HttpSecurity object is done in steps, we just configured the formLogin,
                // now we're continuing to exception handling. To signal this, we need to use and(),
                // otherwise we'd still be trying to configure formLogin.
                .and()
                // What to do when it goes wrong?
                .exceptionHandling()
                // When the access is denied to a certain part of the application, use the given handler.
                .accessDeniedHandler(new UserAccessDeniedHandler())
                // When a user tries to reach an endpoint without a JWT, use the following handler.
                .authenticationEntryPoint(new UnauthenticatedHandler());


        // We need to register our JWTFilter. We register it before the UsernamePasswordAuthenticationFilter,
        // as that is part of the group of filters where Spring expects updates to the SecurityContextHolder
        http.addFilterBefore(new JWTFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

        // As it's a REST API, we don't want Spring remembering sessions for users. It should be stateless.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // We return it so we can chain more configuration
        return http;
    }

    private HttpSecurity disableAuthOnSwagger(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().mvcMatchers("/swagger-ui.html",
                        "swagger-resources/**",
                        "/webjars/springfox-swagger-ui/**",
                        "/v2/api-docs**",
                        "/",
                        "index.jsp")
                .permitAll();
        return httpSecurity;
    }

}






