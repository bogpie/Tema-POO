import Exceptions.InvalidDatesException;
import Exceptions.ResumeIncompleteException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Test
{


    public Consumer.Resume resume;

    public static void main(String[] args) throws FileNotFoundException, ResumeIncompleteException, InvalidDatesException
    {
        File input = new File(System.getProperty("user.dir") + "/arhiva/consumers.json");
        ParsedInfo parsedInfo = new ParsedInfo();

        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<Recruiter> recruiters = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Manager> managers = new ArrayList<>();

        parsedInfo.parseConsumers(input, employees, recruiters, users, managers);
        Application.getInstance().setUsers(users);
        Application.getInstance().setManagers(managers);
        Application.getInstance().setRecruiters(recruiters);
        Application.getInstance().setEmployees(employees);

        input = new File(System.getProperty("user.dir") + "/arhiva/companies.json");
        parsedInfo.parseCompanies(input);

        input = new File(System.getProperty("user.dir") + "/arhiva/users_codes.json");
        parsedInfo.parseUsersCodes(input);

        input = new File(System.getProperty("user.dir") + "/arhiva/graph_edges.txt");
        parsedInfo.parseGraphEdges(input);

        input = new File(System.getProperty("user.dir") + "/arhiva/jobs.json");
        parsedInfo.parseJobs(input);


        for(User user : Application.getInstance().getUsers())
        {
            for (String companyName : user.getInterestedCompanies())
            {
                Company company = Application.getInstance().getCompany(companyName);
                company.addObserver(user);
                ArrayList<Job> jobs = company.getJobs();
                for (Job job : jobs)
                {
                    job.apply(user);
                }
            }
        }


        for(Company company : Application.getInstance().getCompanies())
        {
            Manager manager = company.getManager();

            for(Job job : company.getJobs())
            {
                if(!job.getOpen()) continue;
                job.setOpen(false);
                manager.process(job);
            }
        }

        //Application.getInstance().getCompany("Amazon").getJobs().get(1).apply(Application.getInstance().getUsers().get(2));
        //Application.getInstance().getCompany("Google").getJobs().get(1).apply(Application.getInstance().getUsers().get(3));


        System.out.println(Application.getInstance().getCompany("Google").getDepartments().get(0));
        System.out.println();
        System.out.println();
        System.out.println(Application.getInstance().getCompany("Amazon").getDepartments().get(0));

    }

}
