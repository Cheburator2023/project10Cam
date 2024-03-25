FROM nexus-ci.corp.dev.vtb/sumd-docker-lib/camunda:1.0

# Add jdbc-driver

RUN mkdir -p /camunda/modules/com/oracle/ojdbc/main/
COPY ojdbc10-19.3.0.0.jar /camunda/modules/com/oracle/ojdbc/main/
COPY module.xml /camunda/modules/com/oracle/ojdbc/main/
COPY standalone.xml /camunda/standalone/configuration/
COPY camunda.sh /camunda/

# SUM application
RUN rm ./standalone/deployments/camunda-example*
COPY deployment/model.war /camunda/standalone/deployments

USER 0
RUN chgrp -R 0 /camunda && chmod -R g=u /camunda
USER 1000

ENV JAVA_OPTS "${JAVA_OPTS} -Doracle.net.tns_admin=/configuration/oracle"

# EXPOSE Ports

EXPOSE 8080
EXPOSE 9990
