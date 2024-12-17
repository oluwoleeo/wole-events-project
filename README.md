# Wole's Events app

[[_TOC_]]

---

## Introduction

In today's fast-paced world, the convenience of booking systems has become an essential aspect of daily life. From booking tickets for a concert or reserving a spot at a conference, these systems are used widely by individuals and businesses alike.

---

## System Description

The system will allow users to create, find and reserve tickets for events, view and manage their reservations and to be notified before the event kickoff.

A **user** has:
- name (limited to 100 characters);
- email (valid email format);
- password (minimum 8 characters).

An **event** has:
- name (limited to 100 characters);
- date (in a valid date format);
- available attendees count (positive integer limited to 1000);
- event description (limited to 500 characters).
- category (Concert, Conference, Game)

The system is made up of a set of REST service APIs based on the swagger file provided - [swagger file](events-app-contract/src/main/resources/static/events-app-swagger.yml), that allows users to:

- Create an account.
- User authentication to log into the system.
- Create events.
- Search and reserve tickets for events.
- Send notification . Introduce a periodic task to send notifications to users for upcoming events/before event starts and create history/audit event log for this.


