package yqr.ui;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import yqr.task.Todo;

class UiTest {
    @Test
    void showTaskAdded_taskAndCountProvided_allLinesEmittedInOrder() {
        List<String> outputLines = new ArrayList<>();
        Ui ui = new Ui(outputLines::add);

        ui.showTaskAdded(new Todo("read book"), 1);

        assertIterableEquals(List.of(
                "Got it. I've added this task:",
                "  [T][ ] read book",
                "Now you have 1 task in the list."), outputLines);
    }
}
