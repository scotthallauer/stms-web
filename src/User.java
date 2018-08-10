public class User {

    private int userID;
    private String name;
    private String email;
    private String pwdHash;
    private String pwdSalt;

    User() {

    }

    User(int userID) {

    }

    User(String email) {

    }

    public boolean checkLogin (String password) {

    }

    public boolean forgotPassword (String email) {

    }

    public void addSemester (Semester semester) {

    }

    public Semester[] getSemesters() {

    }

    public boolean savetoDB() {

    }
}

