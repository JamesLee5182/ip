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
  | `list` | `Task database snapshot:`<br>`No tasks detected; the queue is an empty set. Ribbit.` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-002: Add every supported task type and list them

- Aim: Verify valid `todo`, `deadline`, and `event` commands.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Ribbit! Task compiled into the list: [T][ ] read book` |
  | `deadline return book /by 2/12/2019 1800` | `Ribbit! Task compiled into the list: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `event team meeting /from 2/12/2019 1400 /to 2/12/2019 1600` | `Ribbit! Task compiled into the list: [E][ ] team meeting (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] read book`<br>`2: [D][ ] return book (by: Dec 02 2019, 6:00 pm)`<br>`3: [E][ ] team meeting (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-003: Mark and unmark an existing task

- Aim: Verify valid state changes are shown by `list`.

  | Input | Expected output |
  | --- | --- |
  | `todo submit assignment` | `Ribbit! Task compiled into the list: [T][ ] submit assignment` |
  | `mark 1` | `Boolean state flipped to DONE: submit assignment` |
  | `list` | `Task database snapshot:`<br>`1: [T][X] submit assignment` |
  | `unmark 1` | `Boolean state reset to NOT DONE: submit assignment` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] submit assignment` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-004: Reject missing task numbers without changing the list

- Aim: Interleave valid and invalid task-number commands to verify failed operations preserve state.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Ribbit! Task compiled into the list: [T][ ] read book` |
  | `mark 2` | `Index error: no task exists at that position. Ribbit.` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] read book` |
  | `mark 1` | `Boolean state flipped to DONE: read book` |
  | `unmark 0` | `Index underflow: task numbers start at 1.` |
  | `list` | `Task database snapshot:`<br>`1: [T][X] read book` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-005: Reject empty and unknown commands without ending the session

- Aim: Verify invalid commands are handled and later commands still use the existing task list.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Ribbit! Task compiled into the list: [T][ ] read book` |
  | `todo` | `Syntax error. Expected: todo TASK` |
  | _empty line_ | `Input buffer is empty. Please enter a command, ribbit.` |
  | `dance` | `Unknown command token. My parser cannot compute that, ribbit.` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] read book` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-006: Reject incomplete deadline and event inputs without adding tasks

- Aim: Verify malformed `deadline` and `event` commands do not add partial tasks.

  | Input | Expected output |
  | --- | --- |
  | `deadline submit report /by 2/12/2019 1800` | `Ribbit! Task compiled into the list: [D][ ] submit report (by: Dec 02 2019, 6:00 pm)` |
  | `deadline missing date` | `Syntax error. Expected: deadline TASK /by d/M/yyyy HHmm` |
  | `event tutorial /from 2/12/2019 1400 /to 2/12/2019 1600` | `Ribbit! Task compiled into the list: [E][ ] tutorial (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `event missing end /from 2/12/2019 1400` | `Syntax error. Expected: event TASK /from d/M/yyyy HHmm /to d/M/yyyy HHmm` |
  | `list` | `Task database snapshot:`<br>`1: [D][ ] submit report (by: Dec 02 2019, 6:00 pm)`<br>`2: [E][ ] tutorial (from: Dec 02 2019, 2:00 pm to: Dec 02 2019, 4:00 pm)` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-007: Reject malformed task-number inputs without changing completion state

- Aim: Verify non-integer and missing task numbers leave the task unmarked.

  | Input | Expected output |
  | --- | --- |
  | `todo revise notes` | `Ribbit! Task compiled into the list: [T][ ] revise notes` |
  | `mark first` | `Type mismatch: task number must be an integer.` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] revise notes` |
  | `unmark` | `Index argument missing. Try: unmark 1` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] revise notes` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-008: Accept command keywords regardless of letter case

- Aim: Verify keyword case normalization still works after validation.

  | Input | Expected output |
  | --- | --- |
  | `TODO Read Book` | `Ribbit! Task compiled into the list: [T][ ] Read Book` |
  | `LIST` | `Task database snapshot:`<br>`1: [T][ ] Read Book` |
  | `BYE` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-009: Delete a task and preserve the remaining task order

- Aim: Verify that `delete INDEX` removes the selected task, reindexes later tasks, and leaves the list unchanged after an invalid deletion.

  | Input | Expected output |
  | --- | --- |
  | `todo first task` | `Ribbit! Task compiled into the list: [T][ ] first task` |
  | `todo second task` | `Ribbit! Task compiled into the list: [T][ ] second task` |
  | `todo third task` | `Ribbit! Task compiled into the list: [T][ ] third task` |
  | `delete 4` | `Index error: no task exists at that position. Ribbit.` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] first task`<br>`2: [T][ ] second task`<br>`3: [T][ ] third task` |
  | `delete 2` | `Garbage collection complete; removed: second task` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] first task`<br>`2: [T][ ] third task` |
  | `delete` | `Index argument missing. Try: delete 1` |
  | `list` | `Task database snapshot:`<br>`1: [T][ ] first task`<br>`2: [T][ ] third task` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-010: Find tasks by a case-insensitive description keyword

- Aim: Verify `find KEYWORD` returns matching tasks in their original order and excludes non-matching tasks.

  | Input | Expected output |
  | --- | --- |
  | `todo read book` | `Ribbit! Task compiled into the list: [T][ ] read book` |
  | `todo go running` | `Ribbit! Task compiled into the list: [T][ ] go running` |
  | `deadline return book /by 2/12/2019 1800` | `Ribbit! Task compiled into the list: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `find BOOK` | `Search algorithm complete. Matching specimens:`<br>`1: [T][ ] read book`<br>`2: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `find holiday` | `Search returned zero matches. The pond is quiet.` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |

### TC-011: Use the clearer date command

- Aim: Verify `date d/M/yyyy` lists matching date-based tasks.

  | Input | Expected output |
  | --- | --- |
  | `deadline return book /by 2/12/2019 1800` | `Ribbit! Task compiled into the list: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `date 2/12/2019` | `Temporal query complete for 2/12/2019:`<br>`1: [D][ ] return book (by: Dec 02 2019, 6:00 pm)` |
  | `date 3/12/2019` | `Temporal query returned zero tasks for 3/12/2019.` |
  | `bye` | `Ribbit and good night! Shutting down the lily-pad terminal.` |
