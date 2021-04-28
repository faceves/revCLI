package tutorapp

import tutorapp.utils.ConnectionUtil
import tutorapp.utils.FileUtil
import tutorapp.utils.JSONUtil
import tutorapp.utils.DatabaseUtil
import scala.util.matching.Regex
import scala.io.StdIn
import scala.util.{Try, Success,Failure}
import java.io.FileNotFoundException
import org.postgresql.util.PSQLException
import tutorapp.dao.StudentDAO
import tutorapp.dao.ExamsDAO
import tutorapp.objHolders.Student
import tutorapp.objHolders.Exams
import scala.collection.mutable.ArrayBuffer

class Cli {
    private val commandPattern: Regex =  "(\\w+)\\s*.*".r
    private val jsonFileName = "Students.json"
    private val jsonFilePath = "."

    def run(){
        //check if connection is established otherwise exit
        val conn = Try(ConnectionUtil.getConnection())
        conn match{
            case Success(_) => {}
            case Failure(e) =>{
                println("Database connection not established, please retry again.")
                System.exit(1)
            }
        }
        //DatabaseUtil.loadJSONFile(jsonFilePath, jsonFileName)
        //DatabaseUtil.loadJSONFile(jsonFilePath, "Exams.json")
        menu()
    }
    def menu(){
        var contMenuLoop : Boolean = true

        printGreeting()
        do{
            printMenuOptions()

            var userInput:String = StdIn.readLine() //blocking
            
            userInput match {

                case commandPattern(cmd) if cmd == "loadjson"=>{
                    DatabaseUtil.loadJSONFile(jsonFilePath, jsonFileName)
                    DatabaseUtil.loadJSONFileE(jsonFilePath, "Exams.json")
                }
                case commandPattern(cmd) if cmd == "studentlist" => {
                    runStudentListMenu()                    
                }
                case commandPattern(cmd) if cmd == "student" => {
                    runGetStudentMenu()                    
                }
                case commandPattern(cmd) if cmd == "studentexams" => {
                    runStudentExamsMenu()                    
                }
                case commandPattern(cmd) if cmd == "updateclassgrade" => {
                    runUpdateClassGradeMenu()                  
                }
                case commandPattern(cmd) if cmd == "addstudent" => {
                    runAddStudentMenu()                   
                }
                case commandPattern(cmd) if cmd == "addexams" => {
                    runAddExamsMenu()                   
                }
                case commandPattern(cmd) if cmd == "deletestudent" => {
                    runDeleteStudentMenu()                  
                }
                case commandPattern(cmd) if cmd == "exit" => {
                    contMenuLoop = false
                }
                case commandPattern(cmd) => {
                    println(s"Parsed command $cmd did not correspond to an option")
                }
                case _ => {
                    println("Failed to parse command.")
                }
            }

        }while(contMenuLoop)
    }


