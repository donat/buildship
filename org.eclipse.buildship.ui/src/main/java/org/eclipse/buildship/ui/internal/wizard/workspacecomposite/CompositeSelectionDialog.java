package org.eclipse.buildship.ui.internal.wizard.workspacecomposite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class CompositeSelectionDialog extends AbstractCompositeDialog {


	private List<IWorkingSet> removedWorkingSets = new ArrayList<>();
	private List<IWorkingSet> addedWorkingSets = new ArrayList<>();
	private IWorkingSetManager manager = PlatformUI.getWorkbench().getWorkingSetManager();

	public CompositeSelectionDialog(Shell parentShell) {
		super(parentShell);
		this.setTitle(WorkspaceCompositeWizardMessages.Title_ConfigureGradleWorkspaceCompositeDialog);
	}

	@Override
	protected void createNewWorkspaceComposite() {
		WorkspaceCompositeCreationWizard wizard = new WorkspaceCompositeCreationWizard();
		WizardDialog dialog = new WizardDialog(this.getShell(), wizard);
		dialog.create();
		if (dialog.open() == Window.OK) {
			IWorkingSet workingSet= wizard.getComposite();
			addNewCreatedComposite(workingSet);
			this.manager.addWorkingSet(workingSet);
			this.addedWorkingSets.add(workingSet);
		}
	}

	@Override
	protected void editWorkspaceComposite() {
		IStructuredSelection selection = (IStructuredSelection) getSelectedComposites();
		String id = "org.eclipse.buildship.ui.wizard.workspaceComposite.creation";
        IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
        WorkspaceCompositeCreationWizard wizard = null;
        try {
            wizard = (WorkspaceCompositeCreationWizard) descriptor.createWizard();
        } catch (CoreException e) {
            e.printStackTrace();
        }

        wizard.init(PlatformUI.getWorkbench(), selection);
        WizardDialog wd = new WizardDialog(getShell(), wizard);
        wd.setTitle(wizard.getWindowTitle());
        wd.open();

//		PreferencesUtil.createPropertyDialogOn(this.getShell(), (IWorkingSet) selection.getFirstElement(),
//				"org.eclipse.buildship.ui.GradleCompositePage", null, null).open();
	}

	@Override
	protected void removeWorkspaceComposite() {
		IStructuredSelection selectedELements = (IStructuredSelection) getSelectedComposites();
		this.removedWorkingSets.addAll(selectedELements.toList());
		removeSelectedCompositesFromList(selectedELements);
	}

	@Override
	protected void okPressed() {
		if (!this.removedWorkingSets.isEmpty()) {
			for (Iterator<IWorkingSet> it = this.removedWorkingSets.iterator(); it.hasNext();) {
				IWorkingSet workingSet = it.next();
				this.manager.removeWorkingSet(workingSet);
				it.remove();
			}
		}
		this.addedWorkingSets.clear();
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		if (!this.addedWorkingSets.isEmpty()) {
			for (Iterator<IWorkingSet> it = this.addedWorkingSets.iterator(); it.hasNext();) {
				IWorkingSet workingSet = it.next();
				this.manager.removeWorkingSet(workingSet);
				it.remove();
			}
		}
		this.removedWorkingSets.clear();
		super.cancelPressed();
	}

}
