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
package org.eclipse.che.plugin.docker.machine;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.core.util.FileCleaner;
import org.eclipse.che.api.workspace.server.WorkspaceProjectStorageCleaner;
import org.eclipse.che.plugin.docker.machine.node.WorkspaceFolderPathProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Local implementation {@link WorkspaceProjectStorageCleaner}.
 *
 * @author Alexander Andrienko
 */
@Singleton
public class LocalWorkspaceProjectStorageCleanerImpl implements WorkspaceProjectStorageCleaner {

    private static final Logger LOG = LoggerFactory.getLogger(LocalWorkspaceProjectStorageCleanerImpl.class);

    private final WorkspaceFolderPathProvider workspaceFolderPathProvider;

    @Inject
    public LocalWorkspaceProjectStorageCleanerImpl(WorkspaceFolderPathProvider workspaceFolderPathProvider) {
        this.workspaceFolderPathProvider = workspaceFolderPathProvider;
    }

    @Override
    public void remove(String workspaceId) {
        try {
            File workspaceStorage = new File(workspaceFolderPathProvider.getPath(workspaceId));
            if (workspaceStorage.exists()) {
                FileCleaner.addFile(workspaceStorage);
            }
        } catch (IOException e) {
            LOG.error("Failed to clean up workspace folder for workspace with id: {}. Cause: {}.", workspaceId, e.getMessage());
        }
    }
}
