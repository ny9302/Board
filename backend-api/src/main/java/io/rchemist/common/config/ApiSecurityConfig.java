/*
 *
 *  *  Copyright (c) "2023". rchemist.io by Rchemist
 *  *  Licensed under the Rchemist Common License, Version 1.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License under controlled by Rchemist
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package io.rchemist.common.config;

import io.rchemist.common.security.auth.service.GatewayTokenAuthenticationService;
import io.rchemist.common.security.auth.token.ServiceSecurityCheckFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

/**
 * <p>
 * project : Rchemist Commerce Platform
 * <p>
 * Created by kunner
 **/
@Configuration
@EnableWebSecurity
public class ApiSecurityConfig {


  private final CorsFilter corsFilter;

  protected final GatewayTokenAuthenticationService gatewayTokenAuthenticationService;

  protected final PasswordEncoder passwordEncoder;

  protected final ServiceSecurityCheckFilter securityCheckFilter;

  public ApiSecurityConfig(CorsFilter corsFilter,
      GatewayTokenAuthenticationService gatewayTokenAuthenticationService,
      PasswordEncoder passwordEncoder) {
    this.corsFilter = corsFilter;
    this.gatewayTokenAuthenticationService = gatewayTokenAuthenticationService;
    this.passwordEncoder = passwordEncoder;
    this.securityCheckFilter = new ServiceSecurityCheckFilter(gatewayTokenAuthenticationService, passwordEncoder);
  }

  @Bean
  protected SecurityFilterChain config(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .addFilterBefore(corsFilter, LogoutFilter.class)
        .addFilterBefore(securityCheckFilter, UsernamePasswordAuthenticationFilter.class)
        .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(httpServletRequest -> {
          CorsConfiguration corsConfiguration = new CorsConfiguration();
          corsConfiguration.setAllowedOrigins(List.of("*"));
          corsConfiguration.setAllowedMethods(List.of("*"));
          corsConfiguration.setAllowedHeaders(List.of("*"));
          return corsConfiguration;
        }))
        .build();
  }


}
