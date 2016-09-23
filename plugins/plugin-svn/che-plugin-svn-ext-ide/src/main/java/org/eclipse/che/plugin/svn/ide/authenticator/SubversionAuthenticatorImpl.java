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
package org.eclipse.che.plugin.svn.ide.authenticator;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import org.eclipse.che.api.promises.client.Operation;
import org.eclipse.che.api.promises.client.OperationException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.callback.AsyncPromiseHelper;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.oauth.OAuth2Authenticator;
import org.eclipse.che.ide.api.oauth.OAuth2AuthenticatorUrlProvider;
import org.eclipse.che.ide.rest.RestContext;
import org.eclipse.che.plugin.svn.ide.SubversionClientService;
import org.eclipse.che.security.oauth.JsOAuthWindow;
import org.eclipse.che.security.oauth.OAuthCallback;
import org.eclipse.che.security.oauth.OAuthStatus;

import javax.validation.constraints.NotNull;

/**
 * @author Roman Nikitenko
 */
public class SubversionAuthenticatorImpl implements OAuth2Authenticator, OAuthCallback, SubversionAuthenticatorViewImpl.ActionDelegate {
    public static final String SVN = "svn";

    AsyncCallback<OAuthStatus> callback;

    private final SubversionAuthenticatorView view;
    private final SubversionClientService     clientService;
    private final NotificationManager         notificationManager;

    private final String     baseUrl;
    private final AppContext appContext;
    private       String     authenticationUrl;

    @Inject
    public SubversionAuthenticatorImpl(SubversionAuthenticatorView view,
                                       @RestContext String baseUrl,
                                       NotificationManager notificationManager,
                                       SubversionClientService clientService,
                                       AppContext appContext) {
        this.view = view;
        this.clientService = clientService;
        this.view.setDelegate(this);
        this.baseUrl = baseUrl;
        this.notificationManager = notificationManager;
        this.appContext = appContext;
    }

    @Override
    public void authenticate(String authenticationUrl, @NotNull final AsyncCallback<OAuthStatus> callback) {
        this.authenticationUrl = authenticationUrl;
        this.callback = callback;
        view.showDialog();
    }

    @Override
    public Promise<OAuthStatus> authenticate(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;

        return AsyncPromiseHelper.createFromAsyncRequest(new AsyncPromiseHelper.RequestCall<OAuthStatus>() {
            @Override
            public void makeCall(AsyncCallback<OAuthStatus> callback) {
                SubversionAuthenticatorImpl.this.callback = callback;
                view.showDialog();
            }
        });
    }

    @Override
    public String getProviderName() {
        return SVN;
    }

    @Override
    public void onCancelled() {
        callback.onFailure(new Exception("Authorization request rejected by user."));
    }

    @Override
    public void onAccepted() {
        clientService.saveCredentials(authenticationUrl, "codenvy", "iBiockn0eqo").then(new Operation<Void>() {
            @Override
            public void apply(Void arg) throws OperationException {
                onAuthenticated(OAuthStatus.fromValue(3));
            }
        });
    }

    @Override
    public void onAuthenticated(OAuthStatus authStatus) {
        if (view.isGenerateKeysSelected()) {
            return;
        }
        callback.onSuccess(authStatus);
    }
}
