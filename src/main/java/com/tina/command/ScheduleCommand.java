package com.tina.command;

import com.tina.service.Storage;
import com.tina.service.Ui;
import com.tina.task.DeadlineTask;
import com.tina.task.EventTask;
import com.tina.task.Task;
import com.tina.task.TaskList;
import java.time.LocalDateTime;

/**
 * Represents a Schedule command.
 * Shows all the tasks related to the given date.
 */
public class ScheduleCommand extends Command {
    private final LocalDateTime date;

    /**
     * Instantiates a new Schedule command.
     *
     * @param date the date
     */
    public ScheduleCommand(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Iterates the task list and find the task that related to the given date.
     * Adds these found tasks to a new task list.
     * Calls method from ui to print the new task list.
     *
     * @param taskList the task list.
     * @param ui       the ui util.
     * @param storage  the storage util.
     * @return message and matching tasks, or not found message if no task date matches.
     */
    @Override
    public String execute(TaskList taskList, Ui ui, Storage storage) {
        TaskList tasks = new TaskList();
        for (Task task : taskList.getTaskList()) {
            if (task instanceof DeadlineTask) {
                DeadlineTask deadlineTask = (DeadlineTask) task;
                LocalDateTime by = deadlineTask.getBy();

                // If date ends with 00:00:00, compare only the Date parts
                if (date.toLocalTime().equals(LocalDateTime.MIN.toLocalTime())) {
                    if (date.toLocalDate().isEqual(by.toLocalDate())) {
                        tasks.add(deadlineTask);
                    }
                } else {
                    if (by.isEqual(date)) {
                        tasks.add(deadlineTask);
                    }
                }
            } else if (task instanceof EventTask) {
                EventTask eventTask = (EventTask) task;
                LocalDateTime from = eventTask.getFrom();
                LocalDateTime to = eventTask.getTo();

                // If date ends with 00:00:00, compare only the Date parts
                if (date.toLocalTime().equals(LocalDateTime.MIN.toLocalTime())) {
                    if (date.toLocalDate().isEqual(from.toLocalDate()) || date.toLocalDate().isEqual(to.toLocalDate())) {
                        tasks.add(eventTask);
                    }
                } else {
                    if (from.isBefore(date) && to.isAfter(date)) {
                        tasks.add(eventTask);
                    }
                }
            }
        }
        if (tasks.size() == 0) {
            return "No Deadlines/Events occurring on " + date + ".";
        } else {
            return "Deadlines/Events occurring on " + date + ":\n" + ui.printTaskList(tasks);
        }
    }
}
