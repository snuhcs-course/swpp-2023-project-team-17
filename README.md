# GoClass

Many classes at Seoul National University use an electronic attendance checking system that relies on either student ID card tagging or SNU application. This method is convenient and simple; However, due to its reliance on a single verification, missing it can result in a student being marked late or even absent, despite having attended the class. Often, for the sake of fairness, students in such situations are not granted any remediation. System Architecture
GoClass is an application that provides evidence to prove a students’ attendance. It aims to operate alongside, rather than replace, existing attendance checking system. Even if a student forgets to check in, they can submit evidence to prove their attendance and can be granted a remediation. Additionally, the channel formed between students and professors for submitting evidence can also be utilized for communication.

![Application Screenshot](https://drive.google.com/file/d/1RHogk9i01WZzmWfGehVdj0oDaXeHUmMw/view?usp=share_link)

## Features

- Attendance Check: When the designated class time arrives, the professor's smartphone, as the host of the class channel, starts advertising a Bluetooth signal, and the students' smartphones in the class channel begin scanning for it. The scan is performed once a minute throughout the class duration. A student is considered “in class” only if both the Bluetooth signal from the professor’s smartphone and the class Bluetooth beacon are detected. “Late” and “Absent” are determined based on the current standards: if a signal is not detected until 10 minutes after the class start (late) or not detected for 30 minutes or throughout the class (absent). All attendance check functions operate in the background. Since the background operation of applications has been restricted starting from Android 10, a notification method is used to address this issue. Both the professor's and the students' smartphones will display a notification indicating that the Bluetooth signal is either advertising or scanning when performing related activities. After the class ends, students can send their attendance information to the professor. The professor can then check the student's attendance status(present, late, absent), as well as the record of Bluetooth signal detection during the class, based on the attendance information submitted by the student.
- Chat: Students and professors participating in a class channel can exchange messages and comment on each message. The sender can edit the messages and comments they have sent.

## Getting Started
1. Download the app from the installation link below.
2. Allow all permissions required:
  - Location permissions to "Always allow"
  - Nearby Devices permissions
  - Notification Permissions
3. Log in using a Google id
4. Choose the user type (student or professor)
5. Create or join class

### Prerequisites

- Android Studio [version, e.g., 4.2.1]
- Minimum Android SDK Version [e.g., 21]

### Installation

[Installation link here]
