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

    Database(){
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
            connect = DriverManager.getConnection("jdbc:mysql://localhost/STMS\"\n" +
                    "                            + \"user=root&password=jonrules1021");
        } catch (Exception e){
            e.printStackTrace(); //Purely for debug purposes
            throw e;//
        }   finally {
            //close(); //Check this is necessary and doesn't break shit
        }
    }
    //this class just connects, filtering and inserting will use specific methods

    //This is going to be the template for all SQL queries we use
    public ResultSet filterOffUserID(int userID){
        String query ="SELECT * FROM user WHERE userID = " + userID;
        try{
            PrepStat = connect.prepareStatement(query);
            resultSet = PrepStat.executeQuery();
        } catch (SQLException e){
            System.out.println("ERROR!");
            e.printStackTrace();
        }
        return resultSet;

    }

}
