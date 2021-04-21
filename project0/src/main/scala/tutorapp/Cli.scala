package tutorapp

import tutorapp.utils.ConnectionUtil
import scala.util.matching.Regex
import scala.io.StdIn
import java.io.FileNotFoundException
import org.postgresql.util.PSQLException
import tutorapp.dao.StudentDAO
import tutorapp.objHolders.Student
import scala.collection.mutable.ArrayBuffer

class Cli {
    private val commandPattern: Regex =  "(\\w+)\\s*(.*)".r

    def menu(){
        var contMenuLoop : Boolean = true

        printGreeting()
        do{
            printMenuOptions()

            var userInput:String = StdIn.readLine() //blocking
            
            userInput match {
                case commandPattern(cmd, arg) if cmd == "echo" => {
                    println(arg)
                }
                case commandPattern(cmd, arg) if cmd == "conn" => {
                    try{
                        runGetStudentListMenu()
                        var conn = ConnectionUtil.getConnection()
                    }
                    catch{
                        case e: PSQLException => println(e.getMessage())
                    }
                    
                    
                }
                case commandPattern(cmd, arg) if cmd == "exit" => {
                    contMenuLoop = false
                }
                case commandPattern(cmd, arg) => {
                    println(s"Parsed command $cmd with args $arg did not correspond to an option")
                }
                case _ => {
                    println("Failed to parse command.")
                }
            }

        }while(contMenuLoop)
    }

    /** Display welcome message to console
     * Input: nothing
     * Output: nothing
     * */
    private def printGreeting():Unit = {
        println("Hello")
    }

    /** Display Menu Options to console
     * Input: nothing
     * Output: nothing
     * */
    private def printMenuOptions():Unit = {
        List(
            "Menu options:",
            "echo [word]: repeats word back to you",
            "exit: exits WC CLI"
            ).foreach(println)
    }

    def runGetStudentListMenu(): Unit = {
        var userDone = false
        var userInput =  ""

        //val commandPattern = 
        do{
            println("Enter the Class ID to retrieve the list: ")
            userInput = StdIn.readLine()
            val studentList: ArrayBuffer[Student] = StudentDAO.getStudentListFromClass(userInput.toInt)
            studentList.foreach(println)
            print("\nDone? y or n")
            userInput = StdIn.readLine()
            if(userInput == "y")
                userDone = true
        }while(!userDone)
    }
}
