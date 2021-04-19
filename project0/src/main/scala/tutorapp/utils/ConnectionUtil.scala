package tutorapp

import java.sql.Connection
import java.sql.DriverManager
import org.postgresql.util.PSQLException

object ConnectionUtil {
  
    // recommended: environment variables and connectin pool to make 5 and only 5 and wait tilll one opens
    var conn: Connection = null
    //environment variables for database login
    val dbUsernameEnv = "DB_USERNAME"
    val dbPasswordEnv = "DB_PASSWORD"

    /** utility for retrieving connection
        *
        * This should properly be a connection pool.  Instead, we'll use it
        * like a connection pool with a single connection, that gets returned
        * whenever any part of our application needs it
        *
        * @return Connection
        */
    def getConnection(): Connection = {
        var conn = null
        // if conn is null or closed, initialize it
        if (conn == null || conn.isClosed()) {
            classOf[org.postgresql.Driver].newInstance() // manually load the Driver

            //grab connection for database through JDBC Java Drivermanager
            conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/", 
                getDBLoginInfo("username"),
                getDBLoginInfo("password")
                )
        }
        // return conn, potentially after initialization
        conn
    }

    private def getDBLoginInfo(loginCredential:String): String = {
        var loginInfo : Option[String] = None
        if(loginCredential.equals("username"))
            loginInfo = sys.env.get(dbUsernameEnv)
        else
            loginInfo = sys.env.get(dbPasswordEnv)
    
        loginInfo match{
            case Some(s) => {
                println(s) 
                s
            }
            case None => { 
                if(loginCredential.equals("username"))
                    println("No username found.")
                else
                    println("No password found.") 
                ""
            }
        }    
    }

    
}
