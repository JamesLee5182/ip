# UI Test Plan

This file is the source of truth for console UI test cases run with the `test-ui` skill.

## Test session configuration

- Launch command: `java -Dstdout.encoding=UTF-8 -cp out/production/ip longfrog.Longfrog` (compile current sources and run with Java 25).
- Start a fresh session for each case, run cases in order, and stop after the first failure.
- Compare the output literally, including blank lines and each 50-character `─` separator. User input is not console output.

## Shared console output

Each session starts without printing a greeting or other startup output.

After every input, the corresponding expected response below is inserted literally into this block. If the response has multiple lines, it replaces `RESPONSE` in full.

```text
──────────────────────────────────────────────────
RESPONSE

──────────────────────────────────────────────────

```

The complete expected output is the startup output plus one response block per input, in order. No normalization is allowed.

## Test cases

### TC-001: List an empty task list

- Aim: Verify the empty-list response and ordinary exit.

  | Input | Expected output |
  | --- | --- |
  | `list` | `ok buddy`<br>`you didn't anything yet. What do you want from me?` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-002: Add every supported task type and list them

- Aim: Verify valid `todo`, `deadline`, and `event` commands.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Sure dude. I added: [T][ ] read book` |
  | `deadline return book /by 2/12/2019 1800` | `Sure dude. I added: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `event team meeting /from 2/12/2019 1400 /to 2/12/2019 1600` | `Sure dude. I added: [E][ ] team meeting (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `list` | `ok buddy`<br>`1: [T][ ] read book`<br>`2: [D][ ] return book (by: Dec 02 2019, 6:00 pm)`<br>`3: [E][ ] team meeting (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-003: Mark and unmark an existing task

- Aim: Verify valid state changes are shown by `list`.

  | Input | Expected output |
  | --- | --- |
  | `todo submit assignment` | `Sure dude. I added: [T][ ] submit assignment` |
  | `mark 1` | `I marked the task: submit assignment` |
  | `list` | `ok buddy`<br>`1: [T][X] submit assignment` |
  | `unmark 1` | `I unmarked the task: submit assignment` |
  | `list` | `ok buddy`<br>`1: [T][ ] submit assignment` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-004: Reject missing task numbers without changing the list

- Aim: Interleave valid and invalid task-number commands to verify failed operations preserve state.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Sure dude. I added: [T][ ] read book` |
  | `mark 2` | `I can't do that. The task doesn't exist` |
  | `list` | `ok buddy`<br>`1: [T][ ] read book` |
  | `mark 1` | `I marked the task: read book` |
  | `unmark 0` | `Dude, task numbers start at 1.` |
  | `list` | `ok buddy`<br>`1: [T][X] read book` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-005: Reject empty and unknown commands without ending the session

- Aim: Verify invalid commands are handled and later commands still use the existing task list.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Sure dude. I added: [T][ ] read book` |
  | `todo` | `Dude, use: todo TASK` |
  | _empty line_ | `Bruh, can you enter a command?` |
  | `dance` | `Bruh, I don't know that command.` |
  | `list` | `ok buddy`<br>`1: [T][ ] read book` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-006: Reject incomplete deadline and event inputs without adding tasks

- Aim: Verify malformed `deadline` and `event` commands do not add partial tasks.

  | Input | Expected output |
  | --- | --- |
  | `deadline submit report /by 2/12/2019 1800` | `Sure dude. I added: [D][ ] submit report (by: Dec 02 2019, 6:00 pm)` |
  | `deadline missing date` | `Dude, use: deadline TASK /by d/M/yyyy HHmm` |
  | `event tutorial /from 2/12/2019 1400 /to 2/12/2019 1600` | `Sure dude. I added: [E][ ] tutorial (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `event missing end /from 2/12/2019 1400` | `Dude, use: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm` |
  | `list` | `ok buddy`<br>`1: [D][ ] submit report (by: Dec 02 2019, 6:00 pm)`<br>`2: [E][ ] tutorial (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-007: Reject malformed task-number inputs without changing completion state

- Aim: Verify non-integer and missing task numbers leave the task unmarked.

  | Input | Expected output |
  | --- | --- |
  | `todo revise notes` | `Sure dude. I added: [T][ ] revise notes` |
  | `mark first` | `Dude, the task number must be a valid integer!` |
  | `list` | `ok buddy`<br>`1: [T][ ] revise notes` |
  | `unmark` | `Dude can you specify a task number like: unmark 1` |
  | `list` | `ok buddy`<br>`1: [T][ ] revise notes` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-008: Accept command keywords regardless of letter case

- Aim: Verify keyword case normalization still works after validation.

  | Input | Expected output |
  | --- | --- |
  | `TODO Read Book` | `Sure dude. I added: [T][ ] Read Book` |
  | `LIST` | `ok buddy`<br>`1: [T][ ] Read Book` |
  | `BYE` | `I'm going to sleep. Bye.` |

### TC-009: Delete a task and preserve the remaining task order

- Aim: Verify that `delete INDEX` removes the selected task, reindexes later tasks, and leaves the list unchanged after an invalid deletion.

  | Input | Expected output |
  | --- | --- |
  | `todo first task` | `Sure dude. I added: [T][ ] first task` |
  | `todo second task` | `Sure dude. I added: [T][ ] second task` |
  | `todo third task` | `Sure dude. I added: [T][ ] third task` |
  | `delete 4` | `I can't do that. The task doesn't exist` |
  | `list` | `ok buddy`<br>`1: [T][ ] first task`<br>`2: [T][ ] second task`<br>`3: [T][ ] third task` |
  | `delete 2` | `Sure dude. I deleted: second task` |
  | `list` | `ok buddy`<br>`1: [T][ ] first task`<br>`2: [T][ ] third task` |
  | `delete` | `Dude can you specify a task number like: delete 1` |
  | `list` | `ok buddy`<br>`1: [T][ ] first task`<br>`2: [T][ ] third task` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-010: Find tasks by a case-insensitive description keyword

- Aim: Verify `find KEYWORD` returns matching tasks in their original order and excludes non-matching tasks.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Sure dude. I added: [T][ ] read book` |
  | `todo go running` | `Sure dude. I added: [T][ ] go running` |
  | `deadline return book /by 2/12/2019 1800` | `Sure dude. I added: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `find BOOK` | `Here are the matching tasks in your list:`<br>`1: [T][ ] read book`<br>`2: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `bye` | `I'm going to sleep. Bye.` |

### TC-011: Use the clearer date command

- Aim: Verify `date d/M/yyyy` lists matching date-based tasks.

  | Input | Expected output |
  | --- | --- |
  | `deadline return book /by 2/12/2019 1800` | `Sure dude. I added: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `date 2/12/2019` | `Here are the tasks happening on 2/12/2019:`<br>`1: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `bye` | `I'm going to sleep. Bye.` |
