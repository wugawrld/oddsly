# Information
The Sports Betting Tracker v1.0 is a Graphical User Interface designed to give 
users the ability to add, edit, and track sports bets, players, and teams.
## What it does
Functionalities of the Sports Betting Tracker v1.0 include:
+ The abilities to load or save (including with chosen name) a file that stores the 
current bets, teams, and players or quit the program under "File".
+ Read an about information pop-up under "Help".
+ Create a new bet, tracked player, and tracked team under "File".
+ Edit or delete bets, tracked players, and tracked teams.
+ View the information and statistics attributed to your bets, tracked players, and teams.
+ View minor analysis information including most profitable bet type, summary of bet outcomes, and more.
+ Utilize the ESPN API to get up-to-date information on a variety of league information to inform bets.

## How to run it
To run the Sports Betting Tracker v1.0 you can launch it through the Main class via 
IntelliJ (or another Java compiler application), launch it through the command line of
your device directly or through a .jar file.

For the direct method input this into your command line after navigating to the appropriate directory:

- java --module-path "C:\Program Files\Java\javafx-sdk-24\lib" --add-modules
javafx.controls,javafx.fxml rw.app.Main

For the .jar file method input this into your command line after navigating to the appropriate directory:
- java --module-path "C:\Program Files\Java\javafx-sdk-24\lib" --add-modules
javafx.controls,javafx.fxml -jar SportsBetTrackerGUI.jar

NOTE: Ensure your javafx-sdk files are up to date with the current versions and that the path you have to your accompanying files
is correctly called.
## How to interact with it
A quick start guide to interacting with Sports Betting Tracker v1.0 is as follows:
1. Launch Sports Betting Tracker v1.0.
2. Either load previously saved data (Bets, Players, Teams) or select a data type to add under "File" -> "Add".
3. Read the popup instructions for your specified data type, fill all fields with required information, click the
create / add button to create your bet / team / player.
4. Once completed click save and choose if you would like to add another bet / team / player (then repeat 3) or if you are 
finished. 
5. Your data will now be viewable after clicking its accompanying button. You may edit or delete your inputs by selecting
its accompanying button then clicking "Edit" or "Delete". If you have completed a bet you can update its outcome through the
edit button. 
6. Once you have bets with outcomes you can select an analysis option under the "Calculations and Analysis" menu. 
7. After you are finished editing, deleting, and adding your data save your file by selecting the "save" option under file.
Once you have saved quit the program by selecting "quit" under file.

## Developers
Developed for the CPSC 233 Final Project:

Developed by Jarod Rideout, Risha Vaghani and Sardar Waheed

For support,feedback or feature requests, please contact:
- jarod.rideout@ucalgary.ca
- risha.vaghani@ucalgary.ca
- sardar.waheed@ucalgary.ca