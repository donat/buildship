package org.eclipse.buildship.ui.workspace;

import org.eclipse.osgi.util.NLS;

public class WorkspaceMessages extends NLS {

    private static final String BUNDLE_NAME = "org.eclipse.buildship.ui.workspace.WorkspaceMessages"; //$NON-NLS-1$
    public static String Action_RefreshProjectAction_Text;
    public static String Action_RefreshProjectAction_Tooltip;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, WorkspaceMessages.class);
    }

    private WorkspaceMessages() {
    }
}
