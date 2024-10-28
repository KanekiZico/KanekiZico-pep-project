package Service;

import Model.Account;

import java.sql.SQLException;

import DAO.AccountDAO;


public class AccountService {
    private AccountDAO accountDAO;

    public AccountService()
    {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO)
    {
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account) throws SQLException {
        if(account.getUsername() == "")
        {
            System.out.println("Username should not be empty!");
            return null;
        }
        if(account.getPassword().length() < 4)
        {
            System.out.println("Password should at least have 4 characters!");
            return null;
        }
        if(this.accountDAO.getAccountByUsername(account.getUsername()) == null)
            return this.accountDAO.registerUser(account);
        return null;
    }

    public Account verifyUser(Account account) throws SQLException {
        if(account.getUsername() == "")
        {
            System.out.println("Username should not be empty!");
            return null;
        }
        if(account.getPassword().length() < 4)
        {
            System.out.println("Password should at least have 4 characters!");
            return null;
        }
        return this.accountDAO.verifyAccount(account);

    }
    
}
