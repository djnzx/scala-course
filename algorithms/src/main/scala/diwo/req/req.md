Hi, we kindly ask that you read the descriptions thoroughly and then create a program to solve the problems.
We request that you use Scala for the solution of the problem.
You may use external libraries or tools for building or testing purposes.
Specifically you may use unit testing libraries or build tools available for this programming language.
We also appreciate a brief explanation of your design and assumptions along with your code.

### CASE STUDY

EuroMillions system bets
Consider EuroMillions lottery rules, where in a normal EuroMillions ticket
field the customer can select
5 out of 50 numbers and
2 out of 11 "star"-numbers
(see http://www.tipp24.com/euromillions).

In a EuroMillions system ticket field the customer can select
up to 10 out of 50 numbers and
up to 5 out of 11 "star"-numbers leading to up to 2520 field combinations
(see http://www.tipp24.com/euromillions/systemschein/).

Your **first** task is to design a data structure to model a ticket that can contain
EuroMillions normal fields as well as system ticket fields.

Given a EuroMillions system ticket, implement the system expansion
that outputs all possible normal ticket field combinations
(i.e. out of a system ticket field with numbers 1,2,3,4,5
and with star numbers 1,2,3 it produces [1,2,3,4,5 1,2], [1,2,3,4,5 1,3] and [1,2,3,4,5 2,3]).

Your **second** task is to write a small application that reads the numbers
of a system ticket from a file and outputs all the normal ticket combinations.
Use the data structure you designed in the first task.

#### Consider following EuroMillions prize levels:

```Winning class 1  - 5 correct numbers + 2 correct star numbers
Winning class 2  - 5 correct numbers + 1 correct star number
Winning class 3  - 5 correct numbers
Winning class 4  - 4 correct numbers + 2 correct star numbers
Winning class 5  - 4 correct numbers + 1 correct star number
Winning class 6  - 4 correct numbers
Winning class 7  - 3 correct numbers + 2 correct star numbers
Winning class 8  - 2 correct numbers + 2 correct star numbers
Winning class 9  - 3 correct numbers + 1 correct star number
Winning class 10 - 3 correct numbers
Winning class 11 - 1 correct number  + 2 correct star numbers
Winning class 12 - 2 correct numbers + 1 correct star number
Winning class 13 - 2 correct numbers
```
Implement a EuroMillions winning calculation that,
given the draw results, and a set of tickets participating in the draw,
outputs the information about number of winning tickets
in each winning class for the set of input tickets.

Your **third task** is to extend the application from the second task so that:

- it reads the winning numbers of a draw (5 out of 50 and 2 out of 11) from a file
- it reads tickets that participated in the draw from a file
on the file system (you are free to use any format you like).
The file should contain normal as well as system tickets,
so that you can use the system ticket expansion.
- it outputs an overview about winnings for the draw
in a form Winning class N - number of winning tickets X

#### General rules:

- Implement your solution in scala.
- Assume that the application will be executed in a Linux environment.
- In case of any ambiguity in the assignment
decide for yourself how exactly you can implement it.
