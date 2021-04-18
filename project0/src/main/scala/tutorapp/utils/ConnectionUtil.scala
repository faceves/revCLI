package tutorapp

import java.sql.Connection
import java.sql.DriverManager

object ConnectionUtil {
  
    // recommended: environment variables and connectin pool to make 5 and only 5 and wait tilll one opens

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

            //grab connection for database through Java Drivermanager
            // getConnection takes a url, username, password
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
        
        val loginInfo : Option[String] = None
        val loginFlag : Char = 'u'
        if(loginCredential.equals("username"))
            loginInfo = sys.env.get("$DB_USERNAME")
        else{
            loginInfo = sys.env.get("$DB_PASSWORD")
            loginFlag = 'p'
        }
    
        loginInfo match{
            case Some(s) => s
            case None => { 
                if(loginFlag == 'u')
                    println("No username found.")
                else
                    println("No password found.") 
                ""
            }
        }    
    }

    private def getDBPassword(): String = {
        val password : Option[String] = sys.env.get("$DB_PASSWORD")
        password match{
            case Some(s) => s
            case None => { 
                println("No password found.") 
                return ""
            }
        }    
    }

    
}
