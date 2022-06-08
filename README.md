# Cerbo IDE

Cerbo is an integrated development environment for developing BLAZINGLY fast programs in Brainfuck, yet isn't written in Rust. It is actually written in Java™ 17 and the completely irrelevant JavaFX UI framework that was removed since JDK 11.
The framework is added using a Gradle plugin because that's totally a reasonable way to load a library.
The plugin is very reliable and never randomly complains about improperly loaded JavaFX modules.

![image](https://user-images.githubusercontent.com/51381523/172697983-60efb77d-32f1-42b0-9560-c47eb7dadc9a.png)

It allows developers to see immediately see the results by immediately running the code using an integrated parser and interpreter.
After the developer stops writing code it will display the resulting output above the code and will display a memory dump on the right side of the application.
Input can be given using the field below each code sheet.

![image](https://user-images.githubusercontent.com/51381523/172698347-ebed8eca-0ad1-481d-8d53-eaacd77e1ec3.png)

## Tasks

- [x] ~~Fix JavaFX~~ ~~Fix JavaFX properly~~ ~~Fix JavaFX for real~~ Give up on fixing JavaFX and accept that life is unpredictable 
- [x] Execute code
- [x] Allow input
- [x] See output
- [x] Auto run code after editing
- [x] View memory dump
- [x] View statistics
- [x] See output specific for each line
- [ ] Prevent infinite loops from lagging out application
- [ ] Save / load files
- [ ] Syntax highlighting
- [ ] Custom highlighting for each level of brackets
- [ ] Show location of pointer specific for each line