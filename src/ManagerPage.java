import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerPage extends DefaultPanel implements ActionListener, TreeSelectionListener
{
    private final Manager manager;
    private final JTree tree;
    private Request<Job, Consumer> selectedRequest = new Request<>();
    private DefaultMutableTreeNode selectedNode = new DefaultMutableTreeNode();

    public ManagerPage(Manager manager) throws HeadlessException
    {
        //super(manager.getName());
        this.manager = manager;

        JPanel grid = new JPanel(new GridLayout(2, 1));
        add(grid);

        JPanel requestsPanel = new JPanel();
        grid.add(requestsPanel);

        DefaultMutableTreeNode managerNode = new DefaultMutableTreeNode(manager);

        tree = new JTree(managerNode);
        requestsPanel.add(tree);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);

        for (Request<Job, Consumer> request : manager.getRequests())
        {
            DefaultMutableTreeNode requestNode = new DefaultMutableTreeNode(request);
            managerNode.add(requestNode);
        }

        JPanel actionsPanel = new JPanel();
        grid.add(actionsPanel);
        JButton acceptButton = new JButton("Accept");
        JButton rejectButton = new JButton("Reject");
        acceptButton.addActionListener(this);
        rejectButton.addActionListener(this);
        actionsPanel.add(acceptButton);
        actionsPanel.add(rejectButton);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof JButton && selectedRequest.getValue1() != null)
        {
            JButton button = ((JButton) e.getSource());
            Job job = selectedRequest.getKey();
            User user = ((User) selectedRequest.getValue1());

            if (button.getText().equals("Accept"))
            {
                if (selectedRequest.getKey().getNoPositions() == 0)
                {
                    JOptionPane.showMessageDialog(this,
                            "All the job positions have been filled!");
                    return;
                }
                if (!Application.getInstance().getUsers().contains(user))
                {
                    JOptionPane.showMessageDialog(this,
                            "User already employed!");
                    return;
                }
                manager.forceEmploy(job, user);
            }
            else
            {
                user.update(new Notification("Rejected from " + job.toString(),
                        "Reason: rejected by manager"));
            }

            manager.getRequests().remove(selectedRequest);
            DefaultTreeModel treeModel = ((DefaultTreeModel) tree.getModel());
            treeModel.removeNodeFromParent(selectedNode);

            selectedRequest = new Request<>();
            selectedNode = new DefaultMutableTreeNode();
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null) return;
        Object object = node.getUserObject();
        if (object.getClass() != selectedRequest.getClass()) return;
        selectedRequest = (Request<Job, Consumer>) object;
        selectedNode = node;
    }
}
