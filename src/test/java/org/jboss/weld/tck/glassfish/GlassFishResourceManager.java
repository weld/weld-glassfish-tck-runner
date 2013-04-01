package org.jboss.weld.tck.glassfish;

import org.jboss.arquillian.container.glassfish.managed_3_1.GlassFishManagedContainerConfiguration;
import org.jboss.arquillian.container.glassfish.managed_3_1.GlassFishServerControl;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.event.StartContainer;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;

import java.util.List;

/**
 * @author <a href="mailto:j.j.snyder@oracle.com">JJ Snyder</a>
 */
public class GlassFishResourceManager {
    private boolean jmsResourcesExist = false;

    /**
     * Observe {@link org.jboss.arquillian.container.spi.event.StartContainer} and check/add required EE resources.
     *
     * @param eventContext
     */
    public void checkResources(@Observes EventContext<StartContainer> eventContext) {

        try {
            // First start the container
            eventContext.proceed();

            // Disabling this for now.  Must manually create the jms resources before running tck.
//            createJmsResources( eventContext );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    private void createJmsResources(EventContext<StartContainer> eventContext) throws Exception {
//        if ( ! jmsResourcesExist ) {
//            Container container = eventContext.getEvent().getContainer();
//            GlassFishManagedContainerConfiguration glassFishManagedContainerConfiguration = ( GlassFishManagedContainerConfiguration ) container.createDeployableConfiguration();
//
//            GlassFishServerControl glassFishServerControl = new GlassFishServerControl( glassFishManagedContainerConfiguration );
//            List<String> jmsResources = glassFishServerControl.getJMSResources();
//            if ( jmsResources.contains( "topic/test") && jmsResources.contains( "queue/test" ) ) {
//                jmsResourcesExist = true;
//            } else {
//                glassFishServerControl.createJMSResource( "javax.jms.Queue", "queue_test", "queue_test" );
//                glassFishServerControl.createJMSResource( "javax.jms.Topic", "topic_test", "topic_test" );
//            }
//        }
//    }
//
//    private void createJmsResources() {
//
//    }

}
