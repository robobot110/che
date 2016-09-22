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
package org.eclipse.che.api.workspace.server;

/**
 * This component removes workspace folder from workspace storage. It's used for "delete workspace" operation
 * (see more {@link WorkspaceService#delete}).
 *
 * @author Alexander Andrienko
 */
public interface WorkspaceProjectStorageCleaner {

    /**
     * Removes workspace project folder with all data by {@code workspaceId}. Note: all user's projects will be deleted.
     *
     * @param workspaceId
     *         unique workspaceId identifier
     */
    void remove(String workspaceId);
}
