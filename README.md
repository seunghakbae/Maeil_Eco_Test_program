# maeil-economy-test-practice-program-java-hwplib

## Contents
* About Project
* Motivation
* Structure
* Results
* Tech/framework used

&nbsp;

## About Project

This program shows questions of maeil_economy_Exam when given keywords. It was created using java and [hwplib library](https://github.com/neolord0/hwplib).

&nbsp;

## Motivation
Maeil Economy Newspapers holds [MK Test of Economic & Strategic business Thinking](http://exam.mk.co.kr/) annually and created this program to prepare for the test.

&nbsp;

## Structure
Below picture shows how the program runs. Orange box represents questions and green box represents function. Each step is shown as numbers next to each box, and when quit is typed, the program ends. At step 2, you can download questions. At step 3, you type in keywords and questions are listed at step 4. Answers are checked at step 5 and 6. Finally questions and answers can be downloaded as text file at step 7 and 8.

![](/pic/structure.png)

Below diagram shows structure of program. Since MK test problem file is HWP file, used hwp file library. [https://github.com/neolord0/hwplib](https://github.com/neolord0/hwplib) supports creation of hwp file as instance and extracts text.

![](/pic/structure2.png)


&nbsp;

## Results
**Start**
![](/pic/result1.png)
![](/pic/result2.png)
![](/pic/result3.png)
Simple explanantion of the program and question to download test problems are shown.

**Download problems**
![](/pic/result4.png)
![](/pic/result5.png)
![](/pic/result6.png)

매경테스트 기출문제집.DEA.hwp is downloaded into your desktop at this stage.

**keyword**
![](/pic/result7.png)
![](/pic/result8.png)
Type in keyword.

**Questions**
![](/pic/result9.png)
Problems related to typed in keywords are shown.

**Answer Check**
![](/pic/result10.png)
![](/pic/result11.png)

If you want answers, type in yes.

**Answer List**
![](/pic/result12.png)
You can find answers to questions.

**Save as txt file**
![](/pic/result13.png)
![](/pic/17.png)
If you type in yes, file is saved as "Questions&Answers.txt".


## Tech/framework used
<b>Built with</b>
- java
- [hwplib](https://github.com/neolord0/hwplib)

&nbsp;
