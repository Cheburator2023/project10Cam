/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ACCESS;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;
import static org.camunda.bpm.engine.authorization.Permissions.READ;
import static org.camunda.bpm.engine.authorization.Permissions.UPDATE;
import static org.camunda.bpm.engine.authorization.Resources.APPLICATION;
import static org.camunda.bpm.engine.authorization.Resources.FILTER;
import static org.camunda.bpm.engine.authorization.Resources.TASK;
import static org.camunda.bpm.engine.authorization.Resources.USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.IdentityServiceImpl;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.task.TaskQuery;

/**
 * Creates demo credentials to be used in the invoice showcase.
 *
 * @author drobisch
 */
public class AdminUser {

    private final static Logger LOGGER = Logger.getLogger(AdminUser.class.getName());

    public void createUsers(ProcessEngine engine) {

      final IdentityServiceImpl identityService = (IdentityServiceImpl) engine.getIdentityService();

      if(identityService.isReadOnly()) {
        LOGGER.info("Identity service provider is Read Only, not creating any demo users.");
        return;
      }

      identityService.deleteUser("demo");
      identityService.deleteUser(System.getenv("CAMUNDA_ADMIN"));

      User singleResult = identityService.createUserQuery().userId("demo").singleResult();
      if (singleResult != null) {
        return;
      }

      LOGGER.info("Generating demo data for invoice showcase");

      User user = identityService.newUser(System.getenv("CAMUNDA_ADMIN"));
      user.setFirstName("admin");
      user.setLastName("admin");
      user.setPassword(System.getenv("CAMUNDA_PWD"));
      user.setEmail("demo@camunda.org");
      identityService.saveUser(user, true);

      final AuthorizationService authorizationService = engine.getAuthorizationService();

      // create group
      if(identityService.createGroupQuery().groupId(Groups.CAMUNDA_ADMIN).count() == 0) {
        Group camundaAdminGroup = identityService.newGroup(Groups.CAMUNDA_ADMIN);
        camundaAdminGroup.setName("camunda BPM Administrators");
        camundaAdminGroup.setType(Groups.GROUP_TYPE_SYSTEM);
        identityService.saveGroup(camundaAdminGroup);
      }

      // create ADMIN authorizations on all built-in resources
      for (Resource resource : Resources.values()) {
        if(authorizationService.createAuthorizationQuery().groupIdIn(Groups.CAMUNDA_ADMIN).resourceType(resource).resourceId(ANY).count() == 0) {
          AuthorizationEntity userAdminAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
          userAdminAuth.setGroupId(Groups.CAMUNDA_ADMIN);
          userAdminAuth.setResource(resource);
          userAdminAuth.setResourceId(ANY);
          userAdminAuth.addPermission(ALL);
          authorizationService.saveAuthorization(userAdminAuth);
        }
      }

      identityService.createMembership(System.getenv("CAMUNDA_ADMIN"), "camunda-admin");

    }
}
