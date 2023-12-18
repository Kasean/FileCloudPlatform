@Configuration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    public KeycloakConfig.Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9990/auth")
                .realm("master")
                .username("admin")
                .password("admin")
                .clientId("your-client-id")
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/super_admin/**").hasRole("SUPERADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();
    }
}
