# Changelog `1.1.1`  &rarr;  `1.2.0`

Note: Changes in *italic* are changes not visible to users.

## New Features

- *Dream-Attachments are included into backups*

## Minor Changes

- When no emotions/tags/persons have been added into the app, and the user has to select one, an
  informative dropdown-entry is shown, indicating that the user needs to add one
- Added spacing between the y-axis and the first bar of a histogram
- Added delete-buttons when viewing a person/dream or editing a tag/emotion
- *Removed kotlin opt-in statements where not necessary*

## Bug fixes

- Fixed a bug where dreams of the current day would not be included into the statistics
- Fixed a bug where saving a new tag was not possible, because the user did not explicitly select a color
- Fixed a bug where file-timestamps were not added to files in zip-exports