#  TRIVIA APPLICATION

Created and Designed by: **Irenee Niyibaho & Mark Sivan Tamakloe**

This is both a Terminal-based and GUI-based Java trivia game application with a bank of questions in different fields that adopts our real-time adaptive questioning and recommendation system. This local app models the user experience based on real world experience. Questions dynamically adjust in difficulty based on a player's performance to maintain an engaging and challenging experience.

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


## How to Compile and Run:

Open your terminal and follow the steps below:
-----------------------------------------------

Step 1: Clone the repository
```
git clone https://github.com/marksivan/trivia-application-project.git
```

Step 2: Navigate to the project directory
```
cd trivia-application-project
```

Step 3: Compile all Java files to the bin directory

```
javac -d bin $(find trivia -name "*.java")
```
 
Step 4: Move into the compiled classes directory

```
cd bin
```
Step 5: Run the application:

GUI-based game app:
```
java GUI.MainFrame
```
Terminal-based game app:
```
java trivia.BACKEND.SimulateGame
```

## 📁 Project Structure

```
trivia-application-project/
├── bin/                            # Compiled .class files
│
├── trivia/
│   ├── BACKEND/
│   │   ├── QUESTIONS/              # CSV question banks (geography, math, science, etc.)
│   │   ├── MaxHeap.java
│   │   ├── MinHeap.java
│   │   ├── PriorityQueue.java
│   │   ├── Question.java
│   │   └── SimulateGame.java       # Game logic and adaptive system (Terminal app entry point)
│   │
│   ├── FRONTEND/
│   │   ├── DOM/
│   │   │   ├── ComponentNode.java
│   │   │   └── GenericComponents.java
│   │   └── GUI/
│   │       ├── AuthManager.java
│   │       ├── GameController.java       (Connector of FRONTEND to BACKEND)
│   │       ├── HomePage.java
│   │       ├── LoginPage.java
│   │       ├── MainFrame.java      # GUI entry point
│   │       ├── RegisterPage.java
│   │       └── Settings.java
│
└── README.md
```
## License

    Copyright [2025] [Irenee Niyibaho & Mark S. Tamakloe]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
