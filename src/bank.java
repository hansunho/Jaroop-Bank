import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * This class consists exclusively of static methods. Methods in this class are used to 
 * simulate banking transaction for a user. User can either deposit, withdraw, check balance, or exit program by typing these commands.
 * 
 * @author sun
 */
public class bank {
	// where log.html is located 
	static String fileLocation;
	
	/**
	 * Checks if given amount follows the specifications of valid money amount
	 * 	1. must be non-negative 
	 *  2. must have no more than two decimal numbers
	 *  
	 * @param amount amount to be validated 
	 * @return return true if and only if amount is a non-zero double with no more than two decimal numbers, else return false
	 */
	static boolean checkValidity(double amount){

		//no negative amount including  -0.0
		if(amount < 0 || new Double(-0.0).equals(amount)) 
				return false;
		
		String strAmount=Double.toString(amount);
		//check amount has no more than 2 decimal numbers
		if(strAmount.length() - strAmount.indexOf('.')- 1 > 2)
			return false;		
		
		return true;
	}
	
	

	/**
	 * Formats a given double amount by adding a trailing zero if amount only has one decimal number
	 * 
	 * @param amount amount that will be formatted to have two decimal numbers. 
	 * @return String with formatted amount so it has two decimal numbers 
	 */
	static String formatInputAmount(double amount){	
		String formattedAmount = Double.toString(amount);
		//if double amount only has one decimal place add an additional trailing zero
		if((formattedAmount.length() - 1) - formattedAmount.indexOf(".") ==1)
			return formattedAmount.concat("0");
		
		return formattedAmount;
		
	}
	
	/**
	 * Reads through log.html and return all tbody contents of table with id "transactions"
	 * @param fileLocation a location of where log.html file is located. full pathname and filename
	 * @return String array of transaction entries. each element in the array will contain dollar amount with two decimal numbers. 
	 * Positive numbers for deposit and Negative numbers for withdrawal
	 */
	static String[] fetchTransactions(){
		 File log = new File(fileLocation);
		 Document doc = null;		 
		 try {
			doc = Jsoup.parse(log, "UTF-8", "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		 
		Elements transactions = doc.getElementById("transactions").getElementsByTag("tbody");
		String[] transactionsEntries = transactions.text().split(" ");		
		
		return transactionsEntries;		
	}
	
	/**
	 * Takes a String amount and write that to log.html file. 
	 * it first gets the body of "transactions" table where the new entry 'amount' will be appended. 
	 * 
	 * 
	 * @param amount the amount of transaction that will be written in log.html
	 */
	static void writeTransaction(String amount){
		File log = new File(fileLocation);
		 Document doc = null;		 
		 try {
			doc = Jsoup.parse(log, "UTF-8", "");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Elements transactions = doc.select("table#transactions").select("tbody");
		transactions.append("<td>" + amount+"</td>");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(log,"UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		writer.write(doc.html()) ;
		writer.flush();
		writer.close();
		
	}
	
	
	
	/**
	 * User will be prompted to enter one of four commands (deposit, withdraw, balance, exit)
	 * Any command that does not match one of the four commands above will not be recognized and user will be prompted to choose one of the supported commands.
	 *- When executing 'deposit' command, user will be prompted to input amount. 
	 * 	If input amount is valid (non-negative double number with no more than two decimal numbers), transaction will be logged in log.html. 
 	 *- When executing 'withdraw' command, user will be prompted to input amount. 
	 * 	If input amount is valid (non-negative double number with no more than two decimal numbers), this transaction will be logged in log.html.
 	 * 	input amount will be automatically changed to negative for 'withdraw' command 
	 *- 'balance' command will add up all transactions from log.html and return user's current balance.
	 *- 'exit' command will end the program and exit.
	 * All of these transactions are logged in log.html, which needs to provided by the user as a command line input. 
	 * 
	 * @param args args[0] location of log.html and is passed as a(only) command line argument. location must be local address
	 */
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		fileLocation = args[0];

		while(true){  
		  System.out.print("Please enter in a command (Deposit, Withdraw, Balance, Exit) : ");
		  String command = scanner.nextLine().toLowerCase();
		  if(command.equals("deposit") || command.equals("withdraw")){
			  System.out.print(" Please enter an amount to deposit:");
			  if(scanner.hasNextDouble()){
				  double inputAmount = scanner.nextDouble(); 
				  if(checkValidity(inputAmount)){
					String formattedInput = formatInputAmount(inputAmount);
					if(command.equals("withdraw")) 
						formattedInput = "-" + formattedInput;
					writeTransaction(formattedInput);
				  }
				  else
					  System.out.println("Invalid amount, please follow the format $XX.YY");
			 }
			  else
				  System.out.println("Invalid amount, please follow the format $XX.YY");
			  scanner.nextLine();//consume rest of the line
		  }	 
		  
		  else if(command.equals("balance")){
			  String[] transactionEntries = fetchTransactions();
			  BigDecimal total =new BigDecimal(0);
				for(String transactionEntry : transactionEntries){
					total = total.add(new BigDecimal(transactionEntry));
				}
				System.out.println(" Your current balance is: "+total);
		  }

		  else if(command.equals("exit")){
			  System.out.println("You have exited, Thank You.");
			  scanner.close();
			  break;
		  }
		  
		  else
			System.out.println("Command: \""+command+"\" is not a valid command");
		}	 
	}
	
}
