This is a Java-based trivia game with a real-time adaptive difficulty system. Questions dynamically adjust in difficulty based on player performance to maintain an engaging and challenging experience.

## 🧠 Game Logic

▶️ Start with medium-difficulty questions.

🔼 Answer 3 questions correctly in a row → the next question is hard 🟩 (worth more points)

🔽 Answer 3 questions incorrectly in a row → the next question is easy 🟥 (worth fewer points)

✅ After getting the first correct answer on an easy question → return to medium difficulty

🧠 Difficulty adapts in real-time to keep the game dynamic and personalized.


The game uses **min-heaps and max-heaps** to manage and prioritize questions:

- 📥 `MaxHeap.java`: Efficiently retrieves hard questions with the highest difficulty scores.
- 📤 `MinHeap.java`: Quickly accesses the easiest questions available.
- 📊 `PriorityQueue.java`: Manages underlying heap behavior to streamline dynamic transitions.
- 📋 Medium questions are selected randomly from an `ArrayList`.


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



<pre lang="markdown"> ## 📁 Project Structure ``` trivia-application-project/ ├── bin/ # Compiled .class files │ ├── trivia/ │ ├── BACKEND/ │ │ ├── QUESTIONS/ # CSV question banks (geography, math, science, etc.) │ │ ├── MaxHeap.java │ │ ├── MinHeap.java │ │ ├── PriorityQueue.java │ │ ├── Question.java │ │ └── SimulateGame.java # Game logic and adaptive system │ │ │ ├── FRONTEND/ │ │ ├── DOM/ │ │ │ ├── ComponentNode.java │ │ │ └── GenericComponents.java │ │ └── GUI/ │ │ ├── AuthManager.java │ │ ├── GameController.java │ │ ├── HomePage.java │ │ ├── LoginPage.java │ │ ├── MainFrame.java # GUI entry point │ │ ├── RegisterPage.java │ │ └── Settings.java │ └── README.md ``` </pre>