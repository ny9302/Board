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

import io.rchemist.common.persistence.jpa.EntityManagerSetting;
import io.rchemist.common.persistence.jpa.multitenant.MultiTenantJpaRepositoryFactoryBean;
import io.rchemist.common.search.config.HibernateSearchMassIndexer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * <p>
 * Project : Rchemist Commerce Platform
 * <p>
 * Created by kunner
 **/
@Configuration
public class CommonEntityManagerSetting extends EntityManagerSetting {

  public static final String ENTITY_MANAGER = "entityManager";
  public static final String TRANSACTION_MANAGER = "transactionManager";
  public static final String SEARCH_INDEXER = "enfSearchIndexer";

  public static final String DATA_SOURCE_NAME = "common";

  public static final String[] ENTITY_PACKAGES = {"io.rchemist.**.**.domain.**"};

  @EnableJpaRepositories(basePackages = {"io.rchemist.**.**.dao.**"}
      , entityManagerFactoryRef = CommonEntityManagerSetting.ENTITY_MANAGER
      , repositoryFactoryBeanClass = MultiTenantJpaRepositoryFactoryBean.class
  )
  @Configuration
  public class CommonEntityManagerConfig{
    @Bean(name = DATA_SOURCE_NAME)
    public DataSource dataSource(){
      return getDataSource(DATA_SOURCE_NAME);
    }

    @Bean(name = ENTITY_MANAGER)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
      return getEntityManagerFactory(dataSource(), DATA_SOURCE_NAME, jpaProperties(DATA_SOURCE_NAME), ENTITY_PACKAGES);
    }

    @Bean(SEARCH_INDEXER)
    @DependsOn(ENTITY_MANAGER)
    public HibernateSearchMassIndexer commonSearchMassIndexer(){
      return new HibernateSearchMassIndexer() {

        @PersistenceContext(unitName = ENTITY_MANAGER)
        private EntityManager em;

        @Override
        public EntityManager getEntityManager() {
          return em;
        }
      };
    }

    @Bean(name = TRANSACTION_MANAGER)
    @Primary
    @ConditionalOnExpression("!${platform.config.datasource.provider.atomikos:false}")
    public PlatformTransactionManager transactionManager() {
      JpaTransactionManager transactionManager
          = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(
          entityManagerFactory().getObject());
      return transactionManager;
    }

  }


}
