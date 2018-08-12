import java.sql.*;
import java.util.Date;

/*
 * Database class for the captstone project
 * Will input, output and filter the information stored in the database
 * Coded by Jonathon Everatt, Scott Hallauer and Jessica Bourn
 *

 */
public class Database {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement PrepStat = null;
    private ResultSet resultSet = null;

    Database(){ //This is the constructor fort the new user calass
        try {  //Necessary to put in a try catch block
            connectToDB();
        } catch (Exception e){
            System.out.println("Database connection failed.");
        }

    }

    private void connectToDB() throws Exception{
        try{
            //load the MySQL driver
            Class.forName(("com.mysql.jdbc.Driver"));

            // Setting up the connection
            String connectURL = "jdbc:mysql://localhost:3306/STMS1";
            String user = "root"; //This is whatever we set our user name and password to
            String password = "jonrules1021";
            connect = DriverManager.getConnection(connectURL, user, password);
            System.out.println("Connected");
        } catch (Exception e){
            e.printStackTrace(); //Purely for debug purposes
            throw e;//
        }   finally {
            System.out.println("try catch block completed.");//Debugg
            //close(); //Check this is necessary and doesn't break shit
        }
    }
    //this class just connects, filtering and inserting will use specific methods

    //This is going to be the template for all SQL queries we use
    public ResultSet filterDB(String query){
        try{
            PrepStat = connect.prepareStatement(query);
            resultSet = PrepStat.executeQuery();
        } catch (SQLException e){
            System.out.println("ERROR!");
            e.printStackTrace();
        }
        return resultSet;
    }
	
    public void WriteToDB(String query){
        try {
            PrepStat = connect.prepareStatement(query);
            PrepStat.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Failed to write to DB");
        }

    }
}
