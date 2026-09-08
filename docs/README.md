# Duke User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Duplicate task detection

Longfrog automatically rejects a new task when an existing task has the same type and details. You do not need a
separate command to check for duplicates, and there is no force-add option.

Descriptions are compared without regard to letter case or repeated whitespace. Punctuation remains significant.
Completion state is ignored, so a completed task still prevents an otherwise identical task from being added.

- Todos match when their descriptions match.
- Deadlines match when their descriptions and deadline date-times match.
- Events match when their descriptions, start date-times, and end date-times match.

Tasks of different types are not duplicates. Deadlines at different times and events with different time ranges are
also not duplicates.

For example, after adding `todo Read Book`, entering `todo read   book` produces:

```text
Duplicate detected; task already exists at position 1: [T][ ] Read Book
```

The original task remains unchanged and the new task is not saved. However, `todo read book!` is accepted because
punctuation is significant.

If an existing save file already contains duplicate entries, Longfrog preserves them and displays a warning once at
startup:

```text
Warning: 2 duplicate task entries detected in saved data. Existing entries were preserved.
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
