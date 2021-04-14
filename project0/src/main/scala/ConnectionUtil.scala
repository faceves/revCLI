import java.sql.Connection
import java.sql.DriverManager

object ConnectionUtil {
  
    // recommended: environment variables and connectin pool to make 5 and only 5 and wait tilll one opens
    private var username : String = "";
    private var password : String = "";
    var conn: Connection = null;

    /** utility for retrieving connection, with hardcorded credentials
        *
        * This should properly be a connection pool.  Instead, we'll use it
        * like a connection pool with a single connection, that gets returned
        * whenever any part of our application needs it
        *
        * @return Connection
        */
    def getConnection(): Connection = {

        // if conn is null or closed, initialize it
        if (conn == null || conn.isClosed()) {
            classOf[org.postgresql.Driver].newInstance() // manually load the Driver

            //missing a bit of documentation from the java code:
            // getConnection takes a url, username, password
            // hardcoding these at the moment, though this is v bad practice
            conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/",
                username,
                password
            )
        }
        // return conn, potentially after initialization
        conn
    }

    def setUsername(newUsername: String):Unit = {
        username = newUsername
    }

    def getUsername() : String = {
        username
    }

    def setPassword(newPassword: String) : Unit = {
        password = newPassword
    }

    def getPassword() : Unit = {
        password
    }
}
