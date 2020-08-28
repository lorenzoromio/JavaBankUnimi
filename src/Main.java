

/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args)  {

//            DBConnect.deleteAll();
//        DBConnect.populateDB();


//        new WebApp("luca.armaroli","kiara4Lif3!");
//        new WebApp();
        new LoginForm("lorenzo.romio", "Burton86!");

//        try {
//            new HomeForm(Bank.login("lorenzo.romio","Burton86!"));
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (CredentialException e) {
//            e.printStackTrace();
//        } catch (AccountNotFoundException e) {
//            e.printStackTrace();
//        }

    }
}
