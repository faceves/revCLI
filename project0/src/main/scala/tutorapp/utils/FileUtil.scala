package tutorapp.utils


import java.io.File
import java.io.FileNotFoundException
import scala.io.BufferedSource
import scala.io.Source

object FileUtil {
   
    /**
    * Retrieves the text content of a file as a string.
    * 
    * Accessing files is an exception-prone process -- it's very easy for some problems to occur
    * We should keep this in mind
    *
    * @throws FileNotFoundException
    * @param filename
    * @return entire file content
    */
  def getTextContent(filename: String): String = {
    //Whenever we open an external resource like a file or a database connection
    // we want to close it once we're done with it.  Otherwise it remains open, using our
    // resources and a connection slot on our DB server (until it gets cleaned up at some future point)

    //declare our opened file
    var openedFile: BufferedSource = null

    //to ensure our connection is closed even in the case of errors, we wrap our connection logic
    // in a try block, and we close the connection in a finally block
    try {
      openedFile = Source.fromFile(filename)
      // get the entire content of the file as one big String:
      openedFile.getLines().mkString(" ")
    } 
    catch{
      case e: FileNotFoundException => { 
        println(e.getMessage())
        ""
      }
    }
    finally {
      if (openedFile != null) openedFile.close()
    }
  }
    
}
