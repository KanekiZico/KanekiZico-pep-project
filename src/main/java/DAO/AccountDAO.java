package DAO;

import java.sql.*;


import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account registerUser(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES(?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        preparedStatement.executeUpdate();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if(pkeyResultSet.next())
        {
            int generated_account_id = (int) pkeyResultSet.getLong(1);
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
        }

        return null;
    }

    public Account getAccountByUsername(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            Account account = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"), 
                    rs.getString("password")
                    );
            return account;
        }
        return null;
    }

    public Account verifyAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            Account verified = new Account(
                rs.getInt("account_id"),
                rs.getString("username"), 
                rs.getString("password")
            );
            return verified;
        }
        return null;
    }
    
}
