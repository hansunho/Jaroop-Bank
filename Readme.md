Readme.md

How to run Bank.jar
$ java -jar Bank.jar [local file address of log.html]
Please make sure Jave version >= 1.7


I used Jsoup library to easily parse and edit html file. Jsoup allows to easily select elements in html.

My approach to this problem 

'deposit' and 'withdraw' are pretty much the same functions (e.g. input amount has to conform to the same specifications) other than that 
user input from 'deposit' is logged as a positive number and input from 'withdraw' is logged as negative.
So they are treated as the same until input is logged where "-" will be prepended to the original input. 

when user inputs deposit/withdraw amount, that input is first validated by scanner.hasNextDouble() and read in as a double by caling scanner.nextDouble().
the reason i used nextDouble() was to reduce the complexity of input validation. I thought about reading in String value, but that would need a pretty complex regex. (This was for the purpose of conciseness, however more strict check on input amount is ideal for more serious program)

'balance' was a little trickier to handle using doubles because they do not represent exact amount of number, doing arithmetic operations with doubles did not work.
Therefore, I used more precise representaion of numbers, BigDecimal, so I do not have to worry about many trailing decimals numbers. I read in the transactions as String and converted them to BigDecimal to add all. 

'exit' simply closes scannr and exits the while loop leading to end of program

In short, deposit and withdraw write new numbers to log.html and balance reads the written numbers.

