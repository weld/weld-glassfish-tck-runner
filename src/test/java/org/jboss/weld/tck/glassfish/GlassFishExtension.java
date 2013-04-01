package org.jboss.weld.tck.glassfish;

import org.jboss.arquillian.container.spi.client.container.DeploymentExceptionTransformer;
import org.jboss.arquillian.core.spi.LoadableExtension;
//import org.jboss.as.arquillian.container.ExceptionTransformer;

/**
 * Registers the exception transformer to properly identify deployment failures.
 *
 * @author J J Snyder (j.j.snyder@oracle.com)
 */
public class GlassFishExtension implements LoadableExtension {

    private static final String GLASSFISH_CLIENTUTILS_CLASS = "org.jboss.arquillian.container.glassfish.clientutils.GlassFishClientUtil";
   
    public void register(ExtensionBuilder builder) {

        if (Validate.classExists(GLASSFISH_CLIENTUTILS_CLASS)) {
            builder.service(DeploymentExceptionTransformer.class, GlassFishDeploymentExceptionTransformer.class);
//            builder.service(ExceptionTransformer.class, GlassFishDeploymentExceptionTransformer.class);
            // Override the default NOOP exception transformer
//            builder.override(DeploymentExceptionTransformer.class,
//                             ExceptionTransformer.class,
//                             GlassFishDeploymentExceptionTransformer.class);
            // Observe container start and check EE resources
            builder.observer(GlassFishResourceManager.class);
        }
    }

}
