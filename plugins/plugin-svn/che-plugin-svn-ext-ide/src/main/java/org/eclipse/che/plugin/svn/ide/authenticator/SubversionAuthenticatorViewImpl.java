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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.eclipse.che.ide.api.ProductInfoDataProvider;
import org.eclipse.che.ide.api.dialogs.CancelCallback;
import org.eclipse.che.ide.api.dialogs.ConfirmCallback;
import org.eclipse.che.ide.api.dialogs.DialogFactory;
import org.eclipse.che.plugin.svn.ide.SubversionExtensionLocalizationConstants;

/**
 * @author Roman Nikitenko
 */
public class SubversionAuthenticatorViewImpl implements SubversionAuthenticatorView {

    private DialogFactory              dialogFactory;
    private ActionDelegate             delegate;


    private CheckBox        isGenerateKeys;
    private SubversionExtensionLocalizationConstants locale;
    private DockLayoutPanel contentPanel;

    @Inject
    public SubversionAuthenticatorViewImpl(DialogFactory dialogFactory,
                                           SubversionExtensionLocalizationConstants locale,
                                           ProductInfoDataProvider productInfoDataProvider) {
        this.dialogFactory = dialogFactory;

        isGenerateKeys = new CheckBox(locale.subversionLabel());
        this.locale = locale;
        isGenerateKeys.setValue(true);

        contentPanel = new DockLayoutPanel(Style.Unit.PX);
        contentPanel.addNorth(new InlineHTML(productInfoDataProvider.getName()), 20);
        contentPanel.addNorth(isGenerateKeys, 20);
    }

    @Override
    public void showDialog() {
        isGenerateKeys.setValue(true);
        dialogFactory.createConfirmDialog(locale.cleanupTitle(),
                                          contentPanel,
                                          getConfirmCallback(),
                                          getCancelCallback()).show();
    }

    @Override
    public boolean isGenerateKeysSelected() {
        return isGenerateKeys.getValue();
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public Widget asWidget() {
        return contentPanel;
    }

    private ConfirmCallback getConfirmCallback() {
        return new ConfirmCallback() {
            @Override
            public void accepted() {
                delegate.onAccepted();
            }
        };
    }

    private CancelCallback getCancelCallback() {
        return new CancelCallback() {
            @Override
            public void cancelled() {
                delegate.onCancelled();
            }
        };
    }
}
