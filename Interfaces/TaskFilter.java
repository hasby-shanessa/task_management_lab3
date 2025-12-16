package Interfaces;

import Models.Task;

@FunctionalInterface
public interface TaskFilter {
    boolean test(Task task);
}
