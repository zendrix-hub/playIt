# PLAYIT / BasaTrack 
**An offline, edge-AI driven mobile literacy platform for Grade 3 students.**

### Problem Statement
Digital literacy tools often fail in rural or resource-constrained environments due to a strict reliance on cloud-based AI and stable internet. PLAYIT solves this by utilizing edge-computing to process voice recognition locally on the device, ensuring zero connectivity barriers.

### Tech Stack & Engineering Decisions
* **Language:** Kotlin 
* **Architecture:** MVVM (Model-View-ViewModel) - *Chosen to strictly decouple the UI from business logic, ensuring high testability and preventing the accumulation of technical debt.*
* **Edge-AI:** PocketSphinx - *Integrated for offline, zero-latency voice recognition.*
* **Media:** ExoPlayer - *Handles local audio delivery and playback.*

### Setup & Build Instructions
1. Clone the repository: `git clone https://github.com/zendrix-hub/playIt.git`
2. Open the cloned directory in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the application on an Android Emulator or physical device.

### My Role & Contributions
As Lead Developer and Technical Documentation Manager for this capstone, I authored the v3.0 Software Requirements Specification (SRS) and engineered the decoupled MVVM architecture to successfully integrate the offline edge-AI modules without compromising main-thread performance.
