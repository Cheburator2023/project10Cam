package app;

import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.application.impl.ServletProcessApplication;

@ProcessApplication("MLC APP")

public class MLCApplication extends ServletProcessApplication {
  // empty implementation
  @PostDeploy
  public void startFirstProcess(ProcessEngine processEngine) {
    createAdminUser(processEngine);
  }

  private void createAdminUser(ProcessEngine processEngine) {
    new AdminUser().createUsers(processEngine);
  }
}

