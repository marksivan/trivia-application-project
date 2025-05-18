This is a Java-based trivia game with a real-time adaptive difficulty system. Questions dynamically adjust in difficulty based on player performance to maintain an engaging and challenging experience.

## 🧠 Game Logic

▶️ Start with medium-difficulty questions.

🔼 Answer 3 questions correctly in a row → the next question is hard 🟩 (worth more points)

🔽 Answer 3 questions incorrectly in a row → the next question is easy 🟥 (worth fewer points)

✅ After getting the first correct answer on an easy question → return to medium difficulty

🧠 Difficulty adapts in real-time to keep the game dynamic and personalized.


The game uses **min-heaps and max-heaps** to manage and prioritize questions:

-  **Max-Heap**: Stores hard questions and allows quick access to the most challenging ones.
-  **Min-Heap**: Stores easy questions and ensures the easiest available question is served when needed.
-  **ArrayList**: Maintains medium-difficulty questions in random order for balanced gameplay.


## How to Compile and Run

Open your terminal and follow the steps below:
-----------------------------------------------
Step 1: Navigate to the project directory
```
cd trivia-application-project
```

Step 2: Compile all Java files to the bin directory

```
javac -d bin $(find trivia -name "*.java")
```
 
Step 3: Move into the compiled classes directory

```
cd bin
```
Step 4: Run the application

```
java GUI.MainFrame
```