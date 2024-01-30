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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.index.layout.IndexLayoutStrategy;

/**
 * <p>
 * project : Rchemist Commerce Platform
 * <p>
 * Created by kunner
 **/
public class HibernateSearchIndexNameStrategy implements IndexLayoutStrategy {

  @Override
  public String createInitialElasticsearchIndexName(String hibernateSearchIndexName) {
    return getIndexName(hibernateSearchIndexName) + "-rems";
  }

  @Override
  public String createWriteAlias(String hibernateSearchIndexName) {
    return getIndexName(hibernateSearchIndexName) + "-write";
  }

  @Override
  public String createReadAlias(String hibernateSearchIndexName) {
    return getIndexName(hibernateSearchIndexName) + "-read";
  }

  private String getIndexName(String hibernateSearchIndexName) {

    String configIndexNamePrefix = System.getProperty("hibernate.search.backend.index_name_prefix");

    if (StringUtils.isEmpty(configIndexNamePrefix)) {
      return hibernateSearchIndexName;
    }

    String prefix = StringUtils.endsWith(configIndexNamePrefix, "-") ? configIndexNamePrefix : configIndexNamePrefix + "-";

    return prefix + hibernateSearchIndexName;

  }


}
