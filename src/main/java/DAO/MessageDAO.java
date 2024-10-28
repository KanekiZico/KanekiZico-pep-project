package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message createMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());

        preparedStatement.executeUpdate();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if(pkeyResultSet.next())
        {
            int generated_message_id = (int) pkeyResultSet.getLong(1);
            return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }

        return null;
    }

    public Message verifyUser(int posted_by, Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, posted_by);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next())
        {
            return message;
        }

        return null;
    }

    public List<Message> getAllMessages() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            Message message = new Message(rs.getInt("message_id"), 
                                        rs.getInt("posted_by"), 
                                        rs.getString("message_text"), 
                                        rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
        return messages;

    }

    public Message getMessageById(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            return new Message(rs.getInt("message_id"), 
                            rs.getInt("posted_by"), 
                            rs.getString("message_text"), 
                            rs.getLong("time_posted_epoch"));
        }
        return null;
    }

    public boolean deleteMessageById(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);
        int affected = preparedStatement.executeUpdate();
        if(affected > 0)
            return true;
        return false;
    }

    public boolean updateMessageById(int message_id, Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        // String sql = "INSERT INTO message (message_id, posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?, ?)";
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
       
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, message.getMessage_text());
        preparedStatement.setInt(2, message_id);
        int affected = preparedStatement.executeUpdate();
        if(affected > 0)
            return true;
        return false;

    }

    public List<Message> getAllMessagesByUser(int account_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account_id);
        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next())
        {
            Message m = new Message(rs.getInt("message_id"), 
                                    rs.getInt("posted_by"), 
                                    rs.getString("message_text"), 
                                    rs.getLong("time_posted_epoch"));
            messages.add(m);
        }
        return messages;
    }
    
}