    def runStudentListMenu(): Unit = {
        var userDone = false
        var userInput =  ""
        val idPattern = "([0-9]+)".r
        
        do{
            println("Enter the Class ID to retrieve the list: ")
            userInput = StdIn.readLine()

            //checks for valid input pattern
            userInput match{
                case idPattern(classid) =>{
                    val studentList: ArrayBuffer[Student] = StudentDAO.getStudentListFromClass(classid.toInt)
                    //checks to see if the class id exists, 
                    //an exception can be thrown within getStudnetListFromClass and return empty
                    if(!studentList.isEmpty){
                        //print out result set in readable format
                        Student.printColumnHeader()
                        studentList.foreach(println)
                        userDone = verifyUserFinished()
                    }
                    else
                        println(s"No such class exists with id: $classid.\n")
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format: Numeric Class ID (e.g: 12).")
            }       
        }while(!userDone)
    }

    def runGetStudentMenu(): Unit = {
        var userDone = false
        var userInput =  ""
        val idPattern = "([A-z][0-9]{5})".r //alphanumeric
         do{
            println("Enter the Student ID to retrieve the students info: ")
            userInput = StdIn.readLine()

            //checks for valid input pattern
            userInput match{
                case idPattern(id) =>{
                    val studentTry: Try[Student] = StudentDAO.getStudent(id)
                    //check for exceptions, exception can consist of a message of student does not exist
                    studentTry match{
                        case Success(student) => {
                            Student.printColumnHeader()
                            println(student)
                        }
                        case Failure(e) => println(e.getMessage()) 
                    }
                    userDone = verifyUserFinished()
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format, Alphanumeric Student ID " +
                  "with length of 6 characters(e.g: A12345):")
            }       
        }while(!userDone)
    }

    def runStudentExamsMenu(): Unit = {
        var userDone = false
        var userInput =  ""
        val idPattern = "([A-z][0-9]{5})".r //alphanumeric
         do{
            println("Enter the Student ID to retrieve the students exams: ")
            userInput = StdIn.readLine()

            //checks for valid input pattern
            userInput match{
                case idPattern(id) =>{
                    val examsTry: Try[Exams] = ExamsDAO.getStudentExams(id)
                    //check for exceptions, exception can consist of a message of student does not exist
                    examsTry match{
                        case Success(exams) =>{
                            Exams.printColumnHeader()
                            println(exams)
                        }
                        case Failure(e) => println( e.getMessage()) 
                    }
                    userDone = verifyUserFinished()
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format, Alphanumeric Student ID " +
                  "with length of 6 characters(e.g: A12345):")
            }       
        }while(!userDone)
    }

 /** TODO
    def runLoadJsonMenu(argFilePath: String):Unit = {
        var contLoop = true;
        var userInput =  ""
        val extractArgs = "("
        do{
            println("Enter file path and file name (e.g: ./ FileName.json) :\n")
            //does not do any user input checking for formatting for args
            StdIn.readLine() match{
                case 

            }

            val studentList: ArrayBuffer[Student] = StudentDAO.getStudentListFromClass(userInput.toInt)
            studentList.foreach(println)
            print("\nDone? y or n\n")
            userInput = StdIn.readLine()
            if(userInput == "y")
                userDone = true
        }while(!userDone)
    }
    **/


    def runUpdateClassGradeMenu(): Unit = {
        var userDone = false
        var userInput =  ""
        val argPattern = "([A-z][0-9]{5})\\s+([0-9]+\\.?[0-9]*)".r  //alphanumeric decimal
          
        do{
            println("Enter the Student ID and the Class Grade as a decimal, (eg: A12345 99.5 ): ")
            userInput = StdIn.readLine()
            
            userInput match{
                //make sure thar user input is valid
                case argPattern(id, classgrade) =>{
                    //Makes sure that the update is caught if an exception is thrown
                    StudentDAO.updateStudentClassGrade(id, classgrade.toFloat) match{
                        case Success(updated) =>{
                            if(updated){
                                println("Update Successful!")
                            }
                            else
                                println("Update Unsuccesful! Student possibly does not exist.")
                        }
                        case Failure(e) => println("Update Unsuccesful! Exception: " + e.getMessage())
                    }
                    userDone = verifyUserFinished()
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format: Alphanumeric Student ID and Class Grade in decimal form.")
                
            }
        }while(!userDone)
    } 

    def runAddStudentMenu():Unit = {
        var userDone = false
        var userInput =  ""
        //regex:
        //alphanumeric alpha alpha  decimal   numeric
        //studentid    fname lname classgrade classid
        val argPattern = "([A-z][0-9]{5})\\s+([A-z]+)\\s+([A-z]+)\\s+([0-9]+\\.?[0-9]*)\\s+([0-9]+)".r  
          
        do{
            println("\nEnter the Student's information in the following format:")
            println("STUDENTID FIRSTNAME LASTNAME CLASSGRADE CLASSID\n")
            
            userInput = StdIn.readLine()
            
            userInput match{
                //make sure thar user input is valid
                case argPattern(studentid, fname, lname, classgrade, classid) =>{
                    //Makes sure that the insert is caught if an exception is thrown
                    val addedTry = StudentDAO.insertStudent(studentid, fname, lname, classgrade.toFloat, classid.toInt) 
                    addedTry match{
                        case Success(added) =>{
                            if(added){
                                println("Added Successfully!")
                            }
                            else
                                println("Add Unsuccesful! Student possibly already exists.")
                        }
                        case Failure(e) => println("Add Unsuccesful! Exception: " + e.getMessage())
                    }
                    userDone = verifyUserFinished()
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format: Alphanumeric Student ID and Class Grade in decimal form.")
                
            }
        }while(!userDone)
    }

    def runAddExamsMenu():Unit = {
        var userDone = false
        var userInput =  ""
        //regex:
        //decimal decimal decimal  decimal   alphanumeric
        //exam1    exam2  midterm  finalexam   studentid
        val argPattern = "([0-9]+\\.?[0-9]*)\\s+([0-9]+\\.?[0-9]*)\\s+([0-9]+\\.?[0-9]*)\\s+([0-9]+\\.?[0-9]*)\\s+([A-z][0-9]{5})".r  
          
        do{
            println("\nEnter the Exam's Grades in decimal for the Student in the following format:")
            println("EXAM1 EXAM2 MIDTERM FINAL STUDENTID\n")
            
            userInput = StdIn.readLine()
            
            userInput match{
                //make sure thar user input is valid
                case argPattern(exam1, exam2, midterm, finalexam, studentid) =>{
                    //Makes sure that the insert is caught if an exception is thrown
                    val addedTry = ExamsDAO.insertExams(exam1.toFloat, exam2.toFloat, midterm.toFloat, finalexam.toFloat, studentid) 
                    addedTry match{
                        case Success(added) =>{
                            if(added){
                                println("Added Successfully!")
                            }
                            else
                                println("Add Unsuccesful! Student possibly already exists.")
                        }
                        case Failure(e) => println("Add Unsuccesful! Exception: " + e.getMessage())
                    }
                    userDone = verifyUserFinished()
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format: DECIMAL DECIMAL DECIMAL DECIMAL ALPHANUMERIC.")
                
            }
        }while(!userDone)
    
    }

    def runDeleteStudentMenu():Unit = {
        var userDone = false
        var userInput =  ""
        val argPattern = "([A-z][0-9]{5})".r  //alphanumeric decimal
          
        do{
            println("Enter the Student ID to delete (eg: A12345 ): ")
            userInput = StdIn.readLine()
            
            userInput match{
                //make sure thar user input is valid
                case argPattern(id) =>{
                    //Makes sure that the update is caught if an exception is thrown
                    StudentDAO.deleteStudent(id) match{
                        case Success(deleted) =>{
                            if(deleted){
                                println("Delete Successful!")
                            }
                            else
                                println("Delete Unsuccesful! Student possibly does not exist.")
                        }
                        case Failure(e) => println("Delete Unsuccesful! Exception: " + e.getMessage())
                    }
                    userDone = verifyUserFinished()
                }
                case "exit" => userDone = true
                case _ => println("Please enter the correct format: Alphanumeric.\n")
                
            }
        }while(!userDone)
    }
    /**----------------- HELPER FUNCTIONS-------------------**/

    def verifyUserFinished(): Boolean = {
        var correctInput = false
        var isUserDone = false
        var userInput = ""
        do{
            println("\nAre you finished? 'y' or 'n':")
            userInput = StdIn.readLine()
            if(userInput == "y"){
                isUserDone = true
                correctInput = true
            }
            else if(userInput == "n"){
                correctInput = true
            }
            else 
                println("Please enter y for yes or n for no.")
        }while(!correctInput)
        isUserDone
    }

        /** Display welcome message to console
     * Input: nothing
     * Output: nothing
     * */
    private def printGreeting():Unit = {
        println()
        println("*"*60)
        println("\t\tWelcome to the Tutorapp!")
        println("*"*60)
    }

    /** Display Menu Options to console
     * Input: nothing
     * Output: nothing
     * */
    private def printMenuOptions():Unit = {
        List(
            "\nMenu Options:",
            "-------------",
            "loadjson:         Loads JSON files into database.",
            "studentlist:      Retrieves a student list given a class id.",
            "student:          Retrieves a student information, including their class grade.",
            "studentexams:     Retrieves a student's exams grades.",
            "updateclassgrade: Updates a student's class grade.",
            "addstudent:       Adds a new student.",
            "addexams:         Adds a student's exams.",
            "deletestudent:    Deletes a student.",
            "exit:             Exits the app.\n"
            ).foreach(println)
    }
}



