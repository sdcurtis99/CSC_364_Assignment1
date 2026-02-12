public class Report {
    /*
       1. We synchronized all the methods that our Breath First Search algorithm would need to use in order to update our “Blackboard”(DynamicGridModel) and display the information on the GUI.
       Our BFS continues to constantly read/write, marking things like frontier, visited, and the path taken.
       We also synchronized our pause/resume methods because they are in charge of coordinating the BFS thread using wait/notify.
       This synchronization is important so that BFS can see the changes to “paused” and does not miss notifications.
       We also had to synchronize tryStartPathFinding/finishPathFinding so that BFS ends up only running one thread at a time.

       2. After commenting out our synchronization from all of our methods, we run into no real problems with our GUI.
          This is because our BFS thread primarily writes to the grid model while our EDT primarily reads from it in order to repaint the UI.
          These threads may overlap briefly when trying to read from the same cell, which could cause the EDT thread to read slightly old data, but the grid is updated almost immediately,
          so the inconsistency is too fast for the human eye to notice.
     */
}
