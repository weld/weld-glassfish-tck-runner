package org.jboss.weld.tck.glassfish;

import java.util.List;

import javax.enterprise.inject.spi.DefinitionException;
import javax.enterprise.inject.spi.DeploymentException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jboss.arquillian.container.spi.client.container.DeploymentExceptionTransformer;

/**
 *
 * See AS7-1197 for more details.
 * 
 * @see GlassFishExtension
 * @author J J Snyder (j.j.snyder@oracle.com)
 */
public class GlassFishDeploymentExceptionTransformer implements DeploymentExceptionTransformer {

    private static final String[] DEPLOYMENT_EXCEPTION_FRAGMENTS = new String[] {
            "org.jboss.weld.exceptions.DeploymentException",
            "org.jboss.weld.exceptions.UnserializableDependencyException",
            "org.jboss.weld.exceptions.InconsistentSpecializationException",
            "CDI deployment failure:",
            "org.jboss.weld.exceptions.NullableDependencyException" };

    private static final String[] DEFINITION_EXCEPTION_FRAGMENTS = new String[]
            { "CDI definition failure:",
              "org.jboss.weld.exceptions.DefinitionException" };

    public Throwable transform(Throwable throwable) {

        // Arquillian sometimes returns InvocationException with nested AS7
        // exception and sometimes AS7 exception itself
        @SuppressWarnings("unchecked")
        List<Throwable> throwableList = ExceptionUtils.getThrowableList(throwable);
        if (throwableList.size() < 1)
            return throwable;

        Throwable root = null;

        if (throwableList.size() == 1) {
            root = throwable;
        } else {
            root = ExceptionUtils.getRootCause(throwable);
        }

        if (root instanceof DeploymentException || root instanceof DefinitionException) {
            return root;
        }
        if (isFragmentFound(DEPLOYMENT_EXCEPTION_FRAGMENTS, root)) {
            return new DeploymentException(root);
        }
        if (isFragmentFound(DEFINITION_EXCEPTION_FRAGMENTS, root)) {
            return new DefinitionException(root);
        }
        return throwable;
    }

    private boolean isFragmentFound(String[] fragments, Throwable rootException) {
        for (String fragment : fragments) {
            if (rootException.getMessage().contains(fragment)) {
                return true;
            }
        }
        return false;
    }

}
