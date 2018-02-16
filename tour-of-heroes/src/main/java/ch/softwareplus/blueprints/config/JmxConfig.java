package ch.softwareplus.blueprints.config;

import javax.management.MalformedObjectNameException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

/**
 * This configuration exposes JMX over RMI.
 *
 * @author Gökhan Demirkiyik
 */
@Configuration
@ConditionalOnExpression("${jmx.rmi.enabled:false}")
public class JmxConfig {

  @Value("${jmx.rmi.host:localhost}")
  private String rmiHost;

  @Value("${jmx.rmi.port:1099}")
  private Integer rmiPort;

  @Bean
  public RmiRegistryFactoryBean rmiRegistry() {
    final RmiRegistryFactoryBean rmiRegistryFactoryBean = new RmiRegistryFactoryBean();
    rmiRegistryFactoryBean.setPort(rmiPort);
    rmiRegistryFactoryBean.setAlwaysCreate(true);
    return rmiRegistryFactoryBean;
  }

  @Bean
  @DependsOn("rmiRegistry")
  public ConnectorServerFactoryBean connectorServerFactoryBean()
      throws MalformedObjectNameException {
    final ConnectorServerFactoryBean connectorServerFactoryBean = new ConnectorServerFactoryBean();
    connectorServerFactoryBean.setObjectName("connector:name=rmi");
    connectorServerFactoryBean.setServiceUrl(String.format(
        "service:jmx:rmi://%s:%s/jndi/rmi://%s:%s/jmxrmi", rmiHost, rmiPort, rmiHost, rmiPort));
    return connectorServerFactoryBean;
  }
}
