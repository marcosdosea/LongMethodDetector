package br.ufs.smelldetector.views;

import org.designroleminer.smelldetector.model.DadosMetodoSmell;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import br.ufs.smelldetector.marker.MarkerFactory;
import br.ufs.smelldetector.model.ProviderModel;

public class MetodoLongoView extends ViewPart {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "br.ufs.smelldetector.views.MetodoLongoView";

	private TableViewer viewer;
	private MetodoLongoComparator comparator;

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		/*if (MetodoLongoProviderModel.INSTANCE.metodosLongos == null) {
			new AtualizadorInformacoesMetodoLongo().refreshAll();
		}*/
	}

	public void createPartControl(Composite parent) {
		createViewer(parent);
		// set the sorter for the table
		comparator = new MetodoLongoComparator();
		viewer.setComparator(comparator);
		insertActionTable();
	}

	public void insertActionTable() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				DadosMetodoSmell linha = (DadosMetodoSmell) selection.getFirstElement();
				String localWorkspace = MarkerFactory.alterarDireotioAbsolutoPorWorkspace(
						linha.getDiretorioDaClasse());
				IFile file = ResourcesPlugin.getWorkspace().getRoot()
						.getFile(new Path(localWorkspace));
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage();
				if (page != null) {
					try {
						IMarker marker = MarkerFactory.marcadorOpenEditor(file, linha.getLinhaInicial());
						IDE.openEditor(page, marker);
						marker.delete();
					} catch (CoreException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("N�o foi poss�vel abrir a classe. "
							+ "Erro ao tentar obter o ActiveWorkbenchPage");
				}
			}
		});
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		createColumns(parent, viewer);
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		// get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setInput(ProviderModel.INSTANCE.metodosSmell.values());
		// make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// define layout for the viewer
		viewer.getControl().setLayoutData(layoutViewer());
	}

	private GridData layoutViewer() {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		return gridData;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	// create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Code", "Approaches", "Class", "#Methods", "Method",  "LOC", "CC", "Efferent", "NOP", "Type"};
		int[] bounds = { 50, 80, 250, 70, 150, 50, 50, 50, 50, 300 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getCodigoMetodo();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getListaTecnicas().toString().replace('[', ' ').replace(']', ' ');
			}
		});
		
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getNomeClasse();
			}
		});
		
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getTotalMetodosClasse()+"";
			}
		});
			
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getNomeMetodo();
			}
		});

		

		col = createTableViewerColumn(titles[5], bounds[5], 5);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getLinesOfCode()+"";
			}
		});
		
		col = createTableViewerColumn(titles[6], bounds[6], 6);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getComplexity() +"";
			}
		});
		
		col = createTableViewerColumn(titles[7], bounds[7], 7);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getEfferent() + "";
			}
		});
		
		col = createTableViewerColumn(titles[8], bounds[8], 8);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getNumberOfParameters() + "";
			}
		});
		
		
		
		col = createTableViewerColumn(titles[9], bounds[9], 9);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosMetodoSmell p = (DadosMetodoSmell) element;
				return p.getSmell();
			}
		});
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;
	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column,
			final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
