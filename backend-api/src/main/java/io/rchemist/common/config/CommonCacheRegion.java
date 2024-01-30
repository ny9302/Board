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


/**
 * <p>
 * Project : Rchemist Commerce Platform
 * <p>
 * Created by kunner
 **/
public class CommonCacheRegion {

  public static class Common {
    public static final String CACHE = "common-cache";
    public static final String MENU_CACHE = "common-menu-cache";
    public static final String REPORT_CACHE = "common-report-cache";
    public static final String METADATA_CACHE = "common-metadata-cache";
    public static final String DASHBOARD_BOARD = "common-dashboard-board";
    public static final int MAX_ENTRIES = 1000;
    public static final int TTL = 60 * 60 * 24;
    public static final long TTL_ONE_HOUR = 60 * 60;
    public static final long TTL_ONE_DAY = 60 * 60 * 24;
    public static final long TTL_ONE_MONTH = 60 * 60 * 24 * 30;
  }

}
