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

package io.rchemist;

import io.rchemist.common.transformer.PlatformClassTransformHelper;
import io.rchemist.common.web.context.BeanConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * project : Rchemist Platform
 * <p>
 * Created by kunner
 **/
@EnableCaching
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@EnableJms
@SpringBootApplication(
    scanBasePackages = {"io.rchemist"},
    exclude = {  SecurityAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class,
        LiquibaseAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class,
        ElasticsearchRestClientAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class
    })
@Slf4j
public class ApiRestApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplicationBuilder()
        .sources(ApiRestApplication.class)
        .listeners(new ApplicationPidFileWriter("./api-rest.pid"))
        .build();

    PlatformClassTransformHelper.initializeTransformedClasses("io.rchemist");

    setDefaultProfile(application);

    application.run(args);

    BeanConfigurationService.conditionalExcludeBeans();

    log.info("Application started successfully");


  }

  /**
   * spring default profile setting - if null
   * @param application default application
   */
  public static void setDefaultProfile(SpringApplication application) {
    String profile = System.getProperty("spring.profiles.active");
    if(StringUtils.isEmpty(profile)) {
      application.setAdditionalProfiles("local");
    }
  }

}
