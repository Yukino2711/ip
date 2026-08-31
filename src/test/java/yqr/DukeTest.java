package yqr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DukeTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void getResponse_addThenList_responsesReturnedAndTaskPersisted() {
        Path dataFile = temporaryDirectory.resolve("tasks.txt");
        Duke duke = new Duke(dataFile.toString());

        String addResponse = duke.getResponse("todo read book");
        String listResponse = duke.getResponse("list");

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 task in the list.", addResponse);
        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book", listResponse);
        assertTrue(dataFile.toFile().isFile());
    }

    @Test
    void getResponse_invalidCommand_errorReturned() {
        Duke duke = new Duke(temporaryDirectory.resolve("tasks.txt").toString());

        String response = duke.getResponse("unknown");

        assertEquals("Please input valid commands", response);
    }

    @Test
    void getResponse_byeThenAnotherCommand_sessionRemainsEnded() {
        Duke duke = new Duke(temporaryDirectory.resolve("tasks.txt").toString());

        String goodbyeResponse = duke.getResponse("bye");
        String afterExitResponse = duke.getResponse("list");

        assertEquals("Bye. Hope to see you again soon!", goodbyeResponse);
        assertTrue(duke.hasExited());
        assertEquals("This session has ended. Restart yqr to enter more commands.", afterExitResponse);
    }
}
