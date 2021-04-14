import scala.util.matching.Regex
import scala.io.StdIn
import java.io.FileNotFoundException

class Cli {
    private val commandPattern: Regex =  "(\\w+)\\s*(.*)".r

    def menu(){
        var contMenuLoop : Boolean = true

        printGreeting()
        do{
            printMenuOptions()

            var userInput:String = StdIn.readLine()
            
            userInput match {
                case commandPattern(cmd, arg) if cmd == "echo" => {
                    println(arg)
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
            "getfiles: lists files in current directory",
            "first100chars [filename]: print the first 100 characters of a file",
            "wordcount [filename]: print the count of each word in a file",
            "exit: exits WC CLI"
            ).foreach(println)
    }
}
