# SpiralTest

The Spiral Test has 2 modes, a practice mode and a trial mode. In the practice mode, the user will be able to try out the test to see how it works with no time limit. In trial mode, the user will have a set time limit to draw the spiral. The amount of time depends on the difficulty level (10 seconds for Easy, 15 seconds for Medium, and 20 seconds for Hard). A help mode is also available in case the user needs to review the instructions.

# Researcher instructions: 

There are three levels for the Spiral Test. Easy has the spiral with the fewest number of loops, Medium has one more loop, and Hard has one more loop than Medium. The difficulty of the trial will be set with an integer 1-3, 1 representing Easy, 2 representing Medium, and 3 representing Hard.
Scores and screenshots of the trials will be sent to the excel spreadsheet for the reviewal.

# About the Results: 

After a user takes the test in trial mode a results page will display the user's score, accuracy percentage, and time spent. The score gives 50% weight to the accuracy of the user's drawn spiral, and 50% weight to the percentage of pixels missed from the original spiral. This sum is multiplied by 1 + the time remaining, and a factor which increases with greater difficulty. Thus, a higher difficulty has greater potential to display a higher score. Additionally, the score does not represent a percentage and is not capped at 100.
