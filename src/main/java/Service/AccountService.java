

package Service;
import DAO.AccountDAO;
import  Model.Account;



public class AccountService {
        
    private AccountDAO accountDAO;


    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;

    }

    public Account accountNewUser(String username, String password ){
         if(username==null || username.length()==0)
         {
            return null;
         }else if( password==null || password.length() <4){
            return null;
         }else if(accountDAO.usernameExistence(username)){
            return null;
         }
         return accountDAO.addAccount(username, password);

    }

    public Account loginValidation(String username, String password){
        return accountDAO.userLogin(username, password);
    }
}

