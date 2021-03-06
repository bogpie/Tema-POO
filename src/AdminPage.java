import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class AdminPage extends DefaultPanel
{

    public AdminPage()
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        JTree tree = new JTree(root);
        DefaultMutableTreeNode companiesNode = new DefaultMutableTreeNode("Companies");
        DefaultMutableTreeNode usersNode = new DefaultMutableTreeNode("Users");
        root.add(companiesNode);
        root.add(usersNode);

        ArrayList<Company> companies = Application.getInstance().getCompanies();
        for (Company company : companies)
        {
            DefaultMutableTreeNode companyNode = new DefaultMutableTreeNode(company.getCompanyName());
            companiesNode.add(companyNode);
            for (Department department : company.getDepartments())
            {
                String classType = department.getClass().toString();
                String departmentName = classType.split(" ")[1];
                DefaultMutableTreeNode departmentNode = new DefaultMutableTreeNode(departmentName);
                companyNode.add(departmentNode);

                DefaultMutableTreeNode employeesNode = new DefaultMutableTreeNode("Employees");
                departmentNode.add(employeesNode);
                for (Employee employee : department.getEmployees())
                {
                    DefaultMutableTreeNode employeeNode = new DefaultMutableTreeNode(employee.getName());
                    employeesNode.add(employeeNode);
                }

                DefaultMutableTreeNode jobsNode = new DefaultMutableTreeNode("Jobs");
                departmentNode.add(jobsNode);
                for (Job job : department.getJobs())
                {
                    DefaultMutableTreeNode jobNode = new DefaultMutableTreeNode(job);
                    // + "- is open: " + job.getOpen());
                    if (job.getNoPositions() != 0)
                        jobsNode.add(jobNode);
                }

                DefaultMutableTreeNode budgetNode = new DefaultMutableTreeNode("Salary Budget: "
                        + department.getTotalSalaryBudget());
                departmentNode.add(budgetNode);
            }

        }

        ArrayList<User> users = Application.getInstance().getUsers();
        for (User user : users)
        {
            DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user.getName());
            usersNode.add(userNode);

        }
        add(tree);

        //show();
    }
}
