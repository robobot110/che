/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.api.core.jdbc.jpa.event;

/**
 * @author Anton Korneta.
 */
public class RemovalContext {
    private Throwable cause;

    public Throwable cause() {
        return cause;
    }

    public boolean isFailure() {
        return cause != null;
    }

    public RemovalContext setCause(Throwable cause) {
        this.cause = cause;
        return this;
    }
}
