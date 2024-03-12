

// # Requirements

// ## 1: Our API should be able to process new User registrations.

// As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an account_id.

// - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
// - If the registration is not successful, the response status should be 400. (Client error)

// ## 2: Our API should be able to process User logins.

// As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account, not containing an account_id. In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.

// - The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its account_id. The response status should be 200 OK, which is the default.
// - If the login is not successful, the response status should be 401. (Unauthorized)





package DAO;
import  Model.Account;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;


public class AccountDAO {

    public boolean usernameExistence(String username){
        Connection connection= ConnectionUtil.getConnection();

        try{
             String sql= "Select count(*) from account where username=? ";
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setString(1, username);

             ResultSet resultset = preparedStatement.executeQuery();
             if(resultset.next()){
                return resultset.getInt(1)>0;
             }

        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return false;
    }
   
  public Account addAccount(String username, String password){
    Connection connection= ConnectionUtil.getConnection();
    

     try{
            String sql= "Insert into account (username, password) values (?,?)";
            PreparedStatement preparedStatement= connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_accountId =  pkeyResultSet.getInt(1);
                return new Account(generated_accountId, username, password);

            }

     }catch(SQLException e)
     {
        System.out.println(e.getMessage());
     }

          return null;
  }


     public Account userLogin(String username, String password){
        Connection connection= ConnectionUtil.getConnection();


        try{
            String sql= "select * from account where username=? and password= ? ";
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultset= preparedStatement.executeQuery();

           
            if(resultset.next()){
               int accountId = resultset.getInt("account_id");
               String usernameLog= resultset.getString("username");
               String passwordLog= resultset.getString("password");

               return new Account(accountId, usernameLog, passwordLog);


            }

     }catch(SQLException e)
     {
        System.out.println(e.getMessage());
     }

          return null;
  }
     }

