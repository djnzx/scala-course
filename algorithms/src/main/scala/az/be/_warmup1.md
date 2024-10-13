|===========|
|  Task 1.  |
|===========|
Probably all of you aware of the general rule
of creating English sentences.
It is: subject + verb + object

------------
|  Given:  |
------------
  List<String> subjects = list("Noel", "The cat", "The dog")
  List<String> verbs =    list("wrote", "chased", "slept on")
  List<String> objects =  list("the book","the ball","the bed")

  List<String> sentences = all_possible(subjects, verbs, objects)
------------
|  Goal:   |
------------
To build all possible combinations of this words:

Noel wrote the book
Noel wrote the ball
Noel wrote the bed
Noel chased the book
Noel chased the ball
Noel chased the bed
Noel slept on the book
Noel slept on the ball
Noel slept on the bed
The cat wrote the book
The cat wrote the ball
The cat wrote the bed
The cat chased the book
The cat chased the ball
The cat chased the bed
The cat slept on the book
The cat slept on the ball
The cat slept on the bed
The dog wrote the book
The dog wrote the ball
The dog wrote the bed
The dog chased the book
The dog chased the ball
The dog chased the bed
The dog slept on the book
The dog slept on the ball
The dog slept on the bed


