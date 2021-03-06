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
package org.eclipse.che.plugin.languageserver.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.plugin.languageserver.server.DtoConverter;
import org.eclipse.che.plugin.languageserver.server.exception.LanguageServerException;
import org.eclipse.che.plugin.languageserver.server.registry.LanguageServerDescription;
import org.eclipse.che.plugin.languageserver.server.registry.LanguageServerRegistry;
import org.eclipse.che.plugin.languageserver.server.registry.LanguageServerRegistryImpl;
import org.eclipse.che.plugin.languageserver.shared.ProjectExtensionKey;
import org.eclipse.che.plugin.languageserver.shared.lsapi.InitializeResultDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.LanguageDescriptionDTO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.eclipse.che.dto.server.DtoFactory.newDto;
import static org.eclipse.che.plugin.languageserver.server.DtoConverter.asDto;

@Singleton
@Path("languageserver")
public class LanguageRegistryService {

	private final LanguageServerRegistry registry;

	@Inject
	public LanguageRegistryService(LanguageServerRegistry registry) {
		this.registry = registry;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("supported")
	public List<LanguageDescriptionDTO> getSupportedLanguages() {
		return registry.getSupportedLanguages()
					   .stream()
					   .map(DtoConverter::asDto)
					   .collect(toList());
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("registered")
	public List<InitializeResultDTO> getRegisteredLanguages() {
		return registry.getInitializedLanguages()
					   .entrySet()
					   .stream()
					   .map(entry -> {
						   ProjectExtensionKey projectExtensionKey = entry.getKey();
						   LanguageServerDescription serverDescription = entry.getValue();

						   List<LanguageDescriptionDTO> languageDescriptionDTOs
								   = Collections.singletonList(asDto(serverDescription.getLanguageDescription()));

						   InitializeResultDTO dto = newDto(InitializeResultDTO.class);
						   dto.setProject(projectExtensionKey.getProject().substring(LanguageServerRegistryImpl.PROJECT_FOLDER_PATH.length()));
						   dto.setSupportedLanguages(languageDescriptionDTOs);
						   dto.setCapabilities(asDto(serverDescription.getInitializeResult().getCapabilities()));
						   return dto;
					   })
					   .collect(toList());

	}

	@POST
    @Path("initialize")
	public void initialize(@QueryParam("path") String path) throws LanguageServerException {
        //in most cases starts new LS if not already started
        registry.findServer(TextDocumentService.prefixURI(path));
    }
}
