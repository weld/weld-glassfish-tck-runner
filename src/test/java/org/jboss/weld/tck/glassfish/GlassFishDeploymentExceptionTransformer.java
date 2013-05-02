/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

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
