public class IT extends Department
{

    public IT()
    {
        super();
    }

    @Override
    public double getTotalSalaryBudget()
    {
        double result = 0;
        for(Employee employee : getEmployees())
        {
            result += employee.getSalary();
        }
        return result;
    }
}
